package handworks_cleaning_service.handworks_mobile.ui.pages.index;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import java.util.Arrays;
import java.util.List;

import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.ui.adapters.SliderAdapter;
import handworks_cleaning_service.handworks_mobile.ui.pages.auth.Login;
import handworks_cleaning_service.handworks_mobile.utils.NavigationUtil;

public class LandingPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_landing_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ViewPager2 viewPager = findViewById(R.id.viewPager);

        List<Integer> sliderImages = Arrays.asList(
                R.drawable.group_photo,
                R.drawable.group_photo_2,
                R.drawable.group_photo_3
        );

        SliderAdapter adapter = new SliderAdapter(sliderImages);
        viewPager.setAdapter(adapter);

        Button getStarted = findViewById(R.id.btnGetStarted);
        Button learnMore = findViewById(R.id.btnLearnMore);

        getStarted.setOnClickListener(v -> NavigationUtil.navigateTo(this, AppEntryScreenSplash.class));
        learnMore.setOnClickListener(v -> NavigationUtil.navigateTo(this, LearnMore.class));
    }
}
