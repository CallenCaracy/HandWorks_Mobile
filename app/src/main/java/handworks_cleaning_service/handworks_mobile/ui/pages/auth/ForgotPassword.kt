package handworks_cleaning_service.handworks_mobile.ui.pages.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import handworks_cleaning_service.handworks_mobile.R
import handworks_cleaning_service.handworks_mobile.databinding.ActivityForgotPasswordBinding
import handworks_cleaning_service.handworks_mobile.ui.pages.index.AppEntryScreenSplash
import handworks_cleaning_service.handworks_mobile.ui.viewmodel.AuthViewModel
import handworks_cleaning_service.handworks_mobile.utils.NavigationUtil
import handworks_cleaning_service.handworks_mobile.utils.uistate.ResetPasswordUiState
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForgotPassword : ComponentActivity() {
    private lateinit var authViewModel: AuthViewModel
    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewModel.resetPasswordUiState.collect { state ->
                    when (state) {
                        ResetPasswordUiState.NeedsFirstFactor -> showCodeStep()
                        ResetPasswordUiState.NeedsSecondFactor -> showNewPasswordStep()
                        ResetPasswordUiState.NeedsNewPassword -> showNewPasswordStep()
                        ResetPasswordUiState.Complete -> {
                            Toast.makeText(this@ForgotPassword, "Password updated!", Toast.LENGTH_SHORT).show()
                            NavigationUtil.navigateTo(this@ForgotPassword, AppEntryScreenSplash::class.java)
                            finish()
                        }
                        else -> { }
                    }
                }
            }
        }

        binding.emailStep.btnBackResetEmail.setOnClickListener { NavigationUtil.navigateTo(this, Login::class.java) }
        binding.emailStep.btnSendResetEmail.setOnClickListener {
            val resetEmail = binding.emailStep.resetEmailField.text.toString().trim()
            if (resetEmail.isEmpty()) {
                Toast.makeText(this, "Email is required.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            authViewModel.createSignIn(resetEmail)
        }

        binding.codeStep.btnBackResetCode.setOnClickListener { showEmailStep() }
        binding.codeStep.btnValidateCode.setOnClickListener {
            val code = binding.codeStep.codeField.text.toString().trim()
            if (code.isEmpty()) {
                Toast.makeText(this, "Code is required.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            authViewModel.verify(code)
        }

        binding.newPasswordStep.btnBackResetPassword.setOnClickListener { showCodeStep() }
        binding.newPasswordStep.btnUpdatePassword.setOnClickListener {
            val newPassword = binding.newPasswordStep.newPasswordField.text.toString()
            val confirmNewPassword = binding.newPasswordStep.confirmNewPasswordField.text.toString()
            when {
                newPassword.isEmpty() || confirmNewPassword.isEmpty() ->
                    Toast.makeText(this, "New password is required.", Toast.LENGTH_SHORT).show()

                newPassword != confirmNewPassword ->
                    Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show()

                else -> authViewModel.setNewPassword(newPassword)
            }
        }
    }

    private fun showEmailStep() {
        binding.emailStep.root.visibility = View.VISIBLE
        binding.emailStep.resetEmailField.visibility = View.VISIBLE
        binding.emailStep.btnSendResetEmail.visibility = View.VISIBLE

        binding.codeStep.root.visibility = View.GONE
        binding.newPasswordStep.root.visibility = View.GONE
    }

    private fun showCodeStep() {
        binding.emailStep.root.visibility = View.GONE
        binding.codeStep.root.visibility = View.VISIBLE
        binding.newPasswordStep.root.visibility = View.GONE
    }

    private fun showNewPasswordStep() {
        binding.emailStep.root.visibility = View.GONE
        binding.codeStep.root.visibility = View.GONE
        binding.newPasswordStep.root.visibility = View.VISIBLE
    }
}