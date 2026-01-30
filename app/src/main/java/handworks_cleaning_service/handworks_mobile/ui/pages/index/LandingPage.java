package handworks_cleaning_service.handworks_mobile.ui.pages.index;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.databinding.ActivityLandingPageBinding;
import handworks_cleaning_service.handworks_mobile.ui.adapters.SliderAdapter;
import handworks_cleaning_service.handworks_mobile.utils.NavigationUtil;

@AndroidEntryPoint
public class LandingPage extends AppCompatActivity {
    @Inject
    SharedPreferences prefs;
    private ActivityLandingPageBinding binding;
    private int currentPage = 0;
    private final long slideDelay = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLandingPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdge.enable(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        List<Integer> sliderImages = Arrays.asList(
                R.drawable.group_photo,
                R.drawable.group_photo_2,
                R.drawable.group_photo_3
        );

        boolean hasSeenLanding = prefs.getBoolean("landing_seen", false);

        if (hasSeenLanding) {
            NavigationUtil.navigateTo(this, AppEntryScreenSplash.class);
            return;
        }

        SliderAdapter adapter = new SliderAdapter(sliderImages);
        binding.viewPager.setAdapter(adapter);

        binding.viewPager.postDelayed(slideRunnable, slideDelay);

        binding.btnGetStarted.setOnClickListener(v -> {
            prefs.edit().putBoolean("landing_seen", true).apply();
            NavigationUtil.navigateTo(this, AppEntryScreenSplash.class);
        });
        binding.btnLearnMore.setOnClickListener(v -> NavigationUtil.navigateTo(this, LearnMore.class));
    }
    final Runnable slideRunnable = new Runnable() {
        @Override
        public void run() {
            if (binding.viewPager.getAdapter() != null) {
                int pageCount = binding.viewPager.getAdapter().getItemCount();
                currentPage = (currentPage + 1) % pageCount;
                binding.viewPager.setCurrentItem(currentPage, true);
                binding.viewPager.postDelayed(this, slideDelay);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.viewPager.removeCallbacks(slideRunnable);
    }
}
