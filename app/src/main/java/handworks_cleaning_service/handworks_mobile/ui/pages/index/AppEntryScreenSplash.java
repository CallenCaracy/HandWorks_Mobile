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
        final int retryDelay = 800;
        checkSession(0, maxRetries, retryDelay);
    }

    private void checkSession(int attempt, int maxRetries, int retryDelay) {
        if (Clerk.INSTANCE.getSession() != null) {
            Log.d("ClerkSessionToken", Clerk.INSTANCE.getSession().toString());
            progressBar.setVisibility(View.GONE);
            NavigationUtil.navigateTo(this, Dashboard.class);
        } else if (attempt < maxRetries) {
            findViewById(R.id.main).postDelayed(() ->
                    checkSession(attempt + 1, maxRetries, retryDelay), retryDelay);
        } else {
            progressBar.setVisibility(View.GONE);
            NavigationUtil.navigateTo(this, Login.class);
        }
    }
}