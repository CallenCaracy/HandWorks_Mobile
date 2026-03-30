package handworks_cleaning_service.handworks_mobile.data.dto.book;

public class BookingSessionResponse {
    private final String bookingId;
    private final String status;

    public BookingSessionResponse(String bookingId, String status) {
        this.bookingId = bookingId;
        this.status = status;
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getStatus() {
        return status;
    }
}
