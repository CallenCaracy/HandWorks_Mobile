package handworks_cleaning_service.handworks_mobile.ui.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Task
{
    public static ArrayList<Task> tasksList = new ArrayList<>();

    public static ArrayList<Task> tasksForDate(LocalDate date)
    {
        ArrayList<Task> tasks = new ArrayList<>();

        for(Task task : tasksList)
        {
            if(task.getDate().equals(date))
                tasks.add(task);
        }

        return tasks;
    }


    private String name;
    private LocalDate date;
    private LocalTime time;

    public Task(String name, LocalDate date, LocalTime time)
    {
        this.name = name;
        this.date = date;
        this.time = time;
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

    public LocalTime getTime()
    {
        return time;
    }

    public void setTime(LocalTime time)
    {
        this.time = time;
    }
}
