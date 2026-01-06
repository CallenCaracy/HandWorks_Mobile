package handworks_cleaning_service.handworks_mobile.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clerk.api.session.Session;

import java.util.Date;

import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.ui.viewmodel.AuthViewModel;
import handworks_cleaning_service.handworks_mobile.utils.DateUtil;
import handworks_cleaning_service.handworks_mobile.utils.uistate.SessionUiState;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private AuthViewModel authViewModel;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        Date date = new Date();

        TextView cleanerNameDisplay = view.findViewById(R.id.cleanerNameDisplay);
        TextView totalTaskDisplay = view.findViewById(R.id.summaryTaskNumberDisplay); // Later
        TextView dateDisplay = view.findViewById(R.id.dateDisplay);

        authViewModel.getSessionState().observe(getViewLifecycleOwner(), sessionUiState -> {
            if (sessionUiState instanceof SessionUiState.Ready) {
                Session session = ((SessionUiState.Ready) sessionUiState).getSession();
                cleanerNameDisplay.setText("Cleaner " + session.getUser().getFirstName());
            }
        });

        String dateFormatted = DateUtil.formatDateFromIntToString(date);
        dateDisplay.setText("As of " + dateFormatted);

        return view;
    }
}