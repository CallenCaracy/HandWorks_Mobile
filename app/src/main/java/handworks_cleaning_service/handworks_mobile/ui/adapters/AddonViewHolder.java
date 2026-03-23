package handworks_cleaning_service.handworks_mobile.ui.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.services.Addon;
import handworks_cleaning_service.handworks_mobile.utils.MapServiceType;

public class AddonViewHolder extends RecyclerView.ViewHolder {
    private final TextView addonServiceDetails;
    private final TextView addonServicePrice;

    public AddonViewHolder(@NonNull View itemView) {
        super(itemView);

        addonServiceDetails = itemView.findViewById(R.id.addonServiceDetails);
        addonServicePrice = itemView.findViewById(R.id.addonServicePrice);
    }

    void bind(Addon addon) {
        if (addon.getServiceDetail() != null && addon.getServiceDetail().getDetails() != null) {
            addonServiceDetails.setText(MapServiceType.getReadableServiceDetails(addon.getServiceDetail().getDetails()));
        } else {
            addonServiceDetails.setText("N/A");
        }
        addonServicePrice.setText("₱" + addon.getPrice());
    }
}
