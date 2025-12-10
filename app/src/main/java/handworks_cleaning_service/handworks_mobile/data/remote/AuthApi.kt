package handworks_cleaning_service.handworks_mobile.data.remote

import com.clerk.api.signin.SignIn
import handworks_cleaning_service.handworks_mobile.utils.Result

interface AuthApi {
    suspend fun signIn(email: String, password: String): Result<SignIn>
}