package handworks_cleaning_service.handworks_mobile.ui.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import handworks_cleaning_service.handworks_mobile.databinding.FragmentNotificationBinding;

@AndroidEntryPoint
public class NotificationFragment extends Fragment {
    private FragmentNotificationBinding binding;
    @Inject
    SharedPreferences prefs;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotificationBinding.inflate(inflater, container, false);

        boolean isOn = prefs.getBoolean("Notification_Toggle", false);

        int notifCount = 0;

        binding.notifIsOff.getRoot().setVisibility(isOn ? View.GONE : View.VISIBLE);

        binding.noNotifYet.getRoot().setVisibility((notifCount == 0 || !isOn) ? View.VISIBLE : View.GONE);

        binding.notifIsOff.btnLater.setOnClickListener(v -> binding.notifIsOff.getRoot().setVisibility(View.GONE));
        binding.notifIsOff.btnGetNotified.setOnClickListener(v -> {
            prefs.edit().putBoolean("Notification_Toggle", true).apply();
            binding.notifIsOff.getRoot().setVisibility(View.GONE);
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}