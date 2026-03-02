package handworks_cleaning_service.handworks_mobile.ui.pages.index;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.databinding.ActivityLearnMoreBinding;
import handworks_cleaning_service.handworks_mobile.utils.NavigationUtil;

@AndroidEntryPoint
public class LearnMore extends AppCompatActivity {
    @Inject
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityLearnMoreBinding binding = ActivityLearnMoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdge.enable(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.btnGoToLogIn.setOnClickListener(v -> {
            prefs.edit().putBoolean("landing_seen", true).apply();
            NavigationUtil.navigateTo(this, AppEntryScreenSplash.class);
        });
        binding.btnGoBackToLanding.setOnClickListener(v -> NavigationUtil.navigateTo(this, LandingPage.class));
    }
}