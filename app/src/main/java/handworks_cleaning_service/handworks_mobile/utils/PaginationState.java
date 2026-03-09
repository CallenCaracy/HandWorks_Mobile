package handworks_cleaning_service.handworks_mobile.utils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import handworks_cleaning_service.handworks_mobile.data.models.bookings.Booking;

public class PaginationState {
    private int currentPage = 0;
    private boolean isLastPage = false;
    private int totalBookings = 0;
    private final Set<Booking> accumulated = new LinkedHashSet<>();

    public List<Booking> getAccumulated() {
        return new ArrayList<>(accumulated);
    }
    public void appendToAccumulated(List<Booking> accumulated) { this.accumulated.addAll(accumulated); }
    public void resetAccumulated() { this.accumulated.clear(); }
    public int getCurrentPage() { return currentPage; }
    public void incrementPage() { currentPage++; }
    public void resetPage() { currentPage = 0; }

    public int getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(int totalBookings) {
        this.totalBookings = totalBookings;
    }

    public boolean isLastPage() { return isLastPage; }
    public void setIsLastPage(boolean isLastPage) { this.isLastPage = isLastPage; }
}
