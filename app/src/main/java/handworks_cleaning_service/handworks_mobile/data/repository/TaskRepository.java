package handworks_cleaning_service.handworks_mobile.data.repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import handworks_cleaning_service.handworks_mobile.data.remote.TaskApi;
import handworks_cleaning_service.handworks_mobile.ui.models.Task;

public class TaskRepository implements TaskApi {
    private final TaskApi taskApi;
    private final Map<LocalDate, List<Task>> allEvents = new HashMap<>();

    @Inject
    public TaskRepository(TaskApi taskApi) { this.taskApi = taskApi; }

    public TaskApi getTaskApi() { return this.taskApi; }

    public Map<LocalDate, List<Task>> getAllEvents() {
        return allEvents;
    }

    public void setEvents(Map<LocalDate, List<Task>> events) {
        allEvents.clear();
        allEvents.putAll(events);
    }
}
