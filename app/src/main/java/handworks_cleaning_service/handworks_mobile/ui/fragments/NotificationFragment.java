package handworks_cleaning_service.handworks_mobile.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import handworks_cleaning_service.handworks_mobile.R;

public class NotificationFragment extends Fragment {
    private View notifIsOff;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        SharedPreferences prefs = view.getContext()
                .getSharedPreferences("PREFS_NAME", Context.MODE_PRIVATE);
        boolean isOn = prefs.getBoolean("Notification_Toggle", false);

        notifIsOff = view.findViewById(R.id.notifIsOff);
        Button btnLater = view.findViewById(R.id.btnLater);
        Button btnGetNotified = view.findViewById(R.id.btnGetNotified);

        notifIsOff.setVisibility(isOn ? View.GONE : View.VISIBLE);

        View noNotifYet = view.findViewById(R.id.noNotifYet);
        int notifCount = 0;

        noNotifYet.setVisibility((notifCount == 0 || !isOn) ? View.VISIBLE : View.GONE);

        btnLater.setOnClickListener(v -> notifIsOff.setVisibility(View.GONE));
        btnGetNotified.setOnClickListener(v -> {
            prefs.edit().putBoolean("Notification_Toggle", true).apply();
            notifIsOff.setVisibility(View.GONE);
        });

        return view;
    }
}