package handworks_cleaning_service.handworks_mobile.data.repository

import android.util.Log
import com.clerk.api.Clerk
import com.clerk.api.Clerk.isInitialized
import com.clerk.api.Clerk.isSignedIn
import com.clerk.api.Clerk.session
import com.clerk.api.network.serialization.ClerkResult
import com.clerk.api.network.serialization.longErrorMessageOrNull
import com.clerk.api.network.serialization.onFailure
import com.clerk.api.network.serialization.onSuccess
import com.clerk.api.session.Session
import com.clerk.api.signin.SignIn
import com.clerk.api.signin.attemptFirstFactor
import com.clerk.api.signin.resetPassword
import com.clerk.api.user.User
import handworks_cleaning_service.handworks_mobile.data.dto.LoginRequest
import handworks_cleaning_service.handworks_mobile.data.remote.AuthApi
import handworks_cleaning_service.handworks_mobile.utils.Result
import handworks_cleaning_service.handworks_mobile.utils.SignInHelper
import java.lang.Boolean
import javax.inject.Inject
import kotlin.Exception
import kotlin.String
import kotlin.Throwable
import kotlin.Unit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthRepository @Inject constructor() : AuthApi {
    private var cachedUser: User? = null

    override suspend fun signIn(request: LoginRequest): Result<SignIn> {
        return try {
            val signInResult = suspendCoroutine<Result<SignIn>> { cont ->
                SignInHelper().signIn(request.email, request.password, object : SignInHelper.Callback {
                    override fun onSuccess(user: SignIn) {
                        cont.resume(Result.Success(user))
                    }
                    override fun onError(errorMessage: String) {
                        cont.resume(Result.Failure(Exception(errorMessage)))
                    }
                })
            }
            signInResult
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    override suspend fun signOut(): ClerkResult<Unit, Throwable> {
        return try {
            Clerk.signOut()
            ClerkResult.Success(Unit, tags = mapOf())
        } catch (e: Exception) {
            ClerkResult.Failure(e)
        }
    }

    override fun isClerkInitialized(): kotlin.Boolean {
        return Boolean.TRUE == isInitialized.value
    }

    override fun getSession(): Session? {
        return session
    }

    override fun isSignedIn(): kotlin.Boolean {
        return isSignedIn
    }

    override fun getUser(): User? {
        if (cachedUser != null) return cachedUser

        val session = getSession()
        cachedUser = session?.user
        return cachedUser
    }

    fun getCachedUser(): User? = cachedUser

    //region Forgot Password/Reset Password
    override suspend fun createSignIn(email: String): SignIn.Status {
        return try {
            SignIn.create(SignIn.CreateParams.Strategy.ResetPasswordEmailCode(email))
                .onSuccess { result ->
                    return result.status
                }
                .onFailure { error ->
                    Log.e("AuthRepository", error.longErrorMessageOrNull, error.throwable)
                    return SignIn.Status.UNKNOWN
                }
            SignIn.Status.UNKNOWN
        } catch (e: Exception) {
            Log.e("AuthRepository", "createSignIn exception", e)
            SignIn.Status.UNKNOWN
        }
    }

    override suspend fun verifyCode(code: String): SignIn.Status {
        val signIn = Clerk.signIn ?: return SignIn.Status.UNKNOWN
        return try {
            signIn.attemptFirstFactor(SignIn.AttemptFirstFactorParams.ResetPasswordEmailCode(code))
                .onSuccess { result ->
                    return result.status
                }
                .onFailure { error ->
                    Log.e("AuthRepository", error.longErrorMessageOrNull, error.throwable)
                    return SignIn.Status.UNKNOWN
                }
            SignIn.Status.UNKNOWN
        } catch (e: Exception) {
            Log.e("AuthRepository", "verifyCode exception", e)
            SignIn.Status.UNKNOWN
        }
    }

    override suspend fun setNewPassword(password: String): SignIn.Status {
        val signIn = Clerk.signIn ?: return SignIn.Status.UNKNOWN
        return try {
            signIn.resetPassword(SignIn.ResetPasswordParams(password))
                .onSuccess { result ->
                    return result.status
                }
                .onFailure { error ->
                    Log.e("AuthRepository", error.longErrorMessageOrNull, error.throwable)
                    return SignIn.Status.UNKNOWN
                }
            SignIn.Status.UNKNOWN
        } catch (e: Exception) {
            Log.e("AuthRepository", "setNewPassword exception", e)
            SignIn.Status.UNKNOWN
        }
    }
    //endregion
}