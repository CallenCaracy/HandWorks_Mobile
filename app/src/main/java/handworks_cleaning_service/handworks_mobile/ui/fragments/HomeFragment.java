package handworks_cleaning_service.handworks_mobile.ui.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clerk.api.user.User;

import java.util.Date;

import dagger.hilt.android.AndroidEntryPoint;
import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.databinding.FragmentHomeBinding;
import handworks_cleaning_service.handworks_mobile.ui.viewmodel.AuthViewModel;
import handworks_cleaning_service.handworks_mobile.utils.DateUtil;

@AndroidEntryPoint
public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        int workCount = 0;

        AuthViewModel authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        Date date = new Date();
        String dateFormatted = DateUtil.formatDateFromIntToString(date);
        String firstName = null;

        User cachedUser = authViewModel.getCachedUser();
        if (cachedUser != null) {
            firstName = cachedUser.getFirstName();
        }

        binding.cleanerNameDisplay.setText(getString(R.string.cleaner_name_display, (firstName != null ? firstName : "Error")));
        binding.dateDisplay.setText(getString(R.string.as_of_display, dateFormatted));
        binding.summaryTaskNumberDisplay.setText("3");
        binding.noJobAssignedYet.getRoot().setVisibility((workCount == 0) ? VISIBLE : GONE);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}