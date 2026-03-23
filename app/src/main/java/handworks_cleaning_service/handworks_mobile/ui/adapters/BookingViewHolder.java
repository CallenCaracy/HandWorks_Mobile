package handworks_cleaning_service.handworks_mobile.ui.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;

import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.Booking;
import handworks_cleaning_service.handworks_mobile.utils.DateUtil;
import handworks_cleaning_service.handworks_mobile.utils.MapServiceType;

public class BookingViewHolder extends RecyclerView.ViewHolder {
    private final TextView customerName;
    private final TextView status;
    private final TextView bookingWorkDate;
    private final TextView bookingStartDate;
    private final TextView bookingEndDate;
    private final TextView bookingMainService;

    public BookingViewHolder(@NonNull View itemView) {
        super(itemView);

        customerName = itemView.findViewById(R.id.customerName);
        status = itemView.findViewById(R.id.bookingStatus);
        bookingWorkDate = itemView.findViewById(R.id.bookingWorkDate);
        bookingStartDate = itemView.findViewById(R.id.bookingStartTime);
        bookingEndDate = itemView.findViewById(R.id.bookingEndTime);
        bookingMainService = itemView.findViewById(R.id.bookingMainService);
    }

    void bind(Booking booking) {
        LocalDate today = LocalDate.now();

        String isoDate = booking.getBase().getStartSched();
        String workDateStr = DateUtil.extractDateFromISO8601TimeStamps(isoDate);

        LocalDate workDate = LocalDate.parse(workDateStr);

        String startTime = DateUtil.extractTimeFromISO8601TimeStamps(isoDate);
        String endTime = DateUtil.extractTimeFromISO8601TimeStamps(
                booking.getBase().getEndSched()
        );

        if (today.equals(workDate)) {
            status.setText(R.string.today);
        } else if (today.isAfter(workDate)) {
            status.setText(R.string.past);
        } else {
            status.setText(R.string.upcoming);
        }

        customerName.setText(booking.getBase().getCustomerFirstName());
        bookingWorkDate.setText(workDateStr);
        bookingStartDate.setText(startTime);
        bookingEndDate.setText(endTime);
        bookingMainService.setText(MapServiceType.getReadableServiceType(itemView.getContext(), booking.getMainService().getServiceType())
        );
    }
}