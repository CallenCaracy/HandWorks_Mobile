package handworks_cleaning_service.handworks_mobile.ui.fragments;

import static handworks_cleaning_service.handworks_mobile.utils.CalendarUtils.daysInMonthArray;
import static handworks_cleaning_service.handworks_mobile.utils.CalendarUtils.monthYearFromDate;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.databinding.FragmentCalendarBinding;
import handworks_cleaning_service.handworks_mobile.ui.adapters.CalendarAdapter;
import handworks_cleaning_service.handworks_mobile.ui.models.Task;
import handworks_cleaning_service.handworks_mobile.utils.CalendarUtils;

public class CalendarFragment extends Fragment implements CalendarAdapter.OnItemListener {
    private FragmentCalendarBinding binding;

    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCalendarBinding.inflate(inflater, container, false);

        if (CalendarUtils.selectedDate == null) {
            CalendarUtils.selectedDate = LocalDate.now();
        }

        binding.previousMonthBtn.setOnClickListener(v -> previousMonthAction());
        binding.nextMonthBtn.setOnClickListener(v -> nextMonthAction());

        setMonthView();

        return binding.getRoot();
    }

    private void setMonthView() {
        binding.monthYearTV.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(CalendarUtils.selectedDate);

//        Testing!!!
        HashMap<LocalDate, List<Task>> eventsByDate = new HashMap<>();

        LocalDate date = LocalDate.of(2026, 2, 4);
        LocalTime timeStart = LocalTime.of(2, 0);
        LocalTime timeEnd = LocalTime.of(4, 0);
        var newTask = new Task("Test", date, timeStart, timeEnd);
        eventsByDate.put(
                date,
                List.of(newTask)
        );
//        Testing!!!

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, eventsByDate, this);
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

    @Override
    public void onItemClick(int position, LocalDate date) {
        if (date == null) return;

        CalendarUtils.selectedDate = date;

        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, new WeekViewFragment())
                .addToBackStack(null)
                .commit();
    }
}