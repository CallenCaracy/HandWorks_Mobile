package handworks_cleaning_service.handworks_mobile.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.ui.models.Task;
import handworks_cleaning_service.handworks_mobile.utils.CalendarUtils;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {
    private ArrayList<LocalDate> days;
    private final OnItemListener onItemListener;
    private final Map<LocalDate, List<Task>> eventsByDate;

    public CalendarAdapter(ArrayList<LocalDate> days, Map<LocalDate, List<Task>> eventsByDate, OnItemListener onItemListener) {
        this.days = days;
        this.eventsByDate = (eventsByDate != null) ? eventsByDate : new HashMap<>();
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if(days.size() > 15)
            layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        else
            layoutParams.height = parent.getHeight();

        return new CalendarViewHolder(view, onItemListener, days);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        final LocalDate date = days.get(position);
        if(date == null) {
            holder.dayOfMonth.setText("");
            holder.dot.setVisibility(View.GONE);
        } else {
            holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));
            if(date.equals(CalendarUtils.selectedDate))
                holder.parentView.setBackgroundColor(ContextCompat.getColor(holder.parentView.getContext(),R.color.backgroundLightTransition));
            if (eventsByDate.containsKey(date) && eventsByDate.get(date) != null) {
                holder.dot.setVisibility(View.VISIBLE);
            } else {
                holder.dot.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public interface OnItemListener {
        void onItemClick(int position, LocalDate date);
    }

    public void updateDays(ArrayList<LocalDate> newDays) {
        this.days = newDays;
        notifyDataSetChanged();
    }
}
