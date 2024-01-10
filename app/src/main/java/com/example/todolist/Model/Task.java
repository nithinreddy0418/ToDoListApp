package com.example.todolist.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A class for representing a To-Do task.
 * For example:
 * <pre>
 *     Task task = new Task(taskId, taskImage, isComplete, taskName, taskDescription, taskDate)
 * </pre>
 */
@Entity(tableName = "tasks")
public class Task implements Comparable<Task>{
    @PrimaryKey
    private int taskId;
    private int taskImage;
    private boolean isComplete;
    private String taskName, taskDescription, taskDate;

    public Task(int taskId, int taskImage, boolean isComplete, String taskName, String taskDescription, String taskDate) {
        this.taskId = taskId;
        this.taskImage = taskImage;
        this.isComplete = isComplete;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskDate = taskDate;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getTaskImage() {return taskImage;}

    public void setTaskImage(int taskImage) {
        this.taskImage = taskImage;
    }

    public boolean getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    @Override
    public int compareTo(Task o) {
        try {
            Date d1 = new SimpleDateFormat("MM/dd/yyyy").parse(getTaskDate());
            Date d2 = new SimpleDateFormat("MM/dd/yyyy").parse(o.getTaskDate());
            return d1.compareTo(d2);
        } catch (ParseException e) {
            return 0;
        }
    }
}
