package handworks_cleaning_service.handworks_mobile.data.models.wrappers;

import java.util.List;

import handworks_cleaning_service.handworks_mobile.data.models.bookings.Booking;

public class BookingWrapper {
    private List<Booking> bookings;
    private int bookingsRequested;
    private int totalBookings;

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public void setBookingsRequested(int bookingsRequested) {
        this.bookingsRequested = bookingsRequested;
    }

    public void setTotalBookings(int totalBookings) {
        this.totalBookings = totalBookings;
    }

    public List<Booking> getBookings() { return bookings; }
    public int getBookingsRequested() { return bookingsRequested; }
    public int getTotalBookings() { return totalBookings; }
}
