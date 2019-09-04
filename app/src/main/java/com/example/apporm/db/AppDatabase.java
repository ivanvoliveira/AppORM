package com.example.apporm.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.apporm.model.Note;

@Database(entities = {Note.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NoteDAO noteDAO();

    public static AppDatabase getDatabase (Context context){
        AppDatabase db = Room.databaseBuilder(context,
                AppDatabase.class, "orm_db")
                .allowMainThreadQueries()
                .build();
        return db;
    }
}
