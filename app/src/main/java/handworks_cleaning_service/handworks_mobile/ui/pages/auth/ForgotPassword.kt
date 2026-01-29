package handworks_cleaning_service.handworks_mobile.ui.pages.auth

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
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
import handworks_cleaning_service.handworks_mobile.ui.viewmodel.AuthViewModel
import handworks_cleaning_service.handworks_mobile.utils.NavigationUtil
import handworks_cleaning_service.handworks_mobile.utils.uistate.ResetPasswordUiState
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForgotPassword : ComponentActivity() {
    private lateinit var authViewModel: AuthViewModel
    private var emailStep: View? = null
    private var codeStep: View? = null
    private var newPasswordStep: View? = null
    private var resetEmailInput: EditText? = null
    private var codeFieldInput: EditText? = null
    private var newPasswordFieldInput: EditText? = null
    private var confirmNewPasswordFieldInput: EditText? = null
    private var btnSendResetEmail: Button? = null
    private var btnBackResetEmail: Button? = null
    private var btnValidateCode: Button? = null
    private var btnBackResetCode: Button? = null
    private var btnUpdatePassword: Button? = null
    private var btnBackResetPassword: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        initWidgets()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewModel.resetPasswordUiState.collect { state ->
                    when (state) {
                        ResetPasswordUiState.NeedsFirstFactor -> showCodeStep()
                        ResetPasswordUiState.NeedsSecondFactor -> showNewPasswordStep()
                        ResetPasswordUiState.NeedsNewPassword -> showNewPasswordStep()
                        ResetPasswordUiState.Complete -> {
                            Toast.makeText(this@ForgotPassword, "Password updated!", Toast.LENGTH_SHORT).show()
                            NavigationUtil.navigateTo(this@ForgotPassword, Login::class.java)
                            finish()
                        }
                        else -> { }
                    }
                }
            }
        }

        btnBackResetEmail?.setOnClickListener { NavigationUtil.navigateTo(this, Login::class.java) }
        btnSendResetEmail?.setOnClickListener {
            val resetEmail = resetEmailInput?.text.toString().trim()
            if (resetEmail.isEmpty()) {
                Toast.makeText(this, "Email is required.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            authViewModel.createSignIn(resetEmail)
        }

        btnBackResetCode?.setOnClickListener { showEmailStep() }
        btnValidateCode?.setOnClickListener {
            val code = codeFieldInput?.text.toString().trim()
            if (code.isEmpty()) {
                Toast.makeText(this, "Code is required.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            authViewModel.verify(code)
        }

        btnBackResetPassword?.setOnClickListener { showCodeStep() }
        btnUpdatePassword?.setOnClickListener {
            val newPassword = newPasswordFieldInput?.text.toString()
            val confirmNewPassword = confirmNewPasswordFieldInput?.text.toString()
            when {
                newPassword.isEmpty() || confirmNewPassword.isEmpty() ->
                    Toast.makeText(this, "New password is required.", Toast.LENGTH_SHORT).show()

                newPassword != confirmNewPassword ->
                    Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show()

                else -> authViewModel.setNewPassword(newPassword)
            }
        }
    }

    private fun initWidgets() {
        emailStep = findViewById(R.id.emailStep)
        codeStep = findViewById(R.id.codeStep)
        newPasswordStep = findViewById(R.id.newPasswordStep)

        resetEmailInput = findViewById(R.id.resetEmailField)
        btnSendResetEmail = findViewById(R.id.btnSendResetEmail)
        btnBackResetEmail = findViewById(R.id.btnBackResetEmail)

        codeFieldInput = findViewById(R.id.codeField)
        btnValidateCode = findViewById(R.id.btnValidateCode)
        btnBackResetCode = findViewById(R.id.btnBackResetCode)

        newPasswordFieldInput = findViewById(R.id.newPasswordField)
        confirmNewPasswordFieldInput = findViewById(R.id.confirmNewPasswordField)
        btnUpdatePassword = findViewById(R.id.btnUpdatePassword)
        btnBackResetPassword = findViewById(R.id.btnBackResetPassword)
    }

    private fun showEmailStep() {
        emailStep?.visibility = View.VISIBLE
        resetEmailInput?.visibility = View.VISIBLE
        btnSendResetEmail?.visibility = View.VISIBLE

        codeStep?.visibility = View.GONE
        newPasswordStep?.visibility = View.GONE
    }

    private fun showCodeStep() {
        emailStep?.visibility = View.GONE
        codeStep?.visibility = View.VISIBLE
        newPasswordStep?.visibility = View.GONE
    }

    private fun showNewPasswordStep() {
        emailStep?.visibility = View.GONE
        codeStep?.visibility = View.GONE
        newPasswordStep?.visibility = View.VISIBLE
    }
}