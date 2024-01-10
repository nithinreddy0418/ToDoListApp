package com.example.todolist.Model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 Creates a local sql database using room
 @author Jay Stewart
 */
@Database(entities = {Task.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
}
