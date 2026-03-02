package handworks_cleaning_service.handworks_mobile.data.repository;

import javax.inject.Inject;

import handworks_cleaning_service.handworks_mobile.data.dto.book.BooksByEmployeeIdRequest;
import handworks_cleaning_service.handworks_mobile.data.models.wrappers.BookingWrapper;
import handworks_cleaning_service.handworks_mobile.data.remote.BookApi;
import retrofit2.Callback;

public class BookRepository {

    private final BookApi bookApi;

    @Inject
    public BookRepository(BookApi bookApi){
        this.bookApi = bookApi;
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
