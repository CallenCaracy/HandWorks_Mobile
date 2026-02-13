package handworks_cleaning_service.handworks_mobile.ui.fragments;

import static handworks_cleaning_service.handworks_mobile.utils.CalendarUtils.daysInMonthArray;
import static handworks_cleaning_service.handworks_mobile.utils.CalendarUtils.monthYearFromDate;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;
import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.databinding.FragmentCalendarBinding;
import handworks_cleaning_service.handworks_mobile.ui.adapters.CalendarAdapter;
import handworks_cleaning_service.handworks_mobile.ui.models.Task;
import handworks_cleaning_service.handworks_mobile.ui.viewmodel.TaskViewModel;
import handworks_cleaning_service.handworks_mobile.utils.CalendarUtils;

@AndroidEntryPoint
public class CalendarFragment extends Fragment implements CalendarAdapter.OnItemListener {
    private FragmentCalendarBinding binding;
    private TaskViewModel taskViewModel;
    private CalendarAdapter calendarAdapter;

    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCalendarBinding.inflate(inflater, container, false);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        if (CalendarUtils.selectedDate == null) {
            CalendarUtils.selectedDate = LocalDate.now();
        }

        binding.previousMonthBtn.setOnClickListener(v -> previousMonthAction());
        binding.nextMonthBtn.setOnClickListener(v -> nextMonthAction());
        binding.hideMonthBtn.setOnClickListener(v -> hideMonth());

        setMonthView();

        return binding.getRoot();
    }

    private void setMonthView() {
        binding.monthYearTV.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(CalendarUtils.selectedDate);

        Map<LocalDate, List<Task>> monthEvents = taskViewModel.getMonthEvents(CalendarUtils.selectedDate);

        calendarAdapter = new CalendarAdapter(daysInMonth, monthEvents, this, CalendarUtils.selectedDate);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(requireContext(), 7);
        binding.calendarRecyclerView.setLayoutManager(layoutManager);
        binding.calendarRecyclerView.setAdapter(calendarAdapter);
    }

    private void previousMonthAction() {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
        setMonthView();
    }

    private void nextMonthAction() {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
        setMonthView();
    }

    private void hideMonth() {
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, new WeekViewFragment())
                .addToBackStack(null)
                .commit();
    }
    
    @Override
    public void onItemClick(int position, LocalDate date) {
        if (date == null) return;

        CalendarUtils.selectedDate = date;
        calendarAdapter.setSelectedDate(date);

        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, new WeekViewFragment())
                .addToBackStack(null)
                .commit();
    }
}