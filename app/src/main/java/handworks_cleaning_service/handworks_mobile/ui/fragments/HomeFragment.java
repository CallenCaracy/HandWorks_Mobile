package handworks_cleaning_service.handworks_mobile.ui.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.data.dto.user.TimeInRequest;
import handworks_cleaning_service.handworks_mobile.data.dto.user.TimeOutRequest;
import handworks_cleaning_service.handworks_mobile.data.models.users.Employee;
import handworks_cleaning_service.handworks_mobile.data.models.users.TimeSheet;
import handworks_cleaning_service.handworks_mobile.data.repository.config.FetchStrategy;
import handworks_cleaning_service.handworks_mobile.databinding.FragmentHomeBinding;
import handworks_cleaning_service.handworks_mobile.ui.adapters.BookingAdapter;
import handworks_cleaning_service.handworks_mobile.ui.pages.booking.BookingDetails;
import handworks_cleaning_service.handworks_mobile.ui.viewmodel.BookViewModel;
import handworks_cleaning_service.handworks_mobile.ui.viewmodel.UserViewModel;
import handworks_cleaning_service.handworks_mobile.utils.DateUtil;

@AndroidEntryPoint
public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private BookViewModel bookViewModel;
    private UserViewModel userViewModel;
    private String employeeId = null;
    private LocalDate today = LocalDate.now();
    private LocalDate endDate;
    private BookingAdapter bookingAdapter;
    @Inject
    SharedPreferences prefs;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        bookViewModel = new ViewModelProvider(requireActivity()).get(BookViewModel.class);
        bookingAdapter = new BookingAdapter(new ArrayList<>(), booking -> {
            Intent intent = new Intent(requireContext(), BookingDetails.class);
            intent.putExtra("booking", booking);
            startActivity(intent);
        });

        // RecyclerView
        binding.bookingsRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.bookingsRecycler.setAdapter(bookingAdapter);

        // Date display
        Date date = new Date();
        binding.dateDisplay.setText(getString(R.string.as_of_display, DateUtil.formatDateFromIntToString(date)));

        // Observers
        observeEmployee();
        observeBookings();
        observeTimeSheet();

        // Setups
        setupScrollListener();
        setupTimeSheetBotton();

        return binding.getRoot();
    }

    private void observeEmployee() {
        userViewModel.loadEmployee(prefs.getString("EMP_ID", null), FetchStrategy.CACHE_ONLY);
        userViewModel.getEmployee().observe(getViewLifecycleOwner(), state -> {
            switch (state.getStatus()) {
                case LOADING:
                    binding.cleanerNameDisplay.setText(R.string.loading);
                    break;
                case SUCCESS:
                    Employee employee = state.getData();
                    employeeId = employee.getId();
                    binding.cleanerNameDisplay.setText(getString(R.string.cleaner_name_display, employee.getAccount().getFirst_name()));

                    if (today == null) today = LocalDate.now();
                    if (endDate == null) endDate = today.plusDays(7);

                    bookViewModel.restoreCachedOrLoad(employeeId, today.toString(), endDate.toString());
                    break;
                case ERROR:
                    binding.cleanerNameDisplay.setText(getString(R.string.cleaner_name_display, "Error"));
                    Toast.makeText(getContext(), "Error: " + state.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    private void observeBookings() {
        bookViewModel.getBookings().observe(getViewLifecycleOwner(), bookings -> {
            boolean hasBooks = bookings != null && !bookings.isEmpty();

            updateUI(hasBooks, bookViewModel.getIsLoading().getValue() != null && bookViewModel.getIsLoading().getValue());

            bookingAdapter.submitList(bookings);
        });

        bookViewModel.getIsLoading().observe(getViewLifecycleOwner(), loading -> {
            boolean hasBooks = bookViewModel.getBookings().getValue() != null && !bookViewModel.getBookings().getValue().isEmpty();
            updateUI(hasBooks, loading != null && loading);
        });
    }

    private void observeTimeSheet() {
        userViewModel.getTimeSheetState().observe(getViewLifecycleOwner(), state -> {
            switch (state.getStatus()) {
                case LOADING:
                    binding.btnTimeIn.setEnabled(false);
                    binding.btnTimeOut.setEnabled(false);
                    break;

                case SUCCESS:
                    binding.btnTimeIn.setEnabled(true);
                    binding.btnTimeOut.setEnabled(true);

                    TimeSheet sheet = state.getData();
                    if (sheet.getTimeOut() == null) {
                        binding.btnTimeIn.setVisibility(View.GONE);
                        binding.btnTimeOut.setVisibility(View.VISIBLE);
                    } else {
                        binding.btnTimeIn.setVisibility(View.VISIBLE);
                        binding.btnTimeOut.setVisibility(View.GONE);
                    }
                    break;

                case ERROR:
                    binding.btnTimeIn.setEnabled(true);
                    binding.btnTimeOut.setEnabled(true);
                    Toast.makeText(requireContext(), "Error: " + state.getMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    private void setupScrollListener() {
        binding.bookingsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView rv, int dx, int dy) {
                if (dy <= 0) return;
                LinearLayoutManager lm = (LinearLayoutManager) rv.getLayoutManager();
                if (lm == null || employeeId == null || endDate == null) return;

                int totalItemCount = lm.getItemCount();
                int lastVisibleItem = lm.findLastVisibleItemPosition();

                int visibleThreshold = 2;
                if (totalItemCount > 0 && totalItemCount <= lastVisibleItem + visibleThreshold) {
                    bookViewModel.loadNextPage(employeeId, today.toString(), today.toString());
                }
            }
        });
    }

    private void setupTimeSheetBotton() {
        binding.btnTimeIn.setOnClickListener(v -> {
            String timeNow = LocalTime.now().toString();
            TimeInRequest request = new TimeInRequest(employeeId, timeNow);
            userViewModel.timeIn(request);
        });

        binding.btnTimeOut.setOnClickListener(v -> new AlertDialog.Builder(requireContext())
                .setTitle("Time out")
                .setMessage("Are you sure you want to time out?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    String timeNow = LocalTime.now().toString();
                    TimeOutRequest request = new TimeOutRequest(employeeId, timeNow);
                    userViewModel.timeOut(request);
                })
                .setNegativeButton("No", null)
                .show());
    }

    private void updateUI(boolean hasBooks, boolean loading) {
        binding.loadingProgress.setVisibility(loading ? View.VISIBLE : View.GONE);

        binding.bookingsRecycler.setVisibility(!loading && hasBooks ? View.VISIBLE : GONE);
        binding.bookingsRecycler.setAlpha(loading ? 0.5f : 1f);
        binding.summaryTaskNumberDisplay.setText(String.valueOf(hasBooks ? bookViewModel.getTotalBookings() : 0));

        binding.noJobAssignedYet.getRoot().setVisibility(!loading && !hasBooks ? VISIBLE : GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}