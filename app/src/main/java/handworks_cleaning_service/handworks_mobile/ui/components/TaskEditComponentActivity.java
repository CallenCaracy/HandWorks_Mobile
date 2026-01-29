package handworks_cleaning_service.handworks_mobile.ui.components;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.LocalTime;

import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.ui.models.Task;
import handworks_cleaning_service.handworks_mobile.utils.CalendarUtils;

public class TaskEditComponentActivity extends AppCompatActivity {
    private EditText taskNameET;
    private TextView taskDateTV, taskTimeTV;
    private LocalTime time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_task_edit_component);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initWidgets();

        time = LocalTime.now();
        taskDateTV.setText("Date: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate));
        taskTimeTV.setText("Time: " + CalendarUtils.formattedTime(time));
    }

    private void initWidgets() {
        taskNameET = findViewById(R.id.taskNameET);
        taskDateTV = findViewById(R.id.taskDateTV);
        taskTimeTV = findViewById(R.id.taskTimeTV);
    }

    public void saveTaskAction(View view) {
        String taskName = taskNameET.getText().toString();
        Task newTask = new Task(taskName, CalendarUtils.selectedDate, time);
        Task.tasksList.add(newTask);
        finish();
    }
}