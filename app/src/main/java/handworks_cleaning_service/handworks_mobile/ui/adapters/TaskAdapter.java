package handworks_cleaning_service.handworks_mobile.ui.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import java.time.Duration;
import java.util.Locale;
import java.util.Objects;

import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.ui.models.Task;
import handworks_cleaning_service.handworks_mobile.ui.pages.booking.BookingDetails;
import handworks_cleaning_service.handworks_mobile.utils.CalendarUtils;

public class TaskAdapter extends ListAdapter<Task, TaskViewHolder> {

    public TaskAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Task> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {

                @Override
                public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
                    return oldItem.getId().equals(newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
                    return Objects.equals(oldItem.getName(), newItem.getName())
                            && Objects.equals(oldItem.getTimeStart(), newItem.getTimeStart())
                            && Objects.equals(oldItem.getTimeEnd(), newItem.getTimeEnd());
                }
            };

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_cell, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = getItem(position);

        holder.bind(task);

        holder.container.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), BookingDetails.class);
            intent.putExtra("BOOKING_ID", task.getId());
            v.getContext().startActivity(intent);
        });
    }
}