package handworks_cleaning_service.handworks_mobile.ui.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.time.LocalDate;

import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.Booking;
import handworks_cleaning_service.handworks_mobile.ui.models.BookingStatus;
import handworks_cleaning_service.handworks_mobile.utils.DateUtil;
import handworks_cleaning_service.handworks_mobile.utils.MapServiceType;

public class BookingViewHolder extends RecyclerView.ViewHolder {
    private final ImageView bookingTypeIcon;
    private final TextView totalPrice;
    private final TextView status;
    private final TextView bookingWorkDate;
    private final TextView bookingStartDate;
    private final TextView bookingEndDate;
    private final TextView bookingMainService;

    public BookingViewHolder(@NonNull View itemView) {
        super(itemView);

        bookingTypeIcon = itemView.findViewById(R.id.iconBookingType);
        bookingMainService = itemView.findViewById(R.id.bookingMainService);
        status = itemView.findViewById(R.id.bookingStatus);
        totalPrice = itemView.findViewById(R.id.totalPrice);
        bookingWorkDate = itemView.findViewById(R.id.bookingWorkDate);
        bookingStartDate = itemView.findViewById(R.id.bookingStartTime);
        bookingEndDate = itemView.findViewById(R.id.bookingEndTime);
    }

    void bind(Booking booking) {
        Context context = itemView.getContext();
        LocalDate today = LocalDate.now();

        String isoDate = booking.getBase().getStartSched();
        String workDateStr = DateUtil.extractDateFromISO8601TimeStamps(isoDate);

        LocalDate workDate = LocalDate.parse(workDateStr);

        String startTime = DateUtil.extractTimeFromISO8601TimeStamps(isoDate);
        String endTime = DateUtil.extractTimeFromISO8601TimeStamps(
                booking.getBase().getEndSched()
        );

        BookingStatus bookingStatus = BookingStatus.fromBackend(booking.getBase().getStatus());
        status.setText(bookingStatus.label);
        status.setBackgroundTintList(
                ContextCompat.getColorStateList(itemView.getContext(), bookingStatus.colorRes)
        );

        bookingTypeIcon.setImageResource(MapServiceType.getIconServiceType(booking.getMainService().getServiceType()));
        totalPrice.setText(context.getString(R.string.price_format, booking.getTotalPrice()));
        bookingWorkDate.setText(workDateStr);
        bookingStartDate.setText(startTime);
        bookingEndDate.setText(endTime);
        bookingMainService.setText(MapServiceType.getReadableServiceType(context, booking.getMainService().getServiceType())
        );
    }
}