package handworks_cleaning_service.handworks_mobile.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.Booking;
import handworks_cleaning_service.handworks_mobile.data.repository.BookRepository;
import handworks_cleaning_service.handworks_mobile.ui.models.Task;
import handworks_cleaning_service.handworks_mobile.utils.DateUtil;
import handworks_cleaning_service.handworks_mobile.utils.uistate.UIState;

@HiltViewModel
public class CalendarViewModel extends ViewModel {
    private final BookRepository bookRepository;
    private final MutableLiveData<UIState<List<Task>>> calendarTasks = new MutableLiveData<>();

    @Inject
    public CalendarViewModel(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public LiveData<UIState<List<Task>>> getCalendarTasks() {
        return calendarTasks;
    }

    public void loadCachedTasks(String employeeId) {
        calendarTasks.setValue(UIState.loading());

        List<Booking> cached = bookRepository.getAllCachedBookingsForEmployee(employeeId);
        if (cached != null && !cached.isEmpty()) {
            List<Task> tasks = mapList(filterFutureBookings(cached));
            calendarTasks.setValue(UIState.success(tasks));
        } else {
            calendarTasks.setValue(UIState.success(List.of()));
        }
    }

    private List<Task> mapList(List<Booking> bookings) {
        return bookings.stream()
                .map(this::mapBookingToTask)
                .collect(Collectors.toList());
    }

    private Task mapBookingToTask(Booking booking) {
        return new Task(
                booking.getId(),
                booking.getBase().getCustomerFirstName() + " " + booking.getBase().getCustomerLastName(),
                booking.getMainService().getServiceType(),
                DateUtil.extractLocalDate(booking.getBase().getStartSched()),
                DateUtil.extractLocalTime(booking.getBase().getStartSched()),
                DateUtil.extractLocalTime(booking.getBase().getEndSched()),
                booking.getBase().getExtraHours()
        );
    }

    private List<Booking> filterFutureBookings(List<Booking> bookings) {
        LocalDate today = LocalDate.now();
        return bookings.stream()
                .filter(b -> !DateUtil.extractLocalDate(b.getBase().getStartSched()).isBefore(today))
                .collect(Collectors.toList());
    }

    public Map<LocalDate, List<Task>> getEventsForWeek(List<Task> allTasks) {
        return allTasks.stream()
                .collect(Collectors.groupingBy(Task::getDate));
    }

    public Map<LocalDate, List<Task>> getEventsForMonth(List<Task> allTasks) {
        return allTasks.stream()
                .collect(Collectors.groupingBy(Task::getDate));
    }
}