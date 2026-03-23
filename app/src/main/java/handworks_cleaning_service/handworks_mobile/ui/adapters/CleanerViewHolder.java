package handworks_cleaning_service.handworks_mobile.ui.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.infos.Cleaner;

public class CleanerViewHolder extends RecyclerView.ViewHolder {
    private final ImageView cleanerProfilePicture;
    private final TextView cleanerFullName;
    public CleanerViewHolder(@NonNull View itemView) {
        super(itemView);

        cleanerProfilePicture = itemView.findViewById(R.id.cleanerPfpUrl);
        cleanerFullName = itemView.findViewById(R.id.cleanersAssignedFullName);
    }

    void bind(Cleaner cleaner) {
        Glide.with(itemView.getContext())
                .load(cleaner.getPfpUrl())
                .placeholder(R.drawable.pfp_placeholder)
                .error(R.drawable.pfp_placeholder)
                .circleCrop()
                .into(cleanerProfilePicture);

        String lName = cleaner.getCleanerLastName().isEmpty() ? "N/A" : cleaner.getCleanerLastName();
        String fName = cleaner.getCleanerFirstName().isEmpty() ? "N/A" : cleaner.getCleanerFirstName();
        String fullName = lName + ", \n" + fName;
        cleanerFullName.setText(fullName);
    }
}
