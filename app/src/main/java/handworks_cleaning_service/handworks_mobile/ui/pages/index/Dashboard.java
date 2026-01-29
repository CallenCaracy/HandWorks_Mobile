package handworks_cleaning_service.handworks_mobile.ui.pages.index;

import android.os.Bundle;
import android.widget.ImageView;

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
import com.google.android.material.bottomnavigation.BottomNavigationView;

import dagger.hilt.android.AndroidEntryPoint;
import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.databinding.ActivityDashboardBinding;
import handworks_cleaning_service.handworks_mobile.ui.fragments.ChatFragment;
import handworks_cleaning_service.handworks_mobile.ui.fragments.HistoryFragment;
import handworks_cleaning_service.handworks_mobile.ui.fragments.HomeFragment;
import handworks_cleaning_service.handworks_mobile.ui.fragments.NotificationFragment;
import handworks_cleaning_service.handworks_mobile.ui.pages.user.UserProfile;
import handworks_cleaning_service.handworks_mobile.ui.viewmodel.AuthViewModel;
import handworks_cleaning_service.handworks_mobile.utils.NavigationUtil;

@AndroidEntryPoint
public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        EdgeToEdge.enable(this);
        ActivityDashboardBinding binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        if (savedInstanceState == null) {
            replaceFrameFragment(new HomeFragment());
        }

        ViewCompat.setOnApplyWindowInsetsListener(bottomNav, (v, insets) -> {
            int bottomInset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;
            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), bottomInset);
            return insets;
        });

        AuthViewModel authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        ImageView userPfp = findViewById(R.id.userPfp);

        User cachedUser = authViewModel.getCachedUser();
        if (cachedUser != null) {
            String userPfpUrl = cachedUser.getImageUrl();
            Glide.with(this)
                    .load(userPfpUrl)
                    .placeholder(R.drawable.pfp_placeholder)
                    .circleCrop()
                    .into(userPfp);
        }

        userPfp.setOnClickListener(v -> NavigationUtil.navigateNoFinishTo(this, UserProfile.class));

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