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
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        Task task = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_cell, parent, false);

        TextView eventCellTV = convertView.findViewById(R.id.taskCellTV);

        String name = (task == null) ? null : task.getName();
        LocalTime time = (task == null) ? null : task.getTime();

        String eventTitle =
                ((name == null || name.isEmpty()) ? "Name is null." : name)
                        + " "
                        + CalendarUtils.formattedTime(
                        (time == null) ? LocalTime.now() : time
                );

        eventCellTV.setText(eventTitle);
        return convertView;
    }
}