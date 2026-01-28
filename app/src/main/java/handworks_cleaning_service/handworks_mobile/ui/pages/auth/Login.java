package handworks_cleaning_service.handworks_mobile.ui.pages.auth;

import static handworks_cleaning_service.handworks_mobile.utils.NetworkConnectivity.isInternetAvailable;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import handworks_cleaning_service.handworks_mobile.ui.pages.index.Dashboard;
import handworks_cleaning_service.handworks_mobile.ui.viewmodel.AuthViewModel;
import handworks_cleaning_service.handworks_mobile.utils.NavigationUtil;
import handworks_cleaning_service.handworks_mobile.utils.uistate.AuthUiState;

@AndroidEntryPoint
public class Login extends AppCompatActivity {
    private ProgressBar progressBar;
    private Button signInBtn;
    private AuthViewModel authViewModel;
    protected LoginRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
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
        EditText emailEditText = findViewById(R.id.emailField);
        EditText passwordEditText = findViewById(R.id.passwordField);
        signInBtn = findViewById(R.id.btnSignIn);
        Button btnForgotPassword = findViewById(R.id.btnForgotPassword);
        progressBar = findViewById(R.id.progressBar);

        btnForgotPassword.setOnClickListener(v -> NavigationUtil.navigateTo(this, ForgotPassword.class));

        signInBtn.setOnClickListener(v -> {
            if (!isInternetAvailable(this)) {
                Toast.makeText(this, "Please connect to the internet", Toast.LENGTH_SHORT).show();
                return;
            }

            request.email = emailEditText.getText().toString().trim();
            request.password = passwordEditText.getText().toString();

            if (request.email.isEmpty() || request.password.isEmpty()) {
                Toast.makeText(this, "Email and password are required.", Toast.LENGTH_SHORT).show();
                return;
            }

            authViewModel.signIn(request);
        });
        observeAuthState();
    }
    private void observeAuthState() {
        authViewModel.getAuthState().observe(this, uiState -> {
            if (uiState instanceof AuthUiState.Loading) {
                progressBar.setVisibility(View.VISIBLE);
                signInBtn.setEnabled(false);
                return;
            }

            progressBar.setVisibility(View.GONE);
            signInBtn.setEnabled(true);

            if (uiState instanceof AuthUiState.Success) {
                Toast.makeText(this, "Welcome!", Toast.LENGTH_SHORT).show();
                NavigationUtil.navigateTo(this, Dashboard.class);
                finish();
            } else if (uiState instanceof AuthUiState.Error) {
                String error = ((AuthUiState.Error) uiState).getMessage();
                Toast.makeText(this, "Login failed: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }
}