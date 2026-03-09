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
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.databinding.FragmentHistoryBinding;
import handworks_cleaning_service.handworks_mobile.ui.adapters.BookingAdapter;
import handworks_cleaning_service.handworks_mobile.ui.pages.booking.BookingDetails;
import handworks_cleaning_service.handworks_mobile.ui.viewmodel.BookViewModel;

@AndroidEntryPoint
public class HistoryFragment extends Fragment {
    private FragmentHistoryBinding binding;
    private BookViewModel bookViewModel;
    private String employeeId = null;
    private LocalDate startDate;
    private LocalDate endDate = LocalDate.now();
    private BookingAdapter bookingAdapter;
    private boolean isFirstSelection = true;
    @Inject
    SharedPreferences prefs;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);

        bookViewModel = new ViewModelProvider(requireActivity()).get(BookViewModel.class);
        bookingAdapter = new BookingAdapter(new ArrayList<>(), booking -> {
            Intent intent = new Intent(requireContext(), BookingDetails.class);
            intent.putExtra("booking", booking);
            startActivity(intent);
        });

        // RecyclerView
        binding.bookingsRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.bookingsRecycler.setAdapter(bookingAdapter);

        // Spinners
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
        employeeId = prefs.getString("EMP_ID", null);

        if (endDate == null) endDate = LocalDate.now();
        if (startDate == null) startDate = endDate.minusDays(7);

        bookViewModel.restoreCachedOrLoad(employeeId, startDate.toString(), endDate.toString());
    }

    private void observeBookings() {
        bookViewModel.getBookings().observe(getViewLifecycleOwner(), bookings -> {
            boolean hasBooks = bookings != null && !bookings.isEmpty();

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
                "Past Week",
                "Past 2 Weeks",
                "Past Month"
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

                endDate = LocalDate.now();
                startDate = switch (position) {
                    case 1 -> endDate.minusDays(14);
                    case 2 -> endDate.minusMonths(1);
                    default -> endDate.minusDays(7);
                };

                if (employeeId != null) {
                    bookViewModel.resetPagination(employeeId, startDate.toString(), endDate.toString());
                    bookViewModel.loadNextPage(employeeId, startDate.toString(), endDate.toString());
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
                LinearLayoutManager lm = (LinearLayoutManager) rv.getLayoutManager();
                if (lm == null || employeeId == null || endDate == null) return;

                int totalItemCount = lm.getItemCount();
                int lastVisibleItem = lm.findLastVisibleItemPosition();

                if (totalItemCount <= lastVisibleItem + 5) {
                    bookViewModel.loadNextPage(employeeId, startDate.toString(), endDate.toString());
                }
            }
        });
    }

    private void updateUI(boolean hasBooks, boolean loading) {
        binding.loadingProgress.setVisibility(loading ? View.VISIBLE : View.GONE);

        binding.bookingsRecycler.setVisibility(!loading && hasBooks ? View.VISIBLE : GONE);
        binding.bookingsRecycler.setAlpha(loading ? 0.5f : 1f);

        binding.noHistoryYet.getRoot().setVisibility(!loading && !hasBooks ? VISIBLE : GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}