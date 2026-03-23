package handworks_cleaning_service.handworks_mobile.ui.pages.index;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.clerk.api.user.User;

import dagger.hilt.android.AndroidEntryPoint;
import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.databinding.ActivityDashboardBinding;
import handworks_cleaning_service.handworks_mobile.ui.fragments.ChatFragment;
import handworks_cleaning_service.handworks_mobile.ui.fragments.HistoryFragment;
import handworks_cleaning_service.handworks_mobile.ui.fragments.HomeFragment;
import handworks_cleaning_service.handworks_mobile.ui.fragments.NotificationFragment;
import handworks_cleaning_service.handworks_mobile.ui.fragments.WeekViewFragment;
import handworks_cleaning_service.handworks_mobile.ui.pages.user.UserProfile;
import handworks_cleaning_service.handworks_mobile.ui.viewmodel.AuthViewModel;
import handworks_cleaning_service.handworks_mobile.utils.NavigationUtil;

@AndroidEntryPoint
public class Dashboard extends AppCompatActivity {
    private long lastClickTime = 0;
    private Fragment currentFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        EdgeToEdge.enable(this);

        ActivityDashboardBinding binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            currentFragment = new HomeFragment();
            replaceFrameFragment(currentFragment);
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomNavigationView, (v, insets) -> {
            int bottomInset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;
            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), bottomInset);
            return insets;
        });

        AuthViewModel authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        User cachedUser = authViewModel.getCachedUser();
        if (cachedUser != null) {
            String userPfpUrl = cachedUser.getImageUrl();
            Glide.with(this)
                    .load(userPfpUrl)
                    .placeholder(R.drawable.pfp_placeholder)
                    .circleCrop()
                    .into(binding.header.userPfp);
        }

        binding.header.userPfp.setOnClickListener(v -> NavigationUtil.navigateNoFinishTo(this, UserProfile.class));

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (binding.bottomNavigationView.getVisibility() != View.VISIBLE) return false;
            return handleNavigation(item.getItemId());
        });

        binding.navigationRail.setOnItemSelectedListener(item -> {
            if (binding.navigationRail.getVisibility() != View.VISIBLE) return false;
            return handleNavigation(item.getItemId());
        });
    }

    private boolean handleNavigation(int id) {
        long now = System.currentTimeMillis();
        if (now - lastClickTime < 500) return false;
        lastClickTime = now;

        Fragment fragment = null;

        if (id == R.id.homeIcon) {
            fragment = new HomeFragment();
        } else if (id == R.id.calendarIcon) {
            fragment = new WeekViewFragment();
        } else if (id == R.id.historyIcon) {
            fragment = new HistoryFragment();
        } else if (id == R.id.chatIcon) {
            fragment = new ChatFragment();
        } else if (id == R.id.notificationIcon) {
            fragment = new NotificationFragment();
        }

        if (fragment != null && currentFragment != null && currentFragment.getClass().equals(fragment.getClass())) {
            return false;
        }

        if (fragment != null) {
            replaceFrameFragment(fragment);
            currentFragment = fragment;
        }
        return true;
    }

    private void replaceFrameFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}