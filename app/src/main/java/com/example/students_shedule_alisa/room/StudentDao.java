package com.example.students_shedule_alisa.room;



import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.students_shedule_alisa.models.Student;

import java.util.List;
@Dao
public interface StudentDao {
    @Query("SELECT*FROM students")
    List<Student> getAll();

    @Insert
    void insert(Student student);

    @Query("SELECT*FROM students")
    LiveData<List<Student>> getAllLive();

    @Delete
    void delete(Student student);

    @Update
    void update(Student student);

    @Query("SELECT * FROM students ORDER BY name_surname ASC")
    List<Student> sortAll();
}
