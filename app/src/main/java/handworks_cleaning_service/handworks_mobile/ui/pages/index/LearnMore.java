package handworks_cleaning_service.handworks_mobile.ui.pages.index;

import static handworks_cleaning_service.handworks_mobile.utils.Constant.PREFS_NAME;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.utils.NavigationUtil;

public class LearnMore extends AppCompatActivity {
    public Button getStarted, backToLanding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_learn_more);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        initWidgets();

        getStarted.setOnClickListener(v -> {
            prefs.edit().putBoolean("landing_seen", true).apply();
            NavigationUtil.navigateTo(this, AppEntryScreenSplash.class);
        });
        backToLanding.setOnClickListener(v -> NavigationUtil.navigateTo(this, LandingPage.class));
    }

    private void initWidgets() {
        getStarted = findViewById(R.id.btnGoToLogIn);
        backToLanding = findViewById(R.id.btnGoBackToLanding);
    }
}