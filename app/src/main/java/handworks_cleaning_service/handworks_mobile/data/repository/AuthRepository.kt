package handworks_cleaning_service.handworks_mobile.data.repository

import android.util.Log
import com.clerk.api.signin.SignIn
import com.clerk.api.Clerk
import com.clerk.api.network.serialization.ClerkResult
import com.clerk.api.network.serialization.longErrorMessageOrNull
import com.clerk.api.network.serialization.onFailure
import com.clerk.api.network.serialization.onSuccess
import com.clerk.api.session.Session
import com.clerk.api.signin.attemptFirstFactor
import com.clerk.api.signin.resetPassword
import handworks_cleaning_service.handworks_mobile.data.dto.LoginRequest
import handworks_cleaning_service.handworks_mobile.data.remote.AuthApi
import handworks_cleaning_service.handworks_mobile.utils.Result
import handworks_cleaning_service.handworks_mobile.utils.SignInHelper
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthRepository @Inject constructor() : AuthApi {
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

    override suspend fun getSession(): Session? {
        return Clerk.session
    }

    override suspend fun signOut(): ClerkResult<Unit, Throwable> {
        return try {
            Clerk.signOut()
            ClerkResult.Success(Unit, tags = mapOf())
        } catch (e: Exception) {
            ClerkResult.Failure(e)
        }
    }

    //region Forgot Password/Reset Password
    override suspend fun createSignIn(email: String, onResult: (SignIn.Status) -> Unit) {
        SignIn.create(SignIn.CreateParams.Strategy.ResetPasswordEmailCode(email))
            .onSuccess { onResult(it.status) }
            .onFailure {
                Log.e("AuthRepository", it.longErrorMessageOrNull, it.throwable)
            }
    }

    override suspend fun verifyCode(code: String, onResult: (SignIn.Status) -> Unit) {
        val signIn = Clerk.signIn ?: return
        signIn.attemptFirstFactor(SignIn.AttemptFirstFactorParams.ResetPasswordEmailCode(code))
            .onSuccess { onResult(it.status) }
            .onFailure {
                Log.e("AuthRepository", it.longErrorMessageOrNull, it.throwable)
            }
    }

    override suspend fun setNewPassword(password: String, onResult: (SignIn.Status) -> Unit) {
        val signIn = Clerk.signIn ?: return
        signIn.resetPassword(SignIn.ResetPasswordParams(password))
            .onSuccess { onResult(it.status) }
            .onFailure {
                Log.e("AuthRepository", it.longErrorMessageOrNull, it.throwable)
            }
    }
    //endregion
}