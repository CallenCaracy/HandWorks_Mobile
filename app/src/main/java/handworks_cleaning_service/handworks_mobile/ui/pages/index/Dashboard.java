package handworks_cleaning_service.handworks_mobile.ui.pages.index;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.databinding.ActivityDashboardBinding;
import handworks_cleaning_service.handworks_mobile.ui.fragments.ChatFragment;
import handworks_cleaning_service.handworks_mobile.ui.fragments.HistoryFragment;
import handworks_cleaning_service.handworks_mobile.ui.fragments.HomeFragment;
import handworks_cleaning_service.handworks_mobile.ui.fragments.NotificationFragment;
import handworks_cleaning_service.handworks_mobile.ui.fragments.SettingsFragment;

public class Dashboard extends AppCompatActivity {
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        EdgeToEdge.enable(this);
        ActivityDashboardBinding binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bottomNav = findViewById(R.id.bottomNavigationView);
        if (savedInstanceState == null) {
            replaceFrameFragment(new HomeFragment());
        }

        ViewCompat.setOnApplyWindowInsetsListener(bottomNav, (v, insets) -> {
            int bottomInset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;
            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), bottomInset);
            return insets;
        });

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.homeIcon) {
                replaceFrameFragment(new HomeFragment());
            } else if (id == R.id.historyIcon) {
                replaceFrameFragment(new HistoryFragment());
            } else if (id == R.id.chatIcon) {
                replaceFrameFragment(new ChatFragment());
            } else if (id == R.id.notificationIcon) {
                replaceFrameFragment(new NotificationFragment());
            } else if (id == R.id.settingsIcon) {
                replaceFrameFragment(new SettingsFragment());
            }
            return true;
        });
    }

    private void replaceFrameFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}