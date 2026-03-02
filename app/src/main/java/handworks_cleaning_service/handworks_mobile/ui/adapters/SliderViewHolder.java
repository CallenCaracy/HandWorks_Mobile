package handworks_cleaning_service.handworks_mobile.ui.adapters;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import handworks_cleaning_service.handworks_mobile.R;

public class SliderViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;

    public SliderViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.sliderImage);
    }
}
