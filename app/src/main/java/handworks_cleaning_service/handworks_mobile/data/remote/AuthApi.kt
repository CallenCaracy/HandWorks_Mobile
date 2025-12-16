package handworks_cleaning_service.handworks_mobile.data.remote

import com.clerk.api.network.model.error.ClerkErrorResponse
import com.clerk.api.network.serialization.ClerkResult
import com.clerk.api.signin.SignIn
import handworks_cleaning_service.handworks_mobile.data.dto.LoginRequest
import handworks_cleaning_service.handworks_mobile.utils.Result

interface AuthApi {
    suspend fun signIn(request: LoginRequest): Result<SignIn>
    suspend fun signOut(): ClerkResult<Unit, Throwable>
}