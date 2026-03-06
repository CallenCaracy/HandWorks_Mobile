package handworks_cleaning_service.handworks_mobile.data.repository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import handworks_cleaning_service.handworks_mobile.data.dto.book.BooksByEmployeeIdRequest;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.Booking;
import handworks_cleaning_service.handworks_mobile.data.models.users.Employee;
import handworks_cleaning_service.handworks_mobile.data.models.wrappers.BookingWrapper;
import handworks_cleaning_service.handworks_mobile.data.remote.BookApi;
import retrofit2.Callback;

public class BookRepository {
    private final BookApi bookApi;
    private List<Booking> cachedBookings = new ArrayList<>();

    @Inject
    public BookRepository(BookApi bookApi){
        this.bookApi = bookApi;
    }

    public List<Booking> getCachedBooking() {
        return new ArrayList<>(cachedBookings);
    }

    public void clearCache() {
        cachedBookings.clear();
    }

    public void cacheBookings(List<Booking> bookings) {
        cachedBookings.clear();
        cachedBookings.addAll(bookings);
    }

    public boolean hasCache() {
        return !cachedBookings.isEmpty();
    }

    public void fetchBookingsByEmployeeId(
            BooksByEmployeeIdRequest request,
            Callback<BookingWrapper> callback
    ) {
        bookApi.getEmployeeBookings(
                request.employeeId,
                request.startDate,
                request.endDate,
                request.pageNumber,
                request.limit
        ).enqueue(callback);
    }
}
