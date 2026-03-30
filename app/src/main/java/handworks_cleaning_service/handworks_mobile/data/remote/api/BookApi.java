package handworks_cleaning_service.handworks_mobile.data.remote.api;

import handworks_cleaning_service.handworks_mobile.data.dto.book.BookingSessionResponse;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.Booking;
import handworks_cleaning_service.handworks_mobile.data.models.wrappers.BookingWrapper;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BookApi {
    @GET("booking/employee")
    Call<BookingWrapper> getEmployeeBookings(
            @Query("employeeId") String employeeId,
            @Query("startDate") String startDate,
            @Query("endDate") String endDate,
            @Query("page") Integer page,
            @Query("limit") Integer limit);

    @GET("booking/")
    Call<Booking> getBookingById(@Query("bookingId") String bookingId);

    @POST("booking/session/start")
    Call<BookingSessionResponse> startBookSession(@Query("bookingId") String bookingId);

    @POST("booking/session/end")
    Call<BookingSessionResponse> endBookSession(@Query("bookingId") String bookingId);
}
