package handworks_cleaning_service.handworks_mobile.ui.adapters;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.infos.Asset;

public class AssetViewHolder extends RecyclerView.ViewHolder {
    private final ImageView assetImage;
    private final TextView assetName;
    private final TextView assetType;

    public AssetViewHolder(@NonNull View itemView) {
        super(itemView);

        assetImage = itemView.findViewById(R.id.assetAllocatedImageUrl);
        assetName = itemView.findViewById(R.id.assetAllocatedName);
        assetType = itemView.findViewById(R.id.assetAllocatedType);
    }

    void bind(Asset asset) {
        Glide.with(itemView.getContext())
                .load(asset.getPhotoUrl())
                .placeholder(R.drawable.theme)
                .error(R.drawable.error_svgrepo_com)
                .circleCrop()
                .into(assetImage);

        assetName.setText((asset.getName().isEmpty() ? "N/A" : asset.getName()));
        assetType.setText(asset.getType().isEmpty() ? "N/A" : asset.getType());
        Log.d("HTTPs", asset.getName());
    }
}
