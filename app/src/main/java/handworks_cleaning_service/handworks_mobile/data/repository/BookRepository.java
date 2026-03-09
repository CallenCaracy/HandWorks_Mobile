package handworks_cleaning_service.handworks_mobile.data.repository;

import static handworks_cleaning_service.handworks_mobile.utils.Constant.PAGE_LIMIT;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import handworks_cleaning_service.handworks_mobile.data.dto.book.BooksByEmployeeIdRequest;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.Booking;
import handworks_cleaning_service.handworks_mobile.data.models.wrappers.BookingWrapper;
import handworks_cleaning_service.handworks_mobile.data.remote.BookApi;
import handworks_cleaning_service.handworks_mobile.utils.PaginationState;
import retrofit2.Callback;

public class BookRepository {
    private final BookApi bookApi;
    private final Map<String, PaginationState> cachedBookings = new HashMap<>();

    @Inject
    public BookRepository(BookApi bookApi){
        this.bookApi = bookApi;
    }

    public void cachePage(String employeeId, String startDate, String endDate, int page, List<Booking> bookings) {
        PaginationState state = getPaginationState(employeeId, startDate, endDate);
        state.getAccumulated().addAll(bookings);
    }

    public List<Booking> getCachedPage(String employeeId, String startDate, String endDate, int page) {
        PaginationState state = getPaginationState(employeeId, startDate, endDate);
        int from = page * PAGE_LIMIT;
        int to = Math.min(from + PAGE_LIMIT, state.getAccumulated().size());
        if (from >= state.getAccumulated().size()) return null;
        return state.getAccumulated().subList(from, to);
    }

    public PaginationState getPaginationState(String employeeId, String startDate, String endDate) {
        String key = buildKey(employeeId, startDate, endDate);
        return cachedBookings.computeIfAbsent(key, k -> new PaginationState());
    }

    public void clearCache() {
        cachedBookings.clear();
    }

    public void fetchBookingsByEmployeeId(BooksByEmployeeIdRequest request, Callback<BookingWrapper> callback) {
        bookApi.getEmployeeBookings(
                request.employeeId,
                request.startDate,
                request.endDate,
                request.pageNumber,
                request.limit
        ).enqueue(callback);
    }

    private String buildKey(String employeeId, String startDate, String endDate) {
        return employeeId + "_" + startDate + "_" + endDate;
    }
}
