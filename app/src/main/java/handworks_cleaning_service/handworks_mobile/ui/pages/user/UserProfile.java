package handworks_cleaning_service.handworks_mobile.ui.pages.user;

import static android.view.View.GONE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.clerk.api.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.databinding.ActivityUserProfileBinding;
import handworks_cleaning_service.handworks_mobile.ui.adapters.ProfileSettingsAdapter;
import handworks_cleaning_service.handworks_mobile.ui.models.ProfileItem;
import handworks_cleaning_service.handworks_mobile.ui.models.ThemeOption;
import handworks_cleaning_service.handworks_mobile.ui.pages.auth.Login;
import handworks_cleaning_service.handworks_mobile.ui.pages.index.FullscreenImageView;
import handworks_cleaning_service.handworks_mobile.ui.viewmodel.AuthViewModel;
import handworks_cleaning_service.handworks_mobile.ui.viewmodel.UserViewModel;
import handworks_cleaning_service.handworks_mobile.utils.Constant;
import handworks_cleaning_service.handworks_mobile.utils.DateUtil;
import handworks_cleaning_service.handworks_mobile.utils.NavigationUtil;
import handworks_cleaning_service.handworks_mobile.utils.ThemeUtil;
import handworks_cleaning_service.handworks_mobile.utils.uistate.AuthUiState;

@AndroidEntryPoint
public class UserProfile extends AppCompatActivity {
    @Inject
    SharedPreferences prefs;
    private AuthViewModel authViewModel;
    private ActivityUserProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdge.enable(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setUpRecyclerView();

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        signOutUserConfirmation();

        User cachedUser = authViewModel.getCachedUser();
        if (cachedUser != null && cachedUser.getCreatedAt() != null) {
            binding.cleanerFirstNameDisplay.setText(cachedUser.getFirstName());
            binding.cleanerLastNameDisplay.setText(cachedUser.getLastName());

            Glide.with(this)
                    .load(cachedUser.getImageUrl())
                    .placeholder(R.drawable.pfp_placeholder)
                    .error(R.drawable.pfp_placeholder)
                    .circleCrop()
                    .into(binding.userPfp);

            binding.userPfp.setOnClickListener(v -> {
                Intent intent = new Intent(this, FullscreenImageView.class);
                intent.putExtra("image_url", cachedUser.getImageUrl());
                startActivity(intent);
            });

            userViewModel.getEmployee().observe(this, employee -> {
                if (employee != null) {
                    String formattedDate = DateUtil.formatStringDate(employee.getHire_date());
                    long longDate = DateUtil.convertStringToLong(employee.getHire_date());

                    binding.joinedValue.setText(DateUtil.getTimeAgo(longDate));
                    binding.ratingBar.setRating((float) employee.getPerformance_score());
                    binding.ratingValue.setText(String.valueOf((int) employee.getPerformance_score()));
                    binding.ratingNumber.setText(
                            getResources().getQuantityString(
                                    R.plurals.reviews_count,
                                    employee.getNum_ratings(),
                                    employee.getNum_ratings()
                            )
                    );
                    binding.employeeEmailValue.setText(employee.getAccount().getEmail());
                    binding.employeeHireDateValue.setText(formattedDate);
                    binding.employeePositionValue.setText(employee.getPosition());
                    binding.employeeStatus.setText(
                            getString(R.string.status_colon, employee.getStatus())
                    );
                }
            });

            userViewModel.getError().observe(this, error -> {
                Toast.makeText(this, "Error fetching user info.", Toast.LENGTH_LONG).show();
                binding.ratingBar.setRating(0.0f);
                binding.joinedValue.setText(R.string._0_months_ago);
                binding.ratingValue.setText("0.0");
                binding.ratingNumber.setVisibility(GONE);
                binding.employeeEmailValue.setText(R.string.error);
                binding.employeeHireDateValue.setText(R.string.error);
                binding.employeePositionValue.setText(R.string.error);
                binding.employeeStatus.setText(R.string.error);
            });
            userViewModel.loadEmployee(cachedUser.getId());
        }

        binding.btnExitProfile.setOnClickListener(v -> finish());
    }

    private void signOutUserConfirmation() {
        binding.btnLogout.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> logoutCompletely())
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
        binding.profileRecycler.setLayoutManager(new LinearLayoutManager(this));

        List<ProfileItem> items = new ArrayList<>();

        items.add(new ProfileItem(Constant.TYPE_HEADER, "Settings", 0));
        items.add(new ProfileItem(Constant.TYPE_SWITCH, "Notifications", R.drawable.bell_svgrepo_com));
        items.add(new ProfileItem(Constant.TYPE_ITEM, "Theme", R.drawable.theme));

        ProfileSettingsAdapter adapter = new ProfileSettingsAdapter(items, item -> {
            switch (item.title) {
                case "Notifications":
                    SwitchCompat switchView = findViewById(R.id.switchView);

                    switchView.setChecked(prefs.getBoolean("Notification_Toggle", false));
                    switchView.setOnCheckedChangeListener((btn, checked) -> prefs.edit().putBoolean("Notification_Toggle", checked).apply());
                    break;
                case "Theme":
                    showThemeModal();
                    break;
            }
        });

        binding.profileRecycler.setAdapter(adapter);
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
                TextView textAndIcon = convertView.findViewById(R.id.textAndIcon);

                textAndIcon.setText(theme.name);

                Context context = getContext();
                Drawable icon = AppCompatResources.getDrawable(context, theme.iconRes);
                if (icon != null) {
                    float scale = context.getResources().getDisplayMetrics().density;
                    int sizeInPx = (int) (24 * scale + 0.5f);
                    icon.setBounds(0, 0, sizeInPx, sizeInPx);

                    textAndIcon.setCompoundDrawables(icon, null, null, null);
                    textAndIcon.setCompoundDrawablePadding(12);
                }

                if (ThemeUtil.themeMatchesCurrent(theme, currentTheme)) {
                    textAndIcon.setTypeface(null, Typeface.BOLD);
                    textAndIcon.setTextColor(getContext().getColor(R.color.colorTertiary));
                } else {
                    textAndIcon.setTypeface(null, Typeface.NORMAL);
                    textAndIcon.setTextColor(getContext().getColor(R.color.textPrimary));
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

    private void logoutCompletely() {
        authViewModel.signOut();
        authViewModel.clearCache();
    }
}