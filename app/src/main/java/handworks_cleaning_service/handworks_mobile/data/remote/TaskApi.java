package handworks_cleaning_service.handworks_mobile.data.remote;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import handworks_cleaning_service.handworks_mobile.ui.models.Task;

public interface TaskApi {
    Map<LocalDate, List<Task>> getAllEvents();
}
