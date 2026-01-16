package handworks_cleaning_service.handworks_mobile.data.remote

import com.clerk.api.network.serialization.ClerkResult
import com.clerk.api.session.Session
import com.clerk.api.signin.SignIn
import handworks_cleaning_service.handworks_mobile.data.dto.LoginRequest
import handworks_cleaning_service.handworks_mobile.utils.Result

interface AuthApi {
    suspend fun signIn(request: LoginRequest): Result<SignIn>
    suspend fun getSession(): Session?
    suspend fun signOut(): ClerkResult<Unit, Throwable>

    //region Forgot Password/Reset Password
    suspend fun createSignIn(email: String): SignIn.Status
    suspend fun verifyCode(code: String): SignIn.Status
    suspend fun setNewPassword(password: String): SignIn.Status
    //endregion
}