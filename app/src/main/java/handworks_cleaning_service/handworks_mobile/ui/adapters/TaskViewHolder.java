package handworks_cleaning_service.handworks_mobile.ui.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import handworks_cleaning_service.handworks_mobile.R;

public class TaskViewHolder extends RecyclerView.ViewHolder {
    TextView timeTV;
    TextView taskNameTV;
    public TaskViewHolder(@NonNull View itemView) {
        super(itemView);
        timeTV = itemView.findViewById(R.id.timeTV);
        taskNameTV = itemView.findViewById(R.id.taskNameTV);
    }
}
