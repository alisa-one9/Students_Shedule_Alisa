package com.example.students_shedule_alisa.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.students_shedule_alisa.models.Student;

@Database(entities = {Student.class},version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract StudentDao studentDao();
}
