package handworks_cleaning_service.handworks_mobile.ui.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalDate;
import java.util.List;
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

    private List<Task> mapList(List<Booking> bookings) {
        return bookings.stream()
                .map(this::mapBookingToTask)
                .collect(Collectors.toList());
    }

    public void initCalendarTasks(String employeeId, LocalDate startDate, LocalDate endDate) {
        calendarTasks.setValue(UIState.loading());

        List<Booking> cached = bookRepository.getAllCachedBookings(employeeId, startDate.toString(), endDate.toString());

        if (cached.isEmpty()) {
            calendarTasks.setValue(UIState.success(null));
        } else {
            calendarTasks.setValue(UIState.success(mapList(cached)));
        }
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
}
