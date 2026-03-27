package handworks_cleaning_service.handworks_mobile.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.ui.models.Task;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {
    private ArrayList<LocalDate> days;
    private final OnItemListener onItemListener;
    private final Map<LocalDate, List<Task>> eventsByDate;
    private LocalDate selectedDate;

    public CalendarAdapter(ArrayList<LocalDate> days, Map<LocalDate, List<Task>> eventsByDate, OnItemListener onItemListener, LocalDate selectedDate) {
        this.days = days;
        this.eventsByDate = (eventsByDate != null) ? eventsByDate : new HashMap<>();
        this.onItemListener = onItemListener;
        this.selectedDate = selectedDate;
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

        return new CalendarViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        final LocalDate date = days.get(position);

        holder.bind(date);
        holder.dot.setVisibility((eventsByDate.containsKey(date) && eventsByDate.get(date) != null) ? View.VISIBLE :  View.GONE);

        holder.parentView.setOnClickListener(v -> {
            setSelectedDate(date);
            onItemListener.onItemClick(position, date);
        });
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public interface OnItemListener {
        void onItemClick(int position, LocalDate date);
    }

    public void setSelectedDate(LocalDate newDate) {
        int oldPosition = days.indexOf(selectedDate);
        int newPosition = days.indexOf(newDate);

        selectedDate = newDate;

        if (oldPosition != -1) notifyItemChanged(oldPosition);
        if (newPosition != -1) notifyItemChanged(newPosition);
    }

    public void updateDays(List<LocalDate> newDays) {
        DiffUtil.DiffResult diffResult =
                DiffUtil.calculateDiff(new DiffUtil.Callback() {

                    @Override
                    public int getOldListSize() {
                        return days.size();
                    }

                    @Override
                    public int getNewListSize() {
                        return newDays.size();
                    }

                    @Override
                    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                        return days.get(oldItemPosition)
                                .equals(newDays.get(newItemPosition));
                    }

                    @Override
                    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                        LocalDate oldDate = days.get(oldItemPosition);
                        LocalDate newDate = newDays.get(newItemPosition);
                        return Objects.equals(oldDate, newDate);
                    }
                });

        days = new ArrayList<>(newDays);
        diffResult.dispatchUpdatesTo(this);
    }

    public void updateEvents(Map<LocalDate, List<Task>> newEvents) {
        this.eventsByDate.clear();
        if (newEvents != null) this.eventsByDate.putAll(newEvents);

        notifyDataSetChanged();
    }

    public void updateEventsLegacy(Map<LocalDate, List<Task>> newEvents) {
        Map<LocalDate, List<Task>> oldEvents = new HashMap<>(this.eventsByDate);

        this.eventsByDate.clear();
        if (newEvents != null) {
            this.eventsByDate.putAll(newEvents);
        }

        for (int i = 0; i < days.size(); i++) {
            LocalDate date = days.get(i);

            boolean oldHasEvent = oldEvents.containsKey(date) && oldEvents.get(date) != null;
            boolean newHasEvent = this.eventsByDate.containsKey(date) && this.eventsByDate.get(date) != null;

            if (oldHasEvent != newHasEvent) {
                notifyItemChanged(i);
            }
        }
    }
}
