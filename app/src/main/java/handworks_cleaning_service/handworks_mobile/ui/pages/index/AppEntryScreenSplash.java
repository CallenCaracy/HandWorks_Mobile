package handworks_cleaning_service.handworks_mobile.ui.pages.index;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.data.models.users.Employee;
import handworks_cleaning_service.handworks_mobile.databinding.ActivityAppEntryScreenSplashBinding;
import handworks_cleaning_service.handworks_mobile.ui.pages.auth.Login;
import handworks_cleaning_service.handworks_mobile.ui.viewmodel.AuthViewModel;
import handworks_cleaning_service.handworks_mobile.ui.viewmodel.UserViewModel;
import handworks_cleaning_service.handworks_mobile.utils.NavigationUtil;
import handworks_cleaning_service.handworks_mobile.utils.uistate.SessionUiState;
import kotlinx.serialization.json.JsonElement;
import kotlinx.serialization.json.JsonObject;

@AndroidEntryPoint
public class AppEntryScreenSplash extends AppCompatActivity {
    private ActivityAppEntryScreenSplashBinding binding;
    private AuthViewModel authViewModel;
    private UserViewModel userViewModel;
    private String empId;
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

    private void render(SessionUiState state) {
        binding.progressBarLoading.setVisibility(View.GONE);
        binding.tvRetryMessage.setVisibility(View.GONE);
        binding.btnRetry.setVisibility(View.GONE);

        if (state instanceof SessionUiState.Loading || state instanceof SessionUiState.Idle) {
            binding.progressBarLoading.setVisibility(View.VISIBLE);
        } else if (state instanceof SessionUiState.Authenticated) {
            checkPoint();
        } else if (state instanceof SessionUiState.Unauthenticated) {
            NavigationUtil.navigateTo(this, Login.class);
        } else if (state instanceof SessionUiState.Error) {
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

        userViewModel.loadEmployee(empId);

        userViewModel.getEmployee().observe(this, new Observer<>() {
            @Override
            public void onChanged(Employee employee) {
                userViewModel.getEmployee().removeObserver(this);

                if (employee != null && "employee".equals(employee.getAccount().getRole())) {
                    Toast.makeText(AppEntryScreenSplash.this, "Welcome back!", Toast.LENGTH_LONG).show();
                    NavigationUtil.navigateTo(AppEntryScreenSplash.this, Dashboard.class);
                } else {
                    Toast.makeText(AppEntryScreenSplash.this, "Unauthorized account", Toast.LENGTH_LONG).show();
                    forceLogout();
                    NavigationUtil.navigateTo(AppEntryScreenSplash.this, Login.class);
                }
            }
        });

        userViewModel.getError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                binding.progressBarLoading.setVisibility(View.GONE);
                showRetryUI();
            }
        });
    }

    private void showRetryUI() {
        binding.progressBarLoading.setVisibility(View.VISIBLE);
        binding.tvRetryMessage.setVisibility(View.GONE);
        binding.btnRetry.setVisibility(View.GONE);
        userViewModel.loadEmployee(empId);
    }

    private void forceLogout() {
        authViewModel.logoutCompletely();
        userViewModel.clearCache();
        NavigationUtil.navigateTo(this, Login.class);
    }
}