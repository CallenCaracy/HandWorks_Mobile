package handworks_cleaning_service.handworks_mobile.ui.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import dagger.hilt.android.AndroidEntryPoint;
import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.ui.pages.auth.Login;
import handworks_cleaning_service.handworks_mobile.ui.viewmodel.AuthViewModel;
import handworks_cleaning_service.handworks_mobile.utils.uistate.AuthUiState;
import handworks_cleaning_service.handworks_mobile.utils.NavigationUtil;
import handworks_cleaning_service.handworks_mobile.utils.ThemeUtil;
import static handworks_cleaning_service.handworks_mobile.utils.Constant.THEME_DARK;
import static handworks_cleaning_service.handworks_mobile.utils.Constant.THEME_LIGHT;
import static handworks_cleaning_service.handworks_mobile.utils.Constant.THEME_SYSTEM;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
public class SettingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private AuthViewModel authViewModel;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Button systemMode = view.findViewById(R.id.systemBtn);
        Button dayMode = view.findViewById(R.id.dayBtn);
        Button nightMode = view.findViewById(R.id.nightBtn);
        Button logoutBtn = view.findViewById(R.id.btnLogout);

        int activeTheme = ThemeUtil.getTheme(requireContext());
        updateThemeButtons(activeTheme, systemMode, dayMode, nightMode);

        systemMode.setOnClickListener(v -> {
            ThemeUtil.setTheme(requireContext(), THEME_SYSTEM);
            updateThemeButtons(THEME_SYSTEM, systemMode, dayMode, nightMode);
        });

        dayMode.setOnClickListener(v -> {
            ThemeUtil.setTheme(requireContext(), THEME_LIGHT);
            updateThemeButtons(THEME_LIGHT, systemMode, dayMode, nightMode);
        });

        nightMode.setOnClickListener(v -> {
            ThemeUtil.setTheme(requireContext(), THEME_DARK);
            updateThemeButtons(THEME_DARK, systemMode, dayMode, nightMode);
        });

        logoutBtn.setOnClickListener(v -> new AlertDialog.Builder(v.getContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes", (dialog, which) -> authViewModel.signOut())
            .setNegativeButton("No", null)
            .show());

        authViewModel.getAuthState().observe(getViewLifecycleOwner(), state -> {
            if (state instanceof AuthUiState.SignedOut) {
                NavigationUtil.navigateTo(requireActivity(), Login.class);
            } else if (state instanceof AuthUiState.Error) {
                Toast.makeText(getContext(), ((AuthUiState.Error) state).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void updateThemeButtons(int activeTheme, Button systemBtn, Button dayBtn, Button nightBtn) {
        resetButton(systemBtn);
        resetButton(dayBtn);
        resetButton(nightBtn);

        switch (activeTheme) {
            case THEME_LIGHT:
                highlightButton(dayBtn);
                break;

            case THEME_DARK:
                highlightButton(nightBtn);
                break;

            case THEME_SYSTEM:
            default:
                highlightButton(systemBtn);
                break;
        }
    }

    private void highlightButton(Button button) {
        button.setAlpha(1f);
        button.setEnabled(false);
    }

    private void resetButton(Button button) {
        button.setAlpha(0.6f);
        button.setEnabled(true);
    }

}