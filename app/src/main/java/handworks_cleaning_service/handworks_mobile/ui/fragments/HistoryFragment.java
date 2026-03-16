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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.Booking;
import handworks_cleaning_service.handworks_mobile.data.repository.config.FetchStrategy;
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
    private static final String PREF_HISTORY_FILTER = "history_filter_position";

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

        employeeId = prefs.getString("EMP_ID", null);

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
        initialFetchBookings();
        observeBookings();

        // Infinite scroll
        setupScrollListener();

        binding.swipeRefresh.setOnRefreshListener(() -> {
            Log.d("HTTPs", "refresh swiped");
            int savedPosition = prefs.getInt(PREF_HISTORY_FILTER, 0);

            if (endDate == null) endDate = LocalDate.now();
            startDate = dayDistance(savedPosition, endDate);
            bookViewModel.loadNextPage(employeeId, startDate.toString(), endDate.toString(), FetchStrategy.NETWORK_ONLY);
        });

        return binding.getRoot();
    }

    private void initialFetchBookings() {
        int savedPosition = prefs.getInt(PREF_HISTORY_FILTER, 0);

        if (endDate == null) endDate = LocalDate.now();
        startDate = dayDistance(savedPosition, endDate);

        bookViewModel.restoreCachedOrLoad(employeeId, startDate.toString(), endDate.toString());
    }

    private void observeBookings() {
        bookViewModel.getBookingsState().observe(getViewLifecycleOwner(), state -> {
            switch (state.getStatus()) {

                case LOADING:
                    updateUI(false, true);
                    break;

                case SUCCESS:
                    binding.swipeRefresh.setRefreshing(false);

                    List<Booking> bookings = state.getData();
                    boolean hasBooks = bookings != null && !bookings.isEmpty();

                    bookingAdapter.submitList(bookings);

                    updateUI(hasBooks, false);
                    break;

                case ERROR:
                    binding.swipeRefresh.setRefreshing(false);

                    updateUI(false, false);
                    Toast.makeText(requireContext(), state.getMessage(), Toast.LENGTH_LONG).show();
                    break;
            }
        });
    }

    private void initSpinnerFilter() {
        List<String> options = Arrays.asList(
                "Past Week",
                "Past 2 Weeks",
                "Past Month",
                "Past 6 Months"
        );

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.item_spinner_book_filter,
                options
        );

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        binding.spinnerDateFilter.setAdapter(adapter);

        int savedPosition = prefs.getInt(PREF_HISTORY_FILTER, 0);
        binding.spinnerDateFilter.setSelection(savedPosition);
    }

    private void setupSpinnerListener() {
        binding.spinnerDateFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirstSelection) { isFirstSelection = false; return; }

                prefs.edit().putInt(PREF_HISTORY_FILTER, position).apply();

                endDate = LocalDate.now();
                startDate = dayDistance(position, endDate);

                if (employeeId != null) {
                    bookViewModel.resetPagination(employeeId, startDate.toString(), endDate.toString());
                    bookViewModel.loadNextPage(employeeId, startDate.toString(), endDate.toString(), FetchStrategy.CACHE_FIRST);
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

                int visibleThreshold = 2;
                if (totalItemCount > 0 && totalItemCount <= lastVisibleItem + visibleThreshold) {
                    bookViewModel.loadNextPage(employeeId, startDate.toString(), endDate.toString(), FetchStrategy.CACHE_FIRST);
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

    private LocalDate dayDistance(int distance, LocalDate starter) {
        return switch (distance) {
            case 1 -> starter.minusDays(14);
            case 2 -> starter.minusMonths(1);
            case 3 -> starter.minusMonths(6);
            default -> starter.minusDays(7);
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}