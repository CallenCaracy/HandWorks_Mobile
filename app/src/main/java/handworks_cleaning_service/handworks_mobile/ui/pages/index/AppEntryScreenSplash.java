package handworks_cleaning_service.handworks_mobile.ui.pages.index;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import dagger.hilt.android.AndroidEntryPoint;
import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.databinding.ActivityAppEntryScreenSplashBinding;
import handworks_cleaning_service.handworks_mobile.ui.pages.auth.Login;
import handworks_cleaning_service.handworks_mobile.ui.viewmodel.AuthViewModel;
import handworks_cleaning_service.handworks_mobile.utils.NavigationUtil;
import handworks_cleaning_service.handworks_mobile.utils.uistate.SessionUiState;

@AndroidEntryPoint
public class AppEntryScreenSplash extends AppCompatActivity {
    private ActivityAppEntryScreenSplashBinding binding;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAppEntryScreenSplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdge.enable(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        
        authViewModel.getSessionState()
                .observe(this, state -> {

                    if (state instanceof SessionUiState.Loading
                            || state instanceof SessionUiState.Idle) {
                        binding.progressBarLoading.setVisibility(View.VISIBLE);
                    }

                    else if (state instanceof SessionUiState.Authenticated) {
                        binding.progressBarLoading.setVisibility(View.GONE);

                        NavigationUtil.navigateTo(this, Dashboard.class);
                    }

                    else if (state instanceof SessionUiState.Unauthenticated) {
                        binding.progressBarLoading.setVisibility(View.GONE);
                        NavigationUtil.navigateTo(this, Login.class);
                    }

                    else if (state instanceof SessionUiState.Error) {
                        binding.progressBarLoading.setVisibility(View.GONE);
                        showRetryUI();
                    }
                });

        authViewModel.checkSession();
    }

    private void showRetryUI() {
        binding.tvRetryMessage.setVisibility(View.VISIBLE);
        binding.btnRetry.setVisibility(View.VISIBLE);

        binding.btnRetry.setOnClickListener(v -> {
            binding.tvRetryMessage.setVisibility(View.GONE);
            binding.btnRetry.setVisibility(View.GONE);
            binding.progressBarLoading.setVisibility(View.VISIBLE);

            authViewModel.checkSession();
        });
    }
}