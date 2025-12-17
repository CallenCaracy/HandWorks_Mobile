package handworks_cleaning_service.handworks_mobile.data.repository

import com.clerk.api.signin.SignIn
import com.clerk.api.Clerk
import com.clerk.api.network.serialization.ClerkResult
import com.clerk.api.session.Session
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
}