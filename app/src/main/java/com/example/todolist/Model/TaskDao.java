package com.example.todolist.Model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 Data Access Object used to access the local database
 @author Jay Stewart
 @author Bryce McNary
 */
@Dao
public interface TaskDao {

    /**
     * called to get a list of all the tasks stored in db
     * @return List The list of all tasks in the db
     */
    @Query("SELECT * FROM tasks")
    List<Task> getAll();

    /**
     * called to get a list of all the tasks stored in db which are marked as complete
     * @return List the list of all tasks in the db marked as complete
     */
    @Query("SELECT * FROM tasks WHERE isComplete = 1")
    List<Task> getComplete();

    /**
     * called to get a list of all the tasks stored in db which are marked as incomplete
     * @return List the list of all tasks in the db marked as incomplete
     */
    @Query("SELECT * FROM tasks WHERE isComplete = 0")
    List<Task> getIncomplete();

    /**
     * called to change a tasks stored value for isComplete to 1
     * @param taskId the id of the task to be modified
     */
    @Query("UPDATE tasks SET isComplete = 1 WHERE taskId = :taskId")
    void setComplete(int taskId);

    /**
     * called to change a tasks stored value for isComplete to 0
     * @param taskId the id of the task to be modified
     */
    @Query("UPDATE tasks SET isComplete = 0 WHERE taskId = :taskId")
    void setIncomplete(int taskId);

    /**
     * called to insert a task into the db
     * @param task the task to be inserted into the db
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Task task);

    /**
     * called to remove a task from the db
     * @param task the task to be removed from the db
     */
    @Delete
    void delete(Task task);
}
