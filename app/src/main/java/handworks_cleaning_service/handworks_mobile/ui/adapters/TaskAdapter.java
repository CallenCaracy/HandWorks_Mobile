package handworks_cleaning_service.handworks_mobile.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.LocalTime;
import java.util.List;

import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.ui.models.Task;
import handworks_cleaning_service.handworks_mobile.utils.CalendarUtils;

public class TaskAdapter extends ArrayAdapter<Task> {
    public TaskAdapter(@NonNull Context context, List<Task> tasks)
    {

        super(context, 0, tasks);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Task task = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_cell, parent, false);

        TextView eventCellTV = convertView.findViewById(R.id.taskCellTV);

        String name = (task == null) ? null : task.getName();
        LocalTime timeStart = (task == null) ? null : task.getTimeStart();
        LocalTime timeEnd = (task == null) ? null : task.getTimeEnd();

        String eventTitle =
                (name == null || name.isEmpty() ? "Name is null." : name)
                        + " "
                        + CalendarUtils.formattedTime(timeStart == null ? LocalTime.now() : timeStart)
                        + " - "
                        + CalendarUtils.formattedTime(timeEnd == null ? LocalTime.now() : timeEnd);

        eventCellTV.setText(eventTitle);
        return convertView;
    }
}