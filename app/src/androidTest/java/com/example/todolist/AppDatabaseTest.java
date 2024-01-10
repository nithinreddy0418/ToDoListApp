package com.example.todolist;

import androidx.room.Room;

import androidx.test.core.app.ApplicationProvider;

import com.example.todolist.Model.AppDatabase;
import com.example.todolist.Model.Task;
import com.example.todolist.Model.TaskDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Objects;

/**
 * Test class for the local Android Room database
 * @see <a href="https://github.com/WSU-DGscheidle/spring23_project-go-team/issues/5">Github Issue #5</a>
 */
public class AppDatabaseTest {

    AppDatabase db;
    TaskDao taskDao;


    @Before
    public void setUp(){
        db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
                AppDatabase.class).allowMainThreadQueries().build();
        taskDao = db.taskDao();
    }

    @After
    public void tearDown(){
        db.close();
    }

    @Test
    public void testInsert() {
        Task task1 = new Task
                (1, R.drawable.placeholder, true, "task1", "desc1", "1/1/1");
        taskDao.insert(task1);

        assert Objects.equals(taskDao.getAll().get(0).getTaskId(), task1.getTaskId());
        assert Objects.equals(taskDao.getAll().get(0).getTaskImage(), task1.getTaskImage());
        assert Objects.equals(taskDao.getAll().get(0).getIsComplete(), task1.getIsComplete());
        assert Objects.equals(taskDao.getAll().get(0).getTaskName(), task1.getTaskName());
        assert Objects.equals(taskDao.getAll().get(0).getTaskDescription(), task1.getTaskDescription());
        assert Objects.equals(taskDao.getAll().get(0).getTaskDate(), task1.getTaskDate());
    }

    @Test
    public void testGetAll(){
        Task task1 = new Task
                (1, R.drawable.placeholder, false, "task1", "desc1", "1/1/1");
        Task task2= new Task
                (2, R.drawable.placeholder, true, "task2", "desc2", "1/2/1");
        Task task3 = new Task
                (3, R.drawable.placeholder, true, "task3", "desc3", "1/3/1");
        taskDao.insert(task1);
        taskDao.insert(task2);
        taskDao.insert(task3);
        List<Task> allTasks = taskDao.getAll();
        assert (allTasks.size() == 3);
        db.clearAllTables();
    }

    @Test
    public void testGetComplete(){
        Task task1 = new Task
                (1, R.drawable.placeholder, false, "task1", "desc1", "1/1/1");
        Task task2= new Task
                (2, R.drawable.placeholder, true, "task2", "desc2", "1/2/1");
        Task task3 = new Task
                (3, R.drawable.placeholder, true, "task3", "desc3", "1/3/1");
        taskDao.insert(task1);
        taskDao.insert(task2);
        taskDao.insert(task3);
        List<Task> completedTasks = taskDao.getComplete();
        assert (completedTasks.size() == 2);
        db.clearAllTables();
    }

    @Test
    public void testSetComplete(){
        Task task1 = new Task
                (1, R.drawable.placeholder, false, "task1", "desc1", "1/1/1");
        taskDao.insert(task1);
        taskDao.setComplete(task1.getTaskId());
        assert (taskDao.getAll().get(0).getIsComplete());
        db.clearAllTables();
    }

    @Test
    public void testGetIncomplete(){
        Task task1 = new Task
                (1, R.drawable.placeholder, false, "task1", "desc1", "1/1/1");
        Task task2= new Task
                (2, R.drawable.placeholder, true, "task2", "desc2", "1/2/1");
        Task task3 = new Task
                (3, R.drawable.placeholder, true, "task3", "desc3", "1/3/1");
        taskDao.insert(task1);
        taskDao.insert(task2);
        taskDao.insert(task3);
        List<Task> incompleteTasks = taskDao.getIncomplete();
        assert (incompleteTasks.size() == 1);
        db.clearAllTables();
    }


    @Test
    public void testSetIncomplete(){
        Task task1 = new Task
                (1, R.drawable.placeholder, true, "task1", "desc1", "1/1/1");
        taskDao.insert(task1);
        taskDao.setIncomplete(task1.getTaskId());
        assert (!taskDao.getAll().get(0).getIsComplete());
        db.clearAllTables();
    }

    @Test
    public void testInsertDuplicateId() {
        Task task1 = new Task
                (1, R.drawable.placeholder, true, "task1", "desc1", "1/1/1");
        Task task2 = new Task
                (1, R.drawable.placeholder, false, "task2", "desc2", "2/2/2");
        taskDao.insert(task1);
        taskDao.insert(task2);

        assert Objects.equals(taskDao.getAll().get(0).getTaskId(), task2.getTaskId());
        assert Objects.equals(taskDao.getAll().get(0).getTaskImage(), task2.getTaskImage());
        assert Objects.equals(taskDao.getAll().get(0).getIsComplete(), task2.getIsComplete());
        assert Objects.equals(taskDao.getAll().get(0).getTaskName(), task2.getTaskName());
        assert Objects.equals(taskDao.getAll().get(0).getTaskDescription(), task2.getTaskDescription());
        assert Objects.equals(taskDao.getAll().get(0).getTaskDate(), task2.getTaskDate());
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void testInsertNull() {
        taskDao.insert(null);
    }

    @Test
    public void testDelete() {
        Task task1 = new Task
                (1, R.drawable.placeholder, true, "task1", "desc1", "1/1/1");
        taskDao.insert(task1);
        taskDao.delete(task1);
        assert taskDao.getAll().isEmpty();
    }

}