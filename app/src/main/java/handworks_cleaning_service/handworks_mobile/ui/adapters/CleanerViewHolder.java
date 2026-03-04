package handworks_cleaning_service.handworks_mobile.ui.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.infos.Cleaner;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.services.Addon;
import handworks_cleaning_service.handworks_mobile.utils.EnumHelper;

public class CleanerViewHolder extends RecyclerView.ViewHolder {
    private final ImageView cleanerProfilePicture;
    private final TextView cleanerFullName;
    public CleanerViewHolder(@NonNull View itemView) {
        super(itemView);

        cleanerProfilePicture = itemView.findViewById(R.id.cleanerPfpUrl);
        cleanerFullName = itemView.findViewById(R.id.cleanersAssignedFullName);
    }

    void bind(Cleaner cleaner) {
        if (cleaner != null) {
            Glide.with(itemView.getContext())
                    .load(cleaner.getPfpUrl())
                    .placeholder(R.drawable.pfp_placeholder)
                    .error(R.drawable.pfp_placeholder)
                    .circleCrop()
                    .into(cleanerProfilePicture);

            String fullName = cleaner.getCleanerLastName() + ", \n" + cleaner.getCleanerFirstName();
            cleanerFullName.setText(fullName);
        } else {
            cleanerFullName.setText("N/A");
        }
    }
}
