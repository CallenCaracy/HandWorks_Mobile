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

        return new CalendarViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        final LocalDate date = days.get(position);

        if (date == null) {
            holder.dayOfMonth.setText("");
            holder.dot.setVisibility(View.GONE);
            holder.parentView.setBackgroundColor(
                    ContextCompat.getColor(holder.parentView.getContext(), R.color.backgroundLightTransition)
            );
            return;
        }

        holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));

        holder.parentView.setBackgroundColor(
                ContextCompat.getColor(holder.parentView.getContext(), R.color.background)
        );

        if (date.equals(CalendarUtils.selectedDate)) {
            holder.parentView.setBackgroundColor(
                    ContextCompat.getColor(holder.parentView.getContext(), R.color.colorSecondary)
            );
        }

        if (eventsByDate.containsKey(date) && eventsByDate.get(date) != null) {
            holder.dot.setVisibility(View.VISIBLE);
        } else {
            holder.dot.setVisibility(View.GONE);
        }

        holder.parentView.setOnClickListener(v -> {
            int oldPosition = days.indexOf(CalendarUtils.selectedDate);

            CalendarUtils.selectedDate = date;

            int newPosition = days.indexOf(date);

            if (oldPosition != -1) notifyItemChanged(oldPosition);
            notifyItemChanged(newPosition);

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
        Map<LocalDate, List<Task>> oldEvents = new HashMap<>(this.eventsByDate);
        List<LocalDate> oldKeys = new ArrayList<>(oldEvents.keySet());
        List<LocalDate> newKeys = new ArrayList<>(newEvents != null ? newEvents.keySet() : List.of());

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() { return oldKeys.size(); }

            @Override
            public int getNewListSize() { return newKeys.size(); }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return oldKeys.get(oldItemPosition).equals(newKeys.get(newItemPosition));
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                List<Task> oldList = oldEvents.get(oldKeys.get(oldItemPosition));
                List<Task> newList = newEvents.get(newKeys.get(newItemPosition));
                return Objects.equals(oldList, newList);
            }
        });

        this.eventsByDate.clear();
        if (newEvents != null) this.eventsByDate.putAll(newEvents);

        diffResult.dispatchUpdatesTo(this);
    }
}
