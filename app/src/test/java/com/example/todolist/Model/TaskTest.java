package com.example.todolist.Model;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test class for Test.java
 * @see com.example.todolist.Model.Task
 */
public class TaskTest {

    final int TASK_ID = 1;
    final int TASK_IMAGE = 1;
    final boolean IS_COMPLETE = false;
    final String TASK_NAME = "TEST TASK";
    final String TASK_DESCRIPTION = "TEST DESCRIPTION";
    final String TASK_DATE = "01/01/2000";

    @Test
    public void taskTest(){
        Task task = new Task(TASK_ID, TASK_IMAGE, IS_COMPLETE, TASK_NAME, TASK_DESCRIPTION, TASK_DATE);

        assertEquals(task.getTaskId(), TASK_ID);
        assertEquals(task.getTaskImage(), TASK_IMAGE);
        assertEquals(task.getIsComplete(), IS_COMPLETE);
        assertEquals(task.getTaskName(), TASK_NAME);
        assertEquals(task.getTaskDescription(), TASK_DESCRIPTION);
        assertEquals(task.getTaskDate(), TASK_DATE);

        task.setTaskId(2);
        task.setTaskImage(2);
        task.setIsComplete(true);
        task.setTaskName("TASK 1");
        task.setTaskDescription("NEW DESCRIPTION");
        task.setTaskDate("03/28/2023");

        assertEquals(task.getTaskId(), 2);
        assertEquals(task.getTaskImage(), 2);
        assertEquals(task.getIsComplete(), true);
        assertEquals(task.getTaskName(), "TASK 1");
        assertEquals(task.getTaskDescription(), "NEW DESCRIPTION");
        assertEquals(task.getTaskDate(), "03/28/2023");

    }

}