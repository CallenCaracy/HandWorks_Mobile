package handworks_cleaning_service.handworks_mobile.ui.pages.auth;

import static handworks_cleaning_service.handworks_mobile.utils.Constant.MAX_RETRIES;
import static handworks_cleaning_service.handworks_mobile.utils.NetworkConnectivity.isInternetAvailable;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import handworks_cleaning_service.handworks_mobile.utils.SignInUiState;

@AndroidEntryPoint
public class Login extends AppCompatActivity {
    private ProgressBar progressBar;
    private Button signInBtn;
    private AuthViewModel authViewModel;
    protected LoginRequest request;
    private int retryCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_general_loading);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        waitForSessionReady();
    }

    private void waitForSessionReady() {
        if (!isInternetAvailable(this)) {
            setContentView(R.layout.activity_login);
            setupLoginUI();
            Toast.makeText(this,"No internet connection", Toast.LENGTH_LONG).show();
            return;
        }

        if (retryCount >= MAX_RETRIES) {
            setContentView(R.layout.activity_login);
            setupLoginUI();
            return;
        }
        retryCount++;

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            var session = Clerk.INSTANCE.getSession();
            if (session != null) {
                NavigationUtil.navigateTo(Login.this, Dashboard.class);
                finish();
            } else {
                waitForSessionReady();
            }
        }, 500);
    }

    private void setupLoginUI() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        EditText emailEditText = findViewById(R.id.emailField);
        EditText passwordEditText = findViewById(R.id.passwordField);
        signInBtn = findViewById(R.id.btnSignIn);
        progressBar = findViewById(R.id.progressBar);


        authViewModel.getUiState().observe(this, uiState -> {
            if (uiState instanceof SignInUiState.Loading) {
                progressBar.setVisibility(View.VISIBLE);
                signInBtn.setEnabled(false);
            } else {
                progressBar.setVisibility(View.GONE);
                signInBtn.setEnabled(true);
                if (uiState instanceof SignInUiState.Success) {
                    Toast.makeText(this, "Welcome!", Toast.LENGTH_SHORT).show();
                    NavigationUtil.navigateTo(this, Dashboard.class);
                } else if (uiState instanceof SignInUiState.Error) {
                    String error = ((SignInUiState.Error) uiState).getMessage();
                    Toast.makeText(this, "Login failed: " + error, Toast.LENGTH_LONG).show();
                }
            }
        });

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
    }
}