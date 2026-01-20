package handworks_cleaning_service.handworks_mobile.ui.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.ui.models.ProfileItem;
import handworks_cleaning_service.handworks_mobile.utils.Constant;

public class ProfileSettingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ProfileItem> items;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ProfileItem item);
    }

    public ProfileSettingsAdapter(List<ProfileItem> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == Constant.TYPE_HEADER) {
            View view = inflater.inflate(R.layout.item_theme_header, parent, false);
            return new HeaderVH(view);
        } else if (viewType == Constant.TYPE_SWITCH) {
            View view = inflater.inflate(R.layout.item_tab_switch, parent, false);
            return new SwitchVH(view);
        } else {
            View view = inflater.inflate(R.layout.item_tab_options, parent, false);
            return new ItemVH(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ProfileItem item = items.get(position);

        if (holder instanceof ItemVH vh) {
            vh.title.setText(item.title);
            vh.icon.setImageResource(item.iconRes);
            vh.itemView.setOnClickListener(v -> listener.onItemClick(item));
        } else if (holder instanceof HeaderVH vh) {
            vh.title.setText(item.title);
        } else if (holder instanceof SwitchVH vh) {
            vh.icon.setImageResource(item.iconRes);
            vh.title.setText(item.title);

            Context context = vh.itemView.getContext();
            SharedPreferences prefs =
                    context.getSharedPreferences(Constant.PREFS_NAME, Context.MODE_PRIVATE);

            vh.switchCompat.setOnCheckedChangeListener(null);

            boolean enabled = prefs.getBoolean("Notification_Toggle", false);
            vh.switchCompat.setChecked(enabled);

            vh.switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
                prefs.edit()
                        .putBoolean("Notification_Toggle", isChecked)
                        .apply();
            });
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class HeaderVH extends RecyclerView.ViewHolder {
        TextView title;
        HeaderVH(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.optionHeader);
        }
    }

    static class ItemVH extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title;

        ItemVH(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
        }
    }

    static class SwitchVH extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title;
        SwitchCompat switchCompat;

        SwitchVH(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
            switchCompat = itemView.findViewById(R.id.switchView);
        }
    }
}
