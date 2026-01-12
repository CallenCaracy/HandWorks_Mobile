package handworks_cleaning_service.handworks_mobile.ui.pages.user;

import static java.security.AccessController.getContext;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.ui.adapters.ProfileSettingsAdapter;
import handworks_cleaning_service.handworks_mobile.ui.models.ProfileItem;
import handworks_cleaning_service.handworks_mobile.ui.pages.auth.Login;
import handworks_cleaning_service.handworks_mobile.ui.viewmodel.AuthViewModel;
import handworks_cleaning_service.handworks_mobile.utils.Constant;
import handworks_cleaning_service.handworks_mobile.utils.NavigationUtil;
import handworks_cleaning_service.handworks_mobile.utils.ThemeUtil;
import handworks_cleaning_service.handworks_mobile.utils.uistate.AuthUiState;

public class UserProfile extends AppCompatActivity {
    private AuthViewModel authViewModel;
    private Button signOut;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView back = findViewById(R.id.btnExitProfile);
        TextView cleanerFName = findViewById(R.id.cleanerFirstNameDisplay);
        TextView cleanerLName = findViewById(R.id.cleanerLastNameDisplay);
        signOut = findViewById(R.id.btnLogout);

        recyclerView = findViewById(R.id.profileRecycler);
        setUpRecyclerView();

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        signOutUser();

        cleanerFName.setText("John");
        cleanerLName.setText("Doe");

        back.setOnClickListener(v -> finish());
    }

    private void signOutUser() {
        signOut.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> authViewModel.signOut())
                .setNegativeButton("No", null)
                .show());

        authViewModel.getAuthState().observe((LifecycleOwner) getLifecycle(), state -> {
            if (state instanceof AuthUiState.SignedOut) {
                NavigationUtil.navigateTo(this, Login.class);
            } else if (state instanceof AuthUiState.Error) {
                Toast.makeText(this, ((AuthUiState.Error) state).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<ProfileItem> items = new ArrayList<>();

        items.add(new ProfileItem(Constant.TYPE_HEADER, "Profile", 0));
        items.add(new ProfileItem(Constant.TYPE_ITEM, "Manage user", R.drawable.target_svgrepo_com));

        items.add(new ProfileItem(Constant.TYPE_HEADER, "Settings", 0));
        items.add(new ProfileItem(Constant.TYPE_ITEM, "Notifications", R.drawable.bell_svgrepo_com));
        items.add(new ProfileItem(Constant.TYPE_ITEM, "Dark Mode", R.drawable.moon_fog_svgrepo_com));
        items.add(new ProfileItem(Constant.TYPE_ITEM, "System Mode", R.drawable.system_settings_backup_svgrepo_com));
        items.add(new ProfileItem(Constant.TYPE_ITEM, "Light Mode", R.drawable.sun_fog_svgrepo_com));

        ProfileSettingsAdapter adapter = new ProfileSettingsAdapter(items, item -> {
            switch (item.title) {
                case "Manage user":
                    // open manage user
                    break;
                case "Notifications":
                    // open notifications
                    break;
                case "Dark Mode":
                    ThemeUtil.setTheme(this, Constant.THEME_DARK);
                    Toast.makeText(this, "Dark Mode activated", Toast.LENGTH_SHORT).show();
                    break;

                case "Light Mode":
                    ThemeUtil.setTheme(this, Constant.THEME_LIGHT);
                    Toast.makeText(this, "Light Mode activated", Toast.LENGTH_SHORT).show();
                    break;

                case "System Mode":
                    ThemeUtil.setTheme(this, Constant.THEME_SYSTEM);
                    Toast.makeText(this, "System Mode activated", Toast.LENGTH_SHORT).show();
                    break;
            }
        });

        recyclerView.setAdapter(adapter);
    }
}