package handworks_cleaning_service.handworks_mobile.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.data.models.bookings.infos.Cleaner;

public class CleanerAdapter extends RecyclerView.Adapter<CleanerViewHolder> {
    private List<Cleaner> cleaners;

    public void setCleaners(List<Cleaner> cleaners) {
        this.cleaners = cleaners;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CleanerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cleaner, parent, false);
        return new CleanerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CleanerViewHolder holder, int position) {
        Cleaner cleaner = cleaners.get(position);
        holder.bind(cleaner);
    }

    @Override
    public int getItemCount() { return (cleaners == null) ? 0 : cleaners.size(); }
}
