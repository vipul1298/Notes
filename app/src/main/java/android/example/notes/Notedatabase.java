package android.example.notes;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Note.class},version=1)
public abstract class Notedatabase extends RoomDatabase {

    public static Notedatabase instance;

    public abstract NotesDao notesDao();

    public static synchronized  Notedatabase getInstance(Context context){

        if(instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(),
                    Notedatabase.class,"note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomcallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomcallback = new RoomDatabase.Callback(){

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void>{
        private NotesDao notesDao;

        private PopulateDbAsyncTask (Notedatabase db){
            notesDao=db.notesDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            notesDao.insert(new Note("Title 1","Description 1",1));
            notesDao.insert(new Note("Title 2","Description 2",2));
            notesDao.insert(new Note("Title 3","Description 3",3));
            return null;
        }
    }

}
