package handworks_cleaning_service.handworks_mobile.data.repository;

import static handworks_cleaning_service.handworks_mobile.utils.Constant.PAGE_LIMIT;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import handworks_cleaning_service.handworks_mobile.data.dto.book.BooksByEmployeeIdRequest;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.Booking;
import handworks_cleaning_service.handworks_mobile.data.models.wrappers.BookingWrapper;
import handworks_cleaning_service.handworks_mobile.data.remote.api.BookApi;
import handworks_cleaning_service.handworks_mobile.data.repository.config.FetchStrategy;
import handworks_cleaning_service.handworks_mobile.data.repository.config.PaginationState;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    private String buildKey(String employeeId, String startDate, String endDate) {
        return employeeId + "|" + startDate + "|" + endDate;
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

    public void fetchBookingById(String bookingId, FetchStrategy strategy, BookingCallback callback) {
        if (strategy == FetchStrategy.CACHE_ONLY || strategy == FetchStrategy.CACHE_FIRST) {
            Booking cached = findInCache(bookingId);
            if (cached != null) {
                callback.onSuccess(cached);

                if (strategy == FetchStrategy.CACHE_ONLY) return;
            }
        }

        bookApi.getBookingById(bookingId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Booking> call, @NonNull Response<Booking> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateCache(response.body());
                }
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Booking> call, @NonNull Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    private Booking findInCache(String bookingId) {
        for (PaginationState state : cachedBookings.values()) {
            for (Booking booking : state.getAccumulatedSet()) {
                if (booking.getId().equals(bookingId)) {
                    return booking;
                }
            }
        }
        return null;
    }

    private void updateCache(Booking updatedBooking) {
        for (PaginationState state : cachedBookings.values()) {
            Set<Booking> accumulated = state.getAccumulatedSet();
            accumulated.removeIf(b -> b.getId().equals(updatedBooking.getId()));
            accumulated.add(updatedBooking);
            return;
        }
    }

    public List<Booking> getAllCachedBookings(String employeeId, String startDate, String endDate) {
        String key = buildKey(employeeId, startDate, endDate);
        PaginationState state = cachedBookings.get(key);
        if (state == null) return List.of();
        return state.getAccumulated();
    }

    public interface BookingCallback {
        void onSuccess(Booking data);
        void onError(String message);
    }
}
