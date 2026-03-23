package handworks_cleaning_service.handworks_mobile.ui.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.UUID;

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

    private final String id;
    private String name;
    private String type;
    private LocalDate date;
    private LocalTime timeStart, timeEnd;
    private int extraHours;

    public Task(String id, String name, String type, LocalDate date, LocalTime timeStart, LocalTime timeEnd, int extraHours) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.extraHours = extraHours;
    }

    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getId() { return id; }
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
