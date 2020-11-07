package com.kostya_zinoviev.architectureexamplecodingflow.db;


import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.kostya_zinoviev.architectureexamplecodingflow.model.Note;

@Database(entities = Note.class,version = 1)
public abstract class NoteDatabase extends androidx.room.RoomDatabase {

    private static NoteDatabase database;

    public static synchronized NoteDatabase getInstance(Context context){
        if (database == null){
            database = Room
                    .databaseBuilder(context.getApplicationContext(), NoteDatabase.class,"Note.db")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return database;
    }
    public abstract NoteDao getNoteDao();

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            //Этот колбэк вызывается при создании базы данных
            //При создании бд,будем заливать туда наши созданные вручную Note
            //Не забываем при создании дб в билдере написать addCallback(наш callback)

            PopulateDbAsyncTask populateDbAsyncTask = new PopulateDbAsyncTask(database);
            populateDbAsyncTask.execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void>{

        private NoteDao noteDao;

        public PopulateDbAsyncTask(NoteDatabase database){
            noteDao = database.getNoteDao();
        }


        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insertNote(new Note("title1","desc1",1));
            noteDao.insertNote(new Note("title2","desc2",2));
            noteDao.insertNote(new Note("title2","desc2",3));
            return null;
        }

    }
}
