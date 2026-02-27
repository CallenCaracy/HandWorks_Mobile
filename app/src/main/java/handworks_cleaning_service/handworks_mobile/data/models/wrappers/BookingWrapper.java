package handworks_cleaning_service.handworks_mobile.data.models.wrappers;

import java.util.List;

import handworks_cleaning_service.handworks_mobile.data.dto.book.BookingListResponse;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.Booking;

public class BookingWrapper {
    BookingListResponse data;

    public BookingWrapper(BookingListResponse data) {
        this.data = data;
    }

    public BookingListResponse getData() {
        return data;
    }

    public void setData(BookingListResponse data) {
        this.data = data;
    }
}
