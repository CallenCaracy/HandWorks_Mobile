package handworks_cleaning_service.handworks_mobile.ui.adapters;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.Duration;
import java.util.Locale;

import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.ui.models.Task;
import handworks_cleaning_service.handworks_mobile.utils.CalendarUtils;
import handworks_cleaning_service.handworks_mobile.utils.MapServiceType;

public class TaskViewHolder extends RecyclerView.ViewHolder {
    View container;
    TextView timeTV;
    TextView taskNameTV;
    public TaskViewHolder(@NonNull View itemView) {
        super(itemView);
        container = itemView.findViewById(R.id.taskContainer);
        timeTV = itemView.findViewById(R.id.timeTV);
        taskNameTV = itemView.findViewById(R.id.taskNameTV);
    }

    public void bind(Task task) {
        Log.d("HTTPs", task.toString());
        Duration duration = Duration.between(task.getTimeStart(), task.getTimeEnd()).plusHours(task.getExtraHours());
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;

        String taskText = String.format(Locale.getDefault(),
                "%s\n(%s) %dh %dm",
                task.getName(),
                MapServiceType.getReadableServiceType(itemView.getContext(), task.getType()),
                hours,
                minutes
        );
        taskNameTV.setText(taskText);

        String timeText = CalendarUtils.formattedTime(task.getTimeStart())
                + "\n"
                + CalendarUtils.formattedTime(task.getTimeEnd());
        timeTV.setText(timeText);
    }
}
