package handworks_cleaning_service.handworks_mobile.ui.pages.index;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.ui.pages.auth.Login;
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

        getStarted = findViewById(R.id.btnGoToLogIn);
        backToLanding = findViewById(R.id.btnGoBackToLanding);

        getStarted.setOnClickListener(v -> NavigationUtil.navigateTo(this, Login.class));
        backToLanding.setOnClickListener(v -> NavigationUtil.navigateTo(this, LandingPage.class));
    }
}