package handworks_cleaning_service.handworks_mobile.ui.pages.user;

import static handworks_cleaning_service.handworks_mobile.utils.Constant.PREFS_NAME;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.clerk.api.Clerk;
import com.clerk.api.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;
import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.ui.adapters.ProfileSettingsAdapter;
import handworks_cleaning_service.handworks_mobile.ui.models.ProfileItem;
import handworks_cleaning_service.handworks_mobile.ui.models.ThemeOption;
import handworks_cleaning_service.handworks_mobile.ui.pages.auth.Login;
import handworks_cleaning_service.handworks_mobile.ui.pages.index.FullscreenImageView;
import handworks_cleaning_service.handworks_mobile.ui.viewmodel.AuthViewModel;
import handworks_cleaning_service.handworks_mobile.utils.Constant;
import handworks_cleaning_service.handworks_mobile.utils.DateUtil;
import handworks_cleaning_service.handworks_mobile.utils.NavigationUtil;
import handworks_cleaning_service.handworks_mobile.utils.ThemeUtil;
import handworks_cleaning_service.handworks_mobile.utils.uistate.AuthUiState;

@AndroidEntryPoint
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
        ImageView userPfp = findViewById(R.id.userPfp);
        TextView cleanerFName = findViewById(R.id.cleanerFirstNameDisplay);
        TextView cleanerLName = findViewById(R.id.cleanerLastNameDisplay);
        TextView joinedAt = findViewById(R.id.joinedValue);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        TextView ratingValue = findViewById(R.id.ratingValue);
        signOut = findViewById(R.id.btnLogout);

        recyclerView = findViewById(R.id.profileRecycler);
        setUpRecyclerView();

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        signOutUser();

        User cachedUser = authViewModel.getCachedUser();
        if (cachedUser != null && cachedUser.getCreatedAt() != null) {
            cleanerFName.setText(cachedUser.getFirstName());
            cleanerLName.setText(cachedUser.getLastName());

            Glide.with(this)
                    .load(cachedUser.getImageUrl())
                    .placeholder(R.drawable.pfp_placeholder)
                    .error(R.drawable.pfp_placeholder)
                    .circleCrop()
                    .into(userPfp);

            long createdAt = cachedUser.getCreatedAt();
            joinedAt.setText(DateUtil.getTimeAgo(createdAt));


            userPfp.setOnClickListener(v -> {
                Intent intent = new Intent(this, FullscreenImageView.class);
                intent.putExtra("image_url", cachedUser.getImageUrl());
                startActivity(intent);
            });
        }

        ratingBar.setRating(4.5f);
        ratingValue.setText("4.5");

        back.setOnClickListener(v -> finish());
    }

    private void signOutUser() {
        signOut.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> authViewModel.signOut())
                .setNegativeButton("No", null)
                .show());

        authViewModel.getAuthState().observe(this, state -> {
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
        items.add(new ProfileItem(Constant.TYPE_SWITCH, "Notifications", R.drawable.bell_svgrepo_com));
        items.add(new ProfileItem(Constant.TYPE_ITEM, "Theme", R.drawable.theme));

        ProfileSettingsAdapter adapter = new ProfileSettingsAdapter(items, item -> {
            switch (item.title) {
                case "Manage user":
                    // open manage user
                    break;
                case "Notifications":
                    SharedPreferences prefs = this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                    SwitchCompat switchView = findViewById(R.id.switchView);

                    switchView.setChecked(prefs.getBoolean("Notification_Toggle", false));

                    switchView.setOnCheckedChangeListener((btn, checked) -> prefs.edit().putBoolean("Notification_Toggle", checked).apply());
                    break;
                case "Theme":
                    showThemeModal();
                    break;
            }
        });

        recyclerView.setAdapter(adapter);
    }

    private void showThemeModal() {
        ThemeOption[] themes = {
                new ThemeOption("System", R.drawable.system_settings_backup_svgrepo_com),
                new ThemeOption("Light", R.drawable.sun_fog_svgrepo_com),
                new ThemeOption("Dark", R.drawable.moon_fog_svgrepo_com)
        };

        int currentTheme = ThemeUtil.getTheme(this);
        int checkedItem = switch (currentTheme) {
            case Constant.THEME_LIGHT -> 1;
            case Constant.THEME_DARK -> 2;
            default -> 0;
        };

        ArrayAdapter<ThemeOption> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_singlechoice, themes) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tab_dialog, parent, false);
                }

                ThemeOption theme = Objects.requireNonNull(getItem(position), "ThemeOption cannot be null");
                TextView text = convertView.findViewById(R.id.text);
                ImageView icon = convertView.findViewById(R.id.icon);

                text.setText(theme.name);
                icon.setImageResource(theme.iconRes);

                if (ThemeUtil.themeMatchesCurrent(theme, currentTheme)) {
                    text.setTypeface(null, Typeface.BOLD);
                    text.setTextColor(getContext().getColor(R.color.colorTertiary));
                } else {
                    text.setTypeface(null, Typeface.NORMAL);
                    text.setTextColor(getContext().getColor(R.color.textPrimary));
                }

                return convertView;
            }
        };

        new AlertDialog.Builder(this)
                .setTitle("Select Theme")
                .setSingleChoiceItems(adapter, checkedItem, (dialog, which) -> {
                    switch (which) {
                        case 0 -> ThemeUtil.setTheme(this, Constant.THEME_SYSTEM);
                        case 1 -> ThemeUtil.setTheme(this, Constant.THEME_LIGHT);
                        case 2 -> ThemeUtil.setTheme(this, Constant.THEME_DARK);
                    }
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}