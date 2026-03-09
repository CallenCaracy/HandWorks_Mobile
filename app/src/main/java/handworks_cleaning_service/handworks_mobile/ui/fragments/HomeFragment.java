package handworks_cleaning_service.handworks_mobile.ui.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import handworks_cleaning_service.handworks_mobile.R;
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
    private boolean isFirstSelection = true;
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

        // Spinner
        initSpinnerFilter();
        setupSpinnerListener();

        // Observers
        observeEmployee();
        observeBookings();

        // Infinite scroll
        setupScrollListener();

        return binding.getRoot();
    }

    private void observeEmployee() {
        userViewModel.loadEmployee(prefs.getString("EMP_ID", null));
        userViewModel.getEmployee().observe(getViewLifecycleOwner(), employee -> {
            if (employee != null) {
                employeeId = employee.getId();
                binding.cleanerNameDisplay.setText(
                        getString(R.string.cleaner_name_display, employee.getAccount().getFirst_name())
                );

                if (today == null) today = LocalDate.now();
                if (endDate == null) endDate = today.plusDays(7);

                bookViewModel.restoreCachedOrLoad(employeeId, today.minusMonths(3).toString(), today.plusMonths(3).toString());
            } else {
                binding.cleanerNameDisplay.setText(getString(R.string.cleaner_name_display, "Error"));
            }
        });
    }

    private void observeBookings() {
        bookViewModel.getBookings().observe(getViewLifecycleOwner(), bookings -> {
            boolean hasBooks = bookings != null && !bookings.isEmpty();

            binding.summaryTaskNumberDisplay.setText(String.valueOf(bookings != null ? bookViewModel.getTotalBookings() : 0));

            updateUI(hasBooks, bookViewModel.getIsLoading().getValue() != null && bookViewModel.getIsLoading().getValue());

            bookingAdapter.submitList(bookings);
        });

        bookViewModel.getIsLoading().observe(getViewLifecycleOwner(), loading -> {
            boolean hasBooks = bookViewModel.getBookings().getValue() != null &&
                    !bookViewModel.getBookings().getValue().isEmpty();
            updateUI(hasBooks, loading != null && loading);
        });
    }

    private void initSpinnerFilter() {
        List<String> options = Arrays.asList(
                "This Week",
                "Next 2 Weeks",
                "This Month"
        );

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.item_spinner_book_filter,
                options
        );

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        binding.spinnerDateFilter.setAdapter(adapter);
    }

    private void setupSpinnerListener() {
        binding.spinnerDateFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirstSelection) { isFirstSelection = false; return; }

                today = LocalDate.now();
                endDate = switch (position) {
                    case 1 -> today.plusDays(14);
                    case 2 -> today.plusMonths(1);
                    default -> today.plusDays(7);
                };

                if (employeeId != null) {
                    bookViewModel.resetPagination(employeeId, today.toString(), endDate.toString());
                    bookViewModel.loadNextPage(employeeId, today.minusMonths(3).toString(), today.plusMonths(3).toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
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

                if (totalItemCount <= lastVisibleItem + 5) {
                    bookViewModel.loadNextPage(employeeId, today.minusMonths(3).toString(), today.plusMonths(3).toString());
                }
            }
        });
    }

    private void updateUI(boolean hasBooks, boolean loading) {
        binding.loadingProgress.setVisibility(loading ? View.VISIBLE : View.GONE);

        binding.bookingsRecycler.setVisibility(!loading && hasBooks ? View.VISIBLE : GONE);
        binding.bookingsRecycler.setAlpha(loading ? 0.5f : 1f);

        binding.noJobAssignedYet.getRoot().setVisibility(!loading && !hasBooks ? VISIBLE : GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}