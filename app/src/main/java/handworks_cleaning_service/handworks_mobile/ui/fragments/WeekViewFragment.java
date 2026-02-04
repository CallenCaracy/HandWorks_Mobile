package handworks_cleaning_service.handworks_mobile.ui.fragments;

import static handworks_cleaning_service.handworks_mobile.utils.CalendarUtils.daysInWeekArray;
import static handworks_cleaning_service.handworks_mobile.utils.CalendarUtils.monthYearFromDate;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.time.LocalDate;
import java.util.ArrayList;

import handworks_cleaning_service.handworks_mobile.databinding.FragmentWeekViewBinding;
import handworks_cleaning_service.handworks_mobile.ui.adapters.CalendarAdapter;
import handworks_cleaning_service.handworks_mobile.ui.adapters.TaskAdapter;
import handworks_cleaning_service.handworks_mobile.ui.components.TaskEditComponentActivity;
import handworks_cleaning_service.handworks_mobile.ui.models.Task;
import handworks_cleaning_service.handworks_mobile.utils.CalendarUtils;

public class WeekViewFragment extends Fragment implements CalendarAdapter.OnItemListener {
    private FragmentWeekViewBinding binding;
    private CalendarAdapter calendarAdapter;

    public WeekViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWeekViewBinding.inflate(inflater, container, false);

        if (CalendarUtils.selectedDate == null) {
            CalendarUtils.selectedDate = LocalDate.now();
        }

        binding.prevWeekBtn.setOnClickListener(v -> previousWeekAction());
        binding.nextWeekBtn.setOnClickListener(v -> nextWeekAction());

        return binding.getRoot();
    }

    private void setWeekView() {
        binding.monthYearTV.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        if (calendarAdapter == null) {
            calendarAdapter = new CalendarAdapter(days, null, this);
            binding.calendarRecyclerView.setLayoutManager(
                    new GridLayoutManager(requireContext(), 7)
            );
            binding.calendarRecyclerView.setAdapter(calendarAdapter);
        } else {
            calendarAdapter.updateDays(days);
        }
        setTaskAdapter();
    }


    public void previousWeekAction() {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }

    public void nextWeekAction() {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        CalendarUtils.selectedDate = date;
        setWeekView();
    }

    @Override
    public void onResume() {
        super.onResume();
        setWeekView();
    }

    private void setTaskAdapter() {
        ArrayList<Task> dailyTasks = Task.tasksForDate(CalendarUtils.selectedDate);
        TaskAdapter taskAdapter = new TaskAdapter(requireContext(), dailyTasks);
        binding.taskListView.setAdapter(taskAdapter);
    }

    public void newTaskAction() {
        startActivity(new Intent(requireContext(), TaskEditComponentActivity.class));
    }
}