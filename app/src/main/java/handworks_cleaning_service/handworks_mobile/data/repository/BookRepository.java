package handworks_cleaning_service.handworks_mobile.data.repository;

import androidx.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import handworks_cleaning_service.handworks_mobile.data.dto.book.BooksByEmployeeIdRequest;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.Booking;
import handworks_cleaning_service.handworks_mobile.data.models.wrappers.BookingWrapper;
import handworks_cleaning_service.handworks_mobile.data.remote.BookApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookRepository {
    private final BookApi bookApi;
    private BookingWrapper cachedBookingWrapper;

    @Inject
    public BookRepository(BookApi bookApi){ this.bookApi = bookApi; }

    public BookingWrapper getCachedBookings() {
        return cachedBookingWrapper;
    }
    public void clearCache() { this.cachedBookingWrapper = null; }

    public void fetchBookingsByEmployeeId(BooksByEmployeeIdRequest request, Callback<BookingWrapper> callback) {
        if (cachedBookingWrapper != null) {
            callback.onResponse(null, Response.success(cachedBookingWrapper));
            return;
        }

        bookApi.getEmployeeBookings(
                request.employeeId,
                request.startDate,
                request.endDate,
                request.pageNumber,
                request.limit
        ).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<BookingWrapper> call, @NonNull Response<BookingWrapper> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cachedBookingWrapper = response.body();
                    callback.onResponse(call, Response.success(response.body()));
                } else {
                    callback.onFailure(call, new Throwable("Empty or unsuccessful response"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<BookingWrapper> call, @NonNull Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }
}
