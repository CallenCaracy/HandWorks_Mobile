package handworks_cleaning_service.handworks_mobile.utils

import com.clerk.api.network.serialization.longErrorMessageOrNull
import com.clerk.api.network.serialization.onSuccess
import com.clerk.api.network.serialization.onFailure
import com.clerk.api.signin.SignIn
import kotlinx.coroutines.*

class SignInHelper {
    interface Callback {
        fun onSuccess(user: SignIn)
        fun onError(errorMessage: String)
    }

    fun signIn(email: String, password: String, callback: Callback) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val result = SignIn.create(SignIn.CreateParams.Strategy.Password(email, password))
                result.onSuccess { user ->
                    callback.onSuccess(user)
                }.onFailure {
                    callback.onError(it.longErrorMessageOrNull ?: "Unknown error")
                }
            } catch (e: Exception) {
                callback.onError(e.message ?: "Exception occurred")
            }
        }
    }
}
