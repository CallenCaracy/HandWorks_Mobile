package handworks_cleaning_service.handworks_mobile.data.repository

import com.clerk.api.signin.SignIn
import handworks_cleaning_service.handworks_mobile.data.remote.AuthApi
import handworks_cleaning_service.handworks_mobile.utils.Result
import handworks_cleaning_service.handworks_mobile.utils.SignInHelper
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthRepository @Inject constructor() : AuthApi {
    override suspend fun signIn(email: String, password: String): Result<SignIn> {
        return try {
            val signInResult = suspendCoroutine<Result<SignIn>> { cont ->
                SignInHelper().signIn(email, password, object : SignInHelper.Callback {
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
}