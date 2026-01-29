package handworks_cleaning_service.handworks_mobile.ui.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clerk.api.user.User;

import java.util.Date;

import dagger.hilt.android.AndroidEntryPoint;
import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.ui.viewmodel.AuthViewModel;
import handworks_cleaning_service.handworks_mobile.utils.DateUtil;

@AndroidEntryPoint
public class HomeFragment extends Fragment {
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView cleanerNameDisplay = view.findViewById(R.id.cleanerNameDisplay);
        TextView totalTaskDisplay = view.findViewById(R.id.summaryTaskNumberDisplay);
        TextView dateDisplay = view.findViewById(R.id.dateDisplay);
        View noJobAssignedYet = view.findViewById(R.id.noJobAssignedYet);
        int workCount = 0;

        AuthViewModel authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        Date date = new Date();
        String dateFormatted = DateUtil.formatDateFromIntToString(date);
        String firstName = null;

        User cachedUser = authViewModel.getCachedUser();
        if (cachedUser != null) {
            firstName = cachedUser.getFirstName();
        }

        dateDisplay.setText(getString(R.string.as_of_display, dateFormatted));
        cleanerNameDisplay.setText(getString(R.string.cleaner_name_display, (firstName != null ? firstName : "Error")));
        totalTaskDisplay.setText("3");

        noJobAssignedYet.setVisibility((workCount == 0) ? VISIBLE : GONE);

        return view;
    }
}