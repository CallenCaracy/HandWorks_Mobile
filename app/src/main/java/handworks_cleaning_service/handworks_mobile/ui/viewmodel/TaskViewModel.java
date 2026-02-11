package handworks_cleaning_service.handworks_mobile.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import handworks_cleaning_service.handworks_mobile.data.repository.TaskRepository;
import handworks_cleaning_service.handworks_mobile.ui.models.Task;

@HiltViewModel
public class TaskViewModel extends ViewModel {
    private final TaskRepository taskRepository;
    private final MutableLiveData<Map<LocalDate, List<Task>>> _events = new MutableLiveData<>();
    public LiveData<Map<LocalDate, List<Task>>> events = _events;

    @Inject
    public TaskViewModel(TaskRepository taskRepository) { this.taskRepository = taskRepository; }

    public void loadTasksForEmployee(String employeeId, boolean useFakeData) {
        String[] taskNames = {"Floor Cleaning", "Window Washing", "Carpet Shampoo", "Bathroom Disinfection", "Testing 123"};
        LocalDate[] taskDates = {
                LocalDate.of(2026, 2, 12),
                LocalDate.of(2025, 12, 30),
                LocalDate.of(2026, 2, 22),
                LocalDate.of(2026, 2, 28),
                LocalDate.of(2026, 2, 1)
        };

        LocalTime[] taskTimeStarts = {
                LocalTime.of(12, 0),
                LocalTime.of(10, 0),
                LocalTime.of(20, 0),
                LocalTime.of(15, 0),
                LocalTime.of(3, 30),
        };

        LocalTime[] taskTimeEnds = {
                LocalTime.of(15, 0),
                LocalTime.of(12, 0),
                LocalTime.of(23, 0),
                LocalTime.of(16, 0),
                LocalTime.of(5, 30),
        };

        if (useFakeData) {
            List<Task> fakeTasks = new ArrayList<>();
            for(int i = 0; i < 5; i++){
                fakeTasks.add(
                        new Task(
                                taskNames[i],
                                taskDates[i],
                                taskTimeStarts[i],
                                taskTimeEnds[i]
                        )
                );
            }

            Map<LocalDate, List<Task>> map = convertToMap(fakeTasks);
            taskRepository.setEvents(map);
            _events.postValue(map);
            return;
        }

//        taskRepository.getTaskApi().getAllEvents()
//                .enqueue(new retrofit2.Callback<List<Task>>() {
//                    @Override
//                    public void onResponse(retrofit2.Call<List<Task>> call, retrofit2.Response<List<Task>> response) {
//                        if (response.isSuccessful() && response.body() != null) {
//                            Map<LocalDate, List<Task>> map = convertToMap(response.body());
//                            taskRepository.setEvents(map);
//                            _events.postValue(map);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(retrofit2.Call<List<Task>> call, Throwable t) {
//                        _events.postValue(new HashMap<>());
//                    }
//                });
    }

    private Map<LocalDate, List<Task>> convertToMap(List<Task> tasks) {
        Map<LocalDate, List<Task>> map = new HashMap<>();
        for (Task task : tasks) {
            map.computeIfAbsent(task.getDate(), k -> new ArrayList<>()).add(task);
        }
        return map;
    }

    public Map<LocalDate, List<Task>> getMonthEvents(LocalDate selectedDate) {
        Map<LocalDate, List<Task>> allEvents = taskRepository.getAllEvents();
        return allEvents.entrySet().stream()
                .filter(entry -> entry.getKey().getMonth() == selectedDate.getMonth())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public LiveData<Map<LocalDate, List<Task>>> getEvents() {
        return events;
    }
}
