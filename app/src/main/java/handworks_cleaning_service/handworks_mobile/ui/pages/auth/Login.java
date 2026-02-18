package handworks_cleaning_service.handworks_mobile.ui.pages.auth;

import static handworks_cleaning_service.handworks_mobile.utils.NetworkConnectivity.isInternetAvailable;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.clerk.api.Clerk;

import dagger.hilt.android.AndroidEntryPoint;
import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.data.dto.LoginRequest;
import handworks_cleaning_service.handworks_mobile.databinding.ActivityLoginBinding;
import handworks_cleaning_service.handworks_mobile.ui.pages.index.Dashboard;
import handworks_cleaning_service.handworks_mobile.ui.viewmodel.AuthViewModel;
import handworks_cleaning_service.handworks_mobile.utils.NavigationUtil;
import handworks_cleaning_service.handworks_mobile.utils.uistate.AuthUiState;

@AndroidEntryPoint
public class Login extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private AuthViewModel authViewModel;
    protected LoginRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdge.enable(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (Clerk.INSTANCE.isSignedIn()) {
            Toast.makeText(this, "Already signed in", Toast.LENGTH_SHORT).show();
            NavigationUtil.navigateTo(this, Dashboard.class);
        }

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        request = new LoginRequest();

        binding.btnForgotPassword.setOnClickListener(v -> NavigationUtil.navigateTo(this, ForgotPassword.class));

        binding.btnSignIn.setOnClickListener(v -> {
            if (!isInternetAvailable(this)) {
                Toast.makeText(this, "Please connect to the internet", Toast.LENGTH_SHORT).show();
                return;
            }

            String emailInput = binding.emailField.getText() != null ? binding.emailField.getText().toString().trim() : "";
            String passwordInput = binding.passwordField.getText() != null ? binding.passwordField.getText().toString() : "";

            if (emailInput.isEmpty() || passwordInput.isEmpty()) {
                Toast.makeText(this, "Email and password are required.", Toast.LENGTH_SHORT).show();
                return;
            }

            request.email = emailInput;
            request.password = passwordInput;

            authViewModel.signIn(request);
        });
        observeAuthState();
    }

    private void observeAuthState() {
        authViewModel.getAuthState().observe(this, uiState -> {
            if (uiState instanceof AuthUiState.Loading) {
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.btnSignIn.setEnabled(false);
                return;
            }

            binding.progressBar.setVisibility(View.GONE);
            binding.btnSignIn.setEnabled(true);

            if (uiState instanceof AuthUiState.Success) {
                Toast.makeText(this, "Welcome!", Toast.LENGTH_SHORT).show();
                NavigationUtil.navigateTo(this, Dashboard.class);
            } else if (uiState instanceof AuthUiState.Error) {
                String error = ((AuthUiState.Error) uiState).getMessage();
                Toast.makeText(this, "Login failed: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }
}