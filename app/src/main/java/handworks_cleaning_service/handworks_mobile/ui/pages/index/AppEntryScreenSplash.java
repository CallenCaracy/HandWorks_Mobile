package handworks_cleaning_service.handworks_mobile.ui.pages.index;

import static handworks_cleaning_service.handworks_mobile.utils.Constant.MAX_RETRIES;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.data.models.users.Employee;
import handworks_cleaning_service.handworks_mobile.data.repository.config.FetchStrategy;
import handworks_cleaning_service.handworks_mobile.databinding.ActivityAppEntryScreenSplashBinding;
import handworks_cleaning_service.handworks_mobile.ui.pages.auth.Login;
import handworks_cleaning_service.handworks_mobile.ui.viewmodel.AuthViewModel;
import handworks_cleaning_service.handworks_mobile.ui.viewmodel.UserViewModel;
import handworks_cleaning_service.handworks_mobile.utils.NavigationUtil;
import handworks_cleaning_service.handworks_mobile.utils.uistate.SessionUIState;
import kotlinx.serialization.json.JsonElement;
import kotlinx.serialization.json.JsonObject;

@AndroidEntryPoint
public class AppEntryScreenSplash extends AppCompatActivity {
    private ActivityAppEntryScreenSplashBinding binding;
    private AuthViewModel authViewModel;
    private UserViewModel userViewModel;
    private String empId;
    private int retryCount = 0;
    @Inject
    SharedPreferences prefs;

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
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        authViewModel.getSessionState().observe(this, this::render);
        authViewModel.checkSession();
        binding.btnRetry.setOnClickListener(v -> authViewModel.checkSession());
    }

    private void render(SessionUIState state) {
        binding.progressBarLoading.setVisibility(View.GONE);
        binding.tvRetryMessage.setVisibility(View.GONE);
        binding.btnRetry.setVisibility(View.GONE);

        if (state instanceof SessionUIState.Loading || state instanceof SessionUIState.Idle) {
            binding.progressBarLoading.setVisibility(View.VISIBLE);
        } else if (state instanceof SessionUIState.Authenticated) {
            checkPoint();
        } else if (state instanceof SessionUIState.Unauthenticated) {
            NavigationUtil.navigateTo(this, Login.class);
        } else if (state instanceof SessionUIState.Error) {
            binding.tvRetryMessage.setVisibility(View.VISIBLE);
            binding.btnRetry.setVisibility(View.VISIBLE);
        }
    }

    private void checkPoint() {
        JsonObject metadata = authViewModel.getCachedUser().getPublicMetadata();
        if (metadata == null || !metadata.containsKey("empId")) {
            forceLogout();
            NavigationUtil.navigateTo(this, Login.class);
            return;
        }

        JsonElement element = metadata.get("empId");
        empId = element.toString().replace("\"", "");
        if (empId.isEmpty()) {
            forceLogout();
            NavigationUtil.navigateTo(this, Login.class);
            return;
        }
        prefs.edit().putString("EMP_ID", empId).apply();

        userViewModel.loadEmployee(empId, FetchStrategy.NETWORK_ONLY);
        userViewModel.getEmployee().observe(this, state -> {
            binding.progressBarLoading.setVisibility(View.GONE);

            switch (state.getStatus()) {
                case LOADING:
                    binding.progressBarLoading.setVisibility(View.VISIBLE);
                    binding.handworksLogo.setVisibility(View.VISIBLE);
                    binding.errorUI.getRoot().setVisibility(View.GONE);
                    break;

                case SUCCESS:
                    Employee employee = state.getData();
                    if (employee != null && "employee".equals(employee.getAccount().getRole())) {
                        Toast.makeText(AppEntryScreenSplash.this, "Welcome back!", Toast.LENGTH_LONG).show();
                        NavigationUtil.navigateTo(AppEntryScreenSplash.this, Dashboard.class);
                    } else {
                        Toast.makeText(AppEntryScreenSplash.this, "Unauthorized account", Toast.LENGTH_LONG).show();
                        forceLogout();
                        NavigationUtil.navigateTo(AppEntryScreenSplash.this, Login.class);
                    }
                    break;

                case ERROR:
                    String errorMessage = state.getMessage() != null ? state.getMessage() : "Unknown error";
                    Toast.makeText(this, errorMessage + ": Taking longer than usual.", Toast.LENGTH_LONG).show();

                    binding.progressBarLoading.setVisibility(View.GONE);
                    if ("timeout".equalsIgnoreCase(errorMessage)) {
                        if (retryCount < MAX_RETRIES) {
                            retryCount++;
                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                binding.progressBarLoading.setVisibility(View.VISIBLE);
                                userViewModel.loadEmployee(empId, FetchStrategy.NETWORK_ONLY);
                            }, 1000);
                        } else {
                            showRetryUI();
                        }
                    } else {
                        binding.handworksLogo.setVisibility(View.GONE);
                        binding.errorUI.getRoot().setVisibility(View.VISIBLE);
                        binding.errorUI.errorBtntnRetry.setOnClickListener(v -> {
                            retryCount = 0;
                            showRetryUI();
                        });
                    }
                    break;
            }
        });
    }

    private void showRetryUI() {
        binding.handworksLogo.setVisibility(View.VISIBLE);
        binding.progressBarLoading.setVisibility(View.VISIBLE);
        binding.tvRetryMessage.setVisibility(View.GONE);
        binding.btnRetry.setVisibility(View.GONE);
        userViewModel.loadEmployee(empId, FetchStrategy.NETWORK_ONLY);
    }

    private void forceLogout() {
        authViewModel.clearCache();
        NavigationUtil.navigateTo(this, Login.class);
    }
}