package handworks_cleaning_service.handworks_mobile.ui.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import handworks_cleaning_service.handworks_mobile.R;

public class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public final View parentView;
    public final View dot;
    public final TextView dayOfMonth;
    private final CalendarAdapter.OnItemListener onItemListener;

    public CalendarViewHolder(@NonNull View itemView, CalendarAdapter.OnItemListener onItemListener) {
        super(itemView);
        parentView = itemView.findViewById(R.id.parentView);
        dayOfMonth = itemView.findViewById(R.id.calendarCellText);
        dot = itemView.findViewById(R.id.eventDot);
        this.onItemListener = onItemListener;
        parentView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int position = getAdapterPosition();
        if (position == RecyclerView.NO_POSITION) return;

        onItemListener.onItemClick(position, null);
    }
}
