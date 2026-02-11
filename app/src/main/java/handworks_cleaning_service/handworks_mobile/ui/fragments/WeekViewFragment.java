package handworks_cleaning_service.handworks_mobile.ui.fragments;

import static handworks_cleaning_service.handworks_mobile.utils.CalendarUtils.daysInWeekArray;
import static handworks_cleaning_service.handworks_mobile.utils.CalendarUtils.monthYearFromDate;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;
import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.databinding.FragmentWeekViewBinding;
import handworks_cleaning_service.handworks_mobile.ui.adapters.CalendarAdapter;
import handworks_cleaning_service.handworks_mobile.ui.adapters.TaskAdapter;
import handworks_cleaning_service.handworks_mobile.ui.models.Task;
import handworks_cleaning_service.handworks_mobile.ui.viewmodel.TaskViewModel;
import handworks_cleaning_service.handworks_mobile.utils.CalendarUtils;

@AndroidEntryPoint
public class WeekViewFragment extends Fragment implements CalendarAdapter.OnItemListener {
    private FragmentWeekViewBinding binding;
    private CalendarAdapter calendarAdapter;
    private TaskViewModel taskViewModel;

    public WeekViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWeekViewBinding.inflate(inflater, container, false);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        if (CalendarUtils.selectedDate == null) {
            CalendarUtils.selectedDate = LocalDate.now();
        }

        taskViewModel.getEvents().observe(getViewLifecycleOwner(), events -> {
            if (events != null) {
                updateUI(events);
            }
        });

        binding.prevWeekBtn.setOnClickListener(v -> previousWeekAction());
        binding.nextWeekBtn.setOnClickListener(v -> nextWeekAction());
        binding.showMonthBtn.setOnClickListener(v -> showWholeCalendar());

        return binding.getRoot();
    }

    private void setWeekView() {
        taskViewModel.loadTasksForEmployee("placeholder", true);
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
        calendarAdapter.setSelectedDate(date);
        setWeekView();
    }

    @Override
    public void onResume() {
        super.onResume();
        setWeekView();
    }

    private void updateUI(Map<LocalDate, List<Task>> events) {
        binding.monthYearTV.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        if (calendarAdapter == null) {
            calendarAdapter = new CalendarAdapter(days, events, this, CalendarUtils.selectedDate);
            binding.calendarRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 7));
            binding.calendarRecyclerView.setAdapter(calendarAdapter);
        } else {
            calendarAdapter.updateDays(days);
            calendarAdapter.updateEvents(events);
        }

        List<Task> dailyTasks = events.get(CalendarUtils.selectedDate);
        if (dailyTasks == null) dailyTasks = new ArrayList<>();

        TaskAdapter taskAdapter = new TaskAdapter(requireContext(), dailyTasks);
        binding.taskListView.setAdapter(taskAdapter);
    }

    public void showWholeCalendar() {
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, new CalendarFragment())
                .addToBackStack(null)
                .commit();
    }
}