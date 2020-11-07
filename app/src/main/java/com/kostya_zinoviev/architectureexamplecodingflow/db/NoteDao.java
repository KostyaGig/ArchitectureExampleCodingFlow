package com.kostya_zinoviev.architectureexamplecodingflow.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.kostya_zinoviev.architectureexamplecodingflow.model.Note;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    void insertNote(Note note);

    @Update
    void updateNote(Note note);

    @Delete
    void deleteNote(Note note);

    //Метод,который будем удалять все наши доступные в бд заметки
    @Query("DELETE FROM NoteT")
    void deleteAllNotes();

    //Метод,который возвращает нам livedata со  всеми Note из бд и они будут сортироваться по полю "priority"
    @Query("SELECT * FROM NoteT ORDER BY priority DESC")
    LiveData<List<Note>> getAllNotes();
}
