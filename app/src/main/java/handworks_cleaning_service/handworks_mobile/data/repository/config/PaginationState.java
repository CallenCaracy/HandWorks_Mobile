package handworks_cleaning_service.handworks_mobile.data.repository.config;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import handworks_cleaning_service.handworks_mobile.data.models.bookings.Booking;

public class PaginationState {
    private int currentPage = 0;
    private boolean isLastPage = false;
    private int totalBookings = 0;
    private boolean loading = false;
    private final List<Booking> accumulated = new ArrayList<>();
    public List<Booking> getAccumulated() {
        return List.copyOf(accumulated);
    }
    public void append(List<Booking> bookings) {
        for (Booking b : bookings) {
            if (!containsId(b.getId())) {
                accumulated.add(b);
            }
        }
    }

    boolean containsId(String id) {
        for (Booking b : accumulated) {
            if (b.getId().equals(id)) return true;
        }
        return false;
    }

    public void upsert(Booking booking) {
        accumulated.removeIf(b -> b.getId().equals(booking.getId()));
        accumulated.add(booking);
    }
    public boolean contains(String bookingId) {
        for (Booking b : accumulated) {
            if (b.getId().equals(bookingId)) {
                return true;
            }
        }
        return false;
    }
    public void nextPage() {
        currentPage++;
    }
    public void updateTotal(int totalBookings) {
        this.totalBookings = totalBookings;
        this.isLastPage = accumulated.size() >= totalBookings;
    }
    public void setLoading(boolean loading) {
        this.loading = loading; }
    public void reset() {
        accumulated.clear();
        currentPage = 0;
        totalBookings = 0;
        isLastPage = false;
        loading = false;
    }
    public int getCurrentPage() { return currentPage; }
    public int getTotalBookings() {
        return totalBookings;
    }
    public boolean canLoadMore() {
        return !loading && !isLastPage;
    }
    public void setIsLastPage(boolean isLastPage) { this.isLastPage = isLastPage; }
}
