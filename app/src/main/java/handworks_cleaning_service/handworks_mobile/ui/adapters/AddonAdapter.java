package handworks_cleaning_service.handworks_mobile.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.infos.Asset;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.services.Addon;

public class AddonAdapter extends RecyclerView.Adapter<AddonViewHolder> {
    private List<Addon> addons;

    public void setAddons(List<Addon> addons) {
        if (addons == null || addons.isEmpty()) {
            this.addons = new ArrayList<>();
            this.addons.add(new Addon("0", 0.00, null));
        } else {
            this.addons = addons;
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AddonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_addon, parent, false);
        return new AddonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddonViewHolder holder, int position) {
        Addon addon = addons.get(position);
        holder.bind(addon);
    }

    @Override
    public int getItemCount() { return (addons == null) ? 0 : addons.size(); }
}
