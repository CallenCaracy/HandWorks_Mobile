package handworks_cleaning_service.handworks_mobile.ui.fragments;

import static handworks_cleaning_service.handworks_mobile.utils.CalendarUtils.daysInMonthArray;
import static handworks_cleaning_service.handworks_mobile.utils.CalendarUtils.monthYearFromDate;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.time.LocalDate;
import java.util.ArrayList;

import handworks_cleaning_service.handworks_mobile.databinding.FragmentCalendarBinding;
import handworks_cleaning_service.handworks_mobile.ui.adapters.CalendarAdapter;
import handworks_cleaning_service.handworks_mobile.ui.components.WeekViewComponentActivity;
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

        binding.previousMonthBtn.setOnClickListener(v -> previousMonthAction());
        binding.nextMonthBtn.setOnClickListener(v -> nextMonthAction());
        binding.weeklyBtn.setOnClickListener(v -> weeklyAction());

        if (CalendarUtils.selectedDate == null) {
            CalendarUtils.selectedDate = LocalDate.now();
        }

        setMonthView();

        return binding.getRoot();
    }

    private void setMonthView() {
        binding.monthYearTV.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(requireContext(), 7);
        binding.calendarRecyclerView.setLayoutManager(layoutManager);
        binding.calendarRecyclerView.setAdapter(calendarAdapter);
        Log.d("CalendarFragment", "Days count = " + daysInMonth.size());

    }

    private void previousMonthAction() {
        CalendarUtils.selectedDate =
                CalendarUtils.selectedDate.minusMonths(1);
        setMonthView();
    }

    private void nextMonthAction() {
        CalendarUtils.selectedDate =
                CalendarUtils.selectedDate.plusMonths(1);
        setMonthView();
    }

    private void weeklyAction() {
        startActivity(
                new Intent(requireContext(), WeekViewComponentActivity.class)
        );
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        if(date != null) {
            CalendarUtils.selectedDate = date;
            setMonthView();
        }
    }
}