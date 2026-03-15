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
import handworks_cleaning_service.handworks_mobile.data.repository.config.FetchStrategy;
import handworks_cleaning_service.handworks_mobile.data.repository.config.PaginationState;
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
        state.append(bookings);
    }

    public List<Booking> getCachedPage(String employeeId, String startDate, String endDate, int page) {
        PaginationState state = getPaginationState(employeeId, startDate, endDate);
        List<Booking> data = state.getAccumulated();

        int from = page * PAGE_LIMIT;
        int to = Math.min(from + PAGE_LIMIT, data.size());

        if (from >= data.size()) return null;

        return data.subList(from, to);
    }

    public PaginationState getPaginationState(String employeeId, String startDate, String endDate) {
        String key = buildKey(employeeId, startDate, endDate);
        return cachedBookings.computeIfAbsent(key, k -> new PaginationState());
    }

    public void clearCache() {
        cachedBookings.clear();
    }

    public void fetchBookingsByEmployeeId(BooksByEmployeeIdRequest request, FetchStrategy strategy, Callback<BookingWrapper> callback) {
        if (strategy == FetchStrategy.CACHE_ONLY) return;

        if (strategy == FetchStrategy.CACHE_FIRST) {
            // network will only happen if ViewModel decides cache empty
            bookApi.getEmployeeBookings(
                    request.employeeId,
                    request.startDate,
                    request.endDate,
                    request.pageNumber,
                    request.limit
            ).enqueue(callback);
            return;
        }

        if (strategy == FetchStrategy.NETWORK_ONLY) {
            bookApi.getEmployeeBookings(
                    request.employeeId,
                    request.startDate,
                    request.endDate,
                    request.pageNumber,
                    request.limit
            ).enqueue(callback);
        }
    }

    private String buildKey(String employeeId, String startDate, String endDate) {
        return employeeId + "|" + startDate + "|" + endDate;
    }
}
