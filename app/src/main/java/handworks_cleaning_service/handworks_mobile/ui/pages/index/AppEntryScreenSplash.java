package handworks_cleaning_service.handworks_mobile.ui.pages.index;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.clerk.api.Clerk;
import com.google.android.material.snackbar.Snackbar;

import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.ui.pages.auth.Login;
import handworks_cleaning_service.handworks_mobile.utils.NavigationUtil;

public class AppEntryScreenSplash extends AppCompatActivity {

    private ProgressBar progressBar;

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

        progressBar = findViewById(R.id.progressBarLoading);
        progressBar.setVisibility(View.VISIBLE);

        checkClerkSessionWithRetry();
    }

    private void checkClerkSessionWithRetry() {
        final int maxRetries = 3;
        final int retryDelay = 1000;
        checkSession(0, maxRetries, retryDelay);
    }

    private void checkSession(int attempt, int maxRetries, int retryDelay) {
        if (!Boolean.TRUE.equals(Clerk.INSTANCE.isInitialized().getValue())) {
            if (attempt < maxRetries) {
                findViewById(R.id.main).postDelayed(() ->
                                checkSession(attempt + 1, maxRetries, retryDelay),
                        retryDelay
                );
            } else {
                progressBar.setVisibility(View.GONE);
                showRetryUI();
            }
            return;
        }

        progressBar.setVisibility(View.GONE);

        if (Clerk.INSTANCE.isSignedIn() && Clerk.INSTANCE.getSession() != null) {
            Log.d("ClerkSessionToken", Clerk.INSTANCE.getSession().toString());
            NavigationUtil.navigateTo(this, Dashboard.class);
        } else {
            NavigationUtil.navigateTo(this, Login.class);
        }
    }

    private void showRetryUI() {
        progressBar.setVisibility(View.GONE);

        Snackbar.make(
                findViewById(R.id.main),
                "Still checking session. Check your internet.",
                Snackbar.LENGTH_INDEFINITE
        ).setAction("Retry", v -> {
            progressBar.setVisibility(View.VISIBLE);
            checkClerkSessionWithRetry();
        }).show();
    }
}