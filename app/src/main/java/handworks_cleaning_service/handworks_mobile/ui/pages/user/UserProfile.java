package handworks_cleaning_service.handworks_mobile.ui.pages.user;

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
import android.widget.EditText;
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.data.dto.user.UpdateEmployeeRequest;
import handworks_cleaning_service.handworks_mobile.data.models.users.Employee;
import handworks_cleaning_service.handworks_mobile.data.repository.config.FetchStrategy;
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
import handworks_cleaning_service.handworks_mobile.utils.StringUtil;
import handworks_cleaning_service.handworks_mobile.utils.ThemeUtil;
import handworks_cleaning_service.handworks_mobile.utils.uistate.AuthUIState;

@AndroidEntryPoint
public class UserProfile extends AppCompatActivity {
    @Inject
    SharedPreferences prefs;
    private AuthViewModel authViewModel;
    private UserViewModel userViewModel;
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
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        signOutUserConfirmation();

        String employeeId = prefs.getString("EMP_ID", null);
        User cachedUser = authViewModel.getCachedUser();
        if (cachedUser != null) {
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

            userViewModel.loadEmployee(employeeId, FetchStrategy.CACHE_ONLY);
            userViewModel.getEmployee().observe(this, state -> {
                binding.swipeRefresh.setRefreshing(false);

                switch (state.getStatus()) {
                    case LOADING:
                        binding.joinedValue.setText("-");
                        binding.ratingBar.setRating(0);
                        binding.ratingValue.setText("-");
                        binding.ratingNumber.setText("-");
                        binding.employeeEmailValue.setText("-");
                        binding.employeeHireDateValue.setText("-");
                        binding.employeePositionValue.setText("-");
                        binding.employeeStatus.setText("-");
                        break;

                    case SUCCESS:
                        Employee employee = state.getData();
                        if (employee != null) {
                            binding.cleanerFirstNameDisplay.setText(employee.getAccount().getFirst_name());
                            binding.cleanerLastNameDisplay.setText(employee.getAccount().getLast_name());

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
                            binding.employeePositionValue.setText(StringUtil.capitalizeFirstLetter(employee.getPosition()));
                            binding.employeeStatus.setText(
                                    getString(R.string.status_colon, employee.getStatus())
                            );
                        }
                        break;

                    case ERROR:
                        Toast.makeText(this, "Error fetching user info.", Toast.LENGTH_LONG).show();
                        binding.ratingBar.setRating(0.0f);
                        binding.joinedValue.setText(R.string._0_months_ago);
                        binding.ratingValue.setText("0.0");
                        binding.ratingNumber.setText(
                                getResources().getQuantityString(
                                        R.plurals.reviews_count,
                                        0,
                                        0
                                )
                        );
                        binding.employeeEmailValue.setText(R.string.error);
                        binding.employeeHireDateValue.setText(R.string.error);
                        binding.employeePositionValue.setText(R.string.error);
                        binding.employeeStatus.setText(R.string.error);
                        break;
                }
            });
        }

        binding.profileHeader.titlePageTxt.setText(getString(R.string.profile));
        binding.profileHeader.btnExit.setOnClickListener(v -> finish());

        binding.swipeRefresh.setOnRefreshListener(() -> userViewModel.loadEmployee(employeeId, FetchStrategy.NETWORK_ONLY));
    }

    private void signOutUserConfirmation() {
        binding.btnLogout.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> logoutCompletely())
                .setNegativeButton("No", null)
                .show());

        authViewModel.getAuthState().observe(this, state -> {
            if (state instanceof AuthUIState.SignedOut) {
                NavigationUtil.navigateTo(this, Login.class);
            } else if (state instanceof AuthUIState.Error) {
                Toast.makeText(this, ((AuthUIState.Error) state).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpRecyclerView() {
        binding.profileRecycler.setLayoutManager(new LinearLayoutManager(this));

        List<ProfileItem> items = new ArrayList<>();

        items.add(new ProfileItem(Constant.TYPE_HEADER, "Settings", 0));
        items.add(new ProfileItem(Constant.TYPE_ITEM, "Update Name", R.drawable.account_icon));
        items.add(new ProfileItem(Constant.TYPE_SWITCH, "Notifications", R.drawable.bell_icon));
        items.add(new ProfileItem(Constant.TYPE_ITEM, "Theme", R.drawable.theme));

        ProfileSettingsAdapter adapter = new ProfileSettingsAdapter(items, item -> {
            switch (item.title) {
                case "Update Name":
                    showUpdateNameModal();
                    break;
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
                new ThemeOption("System", R.drawable.system_icon),
                new ThemeOption("Light", R.drawable.sun_icon),
                new ThemeOption("Dark", R.drawable.moon_icon)
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
                    int sizeInPx = (int) (30 * scale + 0.5f);
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

    private void showUpdateNameModal() {
        Employee employee = userViewModel.getEmployee().getValue().getData();
        if (employee == null){
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            return;
        }

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_update_name, null);
        EditText firstNameInput = dialogView.findViewById(R.id.firstNameInput);
        EditText lastNameInput = dialogView.findViewById(R.id.lastNameInput);

        firstNameInput.setText(employee.getAccount().getFirst_name());
        lastNameInput.setText(employee.getAccount().getLast_name());

        new MaterialAlertDialogBuilder(this)
                .setTitle("Update Name")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    String newFirst = firstNameInput.getText().toString().trim();
                    String newLast = lastNameInput.getText().toString().trim();

                    if (newFirst.isEmpty() || newLast.isEmpty()) {
                        Toast.makeText(this, "Please enter both first and last name", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (newFirst.equals(employee.getAccount().getFirst_name()) && newLast.equals(employee.getAccount().getLast_name())) {
                        Toast.makeText(this, "Please enter a new first or last name", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    UpdateEmployeeRequest request = new UpdateEmployeeRequest(
                            employee.getAccount().getEmail(),
                            employee.getId(),
                            newFirst,
                            employee.getAccount().getId(),
                            newLast
                    );
                    userViewModel.updateEmployeeInfo(employee.getId(), request);

                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void logoutCompletely() {
        authViewModel.signOut();
        authViewModel.clearCache();
    }
}