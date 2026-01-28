package handworks_cleaning_service.handworks_mobile.ui.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.utils.NavigationUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View noNotifYet;
    private View notifIsOff;
    private Button btnLater;
    private Button btnGetNotified;

    public NotificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        SharedPreferences prefs = view.getContext()
                .getSharedPreferences("PREFS_NAME", Context.MODE_PRIVATE);
        boolean isOn = prefs.getBoolean("Notification_Toggle", false);

        notifIsOff = view.findViewById(R.id.notifIsOff);
        btnLater = view.findViewById(R.id.btnLater);
        btnGetNotified = view.findViewById(R.id.btnGetNotified);

        notifIsOff.setVisibility(isOn ? View.GONE : View.VISIBLE);

        noNotifYet = view.findViewById(R.id.noNotifYet);
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