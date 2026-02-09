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
        if (useFakeData) {
            List<Task> fakeTasks = new ArrayList<>();
            fakeTasks.add(new Task("Floor Cleaning", LocalDate.of(2026, 2, 4),
                    LocalTime.of(9, 0), LocalTime.of(11, 0)));
            fakeTasks.add(new Task("Window Washing", LocalDate.of(2026, 2, 4),
                    LocalTime.of(12, 0), LocalTime.of(14, 0)));
            fakeTasks.add(new Task("Carpet Shampoo", LocalDate.of(2026, 2, 5),
                    LocalTime.of(10, 0), LocalTime.of(12, 0)));
            fakeTasks.add(new Task("Bathroom Disinfection", LocalDate.of(2026, 2, 6),
                    LocalTime.of(8, 0), LocalTime.of(9, 30)));

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
}
