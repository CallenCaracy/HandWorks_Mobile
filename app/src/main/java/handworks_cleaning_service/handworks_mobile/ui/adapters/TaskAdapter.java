package handworks_cleaning_service.handworks_mobile.ui.adapters;

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
        Duration duration = Duration.between(task.getTimeStart(), task.getTimeEnd());
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;

        String taskText = String.format(Locale.getDefault(),
                holder.itemView.getContext().getString(R.string.task_with_duration),
                task.getName(),
                hours,
                minutes);

        holder.taskNameTV.setText(taskText);

        String timeText = CalendarUtils.formattedTime(task.getTimeStart())
                + "\n"
                + CalendarUtils.formattedTime(task.getTimeEnd());

        holder.timeTV.setText(timeText);
    }
}