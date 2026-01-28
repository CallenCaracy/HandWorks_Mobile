package handworks_cleaning_service.handworks_mobile.ui.pages.index;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.clerk.api.Clerk;

import dagger.hilt.android.AndroidEntryPoint;
import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.ui.pages.auth.Login;
import handworks_cleaning_service.handworks_mobile.ui.viewmodel.AuthViewModel;
import handworks_cleaning_service.handworks_mobile.utils.NavigationUtil;
import handworks_cleaning_service.handworks_mobile.utils.uistate.SessionUiState;

@AndroidEntryPoint
public class AppEntryScreenSplash extends AppCompatActivity {

    private ProgressBar progressBar;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_app_entry_screen_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        authViewModel = new ViewModelProvider(this)
                .get(AuthViewModel.class);

        progressBar = findViewById(R.id.progressBarLoading);
        
        authViewModel.getSessionState()
                .observe(this, state -> {

                    if (state instanceof SessionUiState.Loading
                            || state instanceof SessionUiState.Idle) {
                        progressBar.setVisibility(View.VISIBLE);
                    }

                    else if (state instanceof SessionUiState.Authenticated) {
                        progressBar.setVisibility(View.GONE);
                        NavigationUtil.navigateTo(this, Dashboard.class);
                        finish();
                    }

                    else if (state instanceof SessionUiState.Unauthenticated) {
                        progressBar.setVisibility(View.GONE);
                        NavigationUtil.navigateTo(this, Login.class);
                        finish();
                    }

                    else if (state instanceof SessionUiState.Error) {
                        progressBar.setVisibility(View.GONE);
                        showRetryUI();
                    }
                });

        authViewModel.checkSession();
    }

    private void showRetryUI() {
        TextView message = findViewById(R.id.tvRetryMessage);
        Button retryButton = findViewById(R.id.btnRetry);

        message.setVisibility(View.VISIBLE);
        retryButton.setVisibility(View.VISIBLE);

        retryButton.setOnClickListener(v -> {
            message.setVisibility(View.GONE);
            retryButton.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            authViewModel.checkSession();
        });
    }
}