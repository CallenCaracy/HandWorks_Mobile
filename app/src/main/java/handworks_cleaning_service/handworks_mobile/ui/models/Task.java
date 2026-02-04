package handworks_cleaning_service.handworks_mobile.ui.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Task {
    public static ArrayList<Task> tasksList = new ArrayList<>();

    public static ArrayList<Task> tasksForDate(LocalDate date) {
        ArrayList<Task> tasks = new ArrayList<>();

        for(Task task : tasksList) {
            if(task.getDate().equals(date))
                tasks.add(task);
        }

        return tasks;
    }

    private String name;
    private LocalDate date;
    private LocalTime timeStart, timeEnd;

    public Task(String name, LocalDate date, LocalTime timeStart, LocalTime timeEnd) {
        this.name = name;
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public LocalDate getDate()
    {
        return date;
    }
    public void setDate(LocalDate date)
    {
        this.date = date;
    }
    public LocalTime getTimeStart()
    {
        return timeStart;
    }
    public void setTimeStart(LocalTime timeStart) {this.timeStart = timeStart;}
    public LocalTime getTimeEnd() {return timeEnd;}
    public void setTimeEnd(LocalTime timeEnd) {this.timeEnd = timeEnd;}
}
