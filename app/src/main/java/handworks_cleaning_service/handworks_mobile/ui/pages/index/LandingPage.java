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
import androidx.viewpager2.widget.ViewPager2;

import java.util.Arrays;
import java.util.List;

import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.ui.adapters.SliderAdapter;
import handworks_cleaning_service.handworks_mobile.utils.NavigationUtil;

public class LandingPage extends AppCompatActivity {
    private ViewPager2 viewPager;
    private int currentPage = 0;
    private final long slideDelay = 3000;

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

        viewPager = findViewById(R.id.viewPager);

        List<Integer> sliderImages = Arrays.asList(
                R.drawable.group_photo,
                R.drawable.group_photo_2,
                R.drawable.group_photo_3
        );

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean hasSeenLanding = prefs.getBoolean("landing_seen", false);

        if (hasSeenLanding) {
            NavigationUtil.navigateTo(this, AppEntryScreenSplash.class);
            return;
        }

        SliderAdapter adapter = new SliderAdapter(sliderImages);
        viewPager.setAdapter(adapter);

        viewPager.postDelayed(slideRunnable, slideDelay);

        Button getStarted = findViewById(R.id.btnGetStarted);
        Button learnMore = findViewById(R.id.btnLearnMore);

        getStarted.setOnClickListener(v -> {
            prefs.edit().putBoolean("landing_seen", true).apply();
            NavigationUtil.navigateTo(this, AppEntryScreenSplash.class);
        });
        learnMore.setOnClickListener(v -> {
            prefs.edit().putBoolean("landing_seen", true).apply();
            NavigationUtil.navigateTo(this, LearnMore.class);
        });
    }
    final Runnable slideRunnable = new Runnable() {
        @Override
        public void run() {
            if (viewPager != null && viewPager.getAdapter() != null) {
                int pageCount = viewPager.getAdapter().getItemCount();
                currentPage = (currentPage + 1) % pageCount;
                viewPager.setCurrentItem(currentPage, true);
                viewPager.postDelayed(this, slideDelay);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewPager.removeCallbacks(slideRunnable);
    }
}
