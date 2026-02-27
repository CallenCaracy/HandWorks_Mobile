package handworks_cleaning_service.handworks_mobile.ui.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.Booking;

public class BookingViewHolder extends RecyclerView.ViewHolder {
    TextView customerName;
    TextView bookingDate;
    TextView bookingStatus;

    public BookingViewHolder(@NonNull View itemView) {
        super(itemView);

        customerName = itemView.findViewById(R.id.customerName);
        bookingDate = itemView.findViewById(R.id.bookingDate);
        bookingStatus = itemView.findViewById(R.id.bookingStatus);
    }

    void bind(Booking booking) {
        customerName.setText(booking.getBase().getCustomerFirstName());
        bookingDate.setText(booking.getBase().getStartSched());
        bookingStatus.setText(booking.getBase().getPaymentStatus());
    }
}