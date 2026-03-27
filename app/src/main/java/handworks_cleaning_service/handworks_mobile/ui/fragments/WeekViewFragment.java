package handworks_cleaning_service.handworks_mobile.ui.fragments;

import static handworks_cleaning_service.handworks_mobile.utils.CalendarUtils.daysInWeekArray;
import static handworks_cleaning_service.handworks_mobile.utils.CalendarUtils.monthYearFromDate;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.databinding.FragmentWeekViewBinding;
import handworks_cleaning_service.handworks_mobile.ui.adapters.CalendarAdapter;
import handworks_cleaning_service.handworks_mobile.ui.adapters.TaskAdapter;
import handworks_cleaning_service.handworks_mobile.ui.models.Task;
import handworks_cleaning_service.handworks_mobile.ui.viewmodel.CalendarViewModel;
import handworks_cleaning_service.handworks_mobile.utils.CalendarUtils;

@AndroidEntryPoint
public class WeekViewFragment extends Fragment implements CalendarAdapter.OnItemListener {
    private FragmentWeekViewBinding binding;
    private CalendarAdapter calendarAdapter;
    private CalendarViewModel calendarViewModel;
    private TaskAdapter taskAdapter;
    @Inject
    SharedPreferences prefs;

    public WeekViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWeekViewBinding.inflate(inflater, container, false);
        calendarViewModel = new ViewModelProvider(this).get(CalendarViewModel.class);

        taskAdapter = new TaskAdapter();
        binding.taskTimeScheduleRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.taskTimeScheduleRecyclerView.setAdapter(taskAdapter);

        if (CalendarUtils.selectedDate == null) {
            CalendarUtils.selectedDate = LocalDate.now();
        }

        String employeeId = prefs.getString("EMP_ID", null);
        calendarViewModel.loadCachedTasks(employeeId);

        calendarViewModel.getCalendarTasks().observe(getViewLifecycleOwner(), state -> {
            switch (state.getStatus()) {
                case LOADING:
                    break;
                case SUCCESS:
                    List<Task> allTasks = state.getData();
                    Map<LocalDate, List<Task>> weekEvents = calendarViewModel.getEventsForWeek(allTasks);
                    updateUI(weekEvents);
                    break;
                case ERROR:
                    Toast.makeText(requireContext(), "Error: " + state.getMessage(), Toast.LENGTH_LONG).show();
                    break;
            }
        });

        binding.prevWeekBtn.setOnClickListener(v -> previousWeekAction());
        binding.nextWeekBtn.setOnClickListener(v -> nextWeekAction());
        binding.showMonthBtn.setOnClickListener(v -> showWholeCalendar());

        return binding.getRoot();
    }

    private void previousWeekAction() {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        calendarAdapter.setSelectedDate(CalendarUtils.selectedDate);
        refreshWeekView();
    }

    private void nextWeekAction() {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        calendarAdapter.setSelectedDate(CalendarUtils.selectedDate);
        refreshWeekView();
    }

    private void refreshWeekView() {
        String employeeId = prefs.getString("EMP_ID", null);
        calendarViewModel.loadCachedTasks(employeeId);
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

        List<Task> dailyTasks = events.getOrDefault(CalendarUtils.selectedDate, new ArrayList<>());
        taskAdapter.submitList(dailyTasks);
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        CalendarUtils.selectedDate = date;
        calendarAdapter.setSelectedDate(date);
        refreshWeekView();
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