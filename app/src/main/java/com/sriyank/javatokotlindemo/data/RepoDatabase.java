package com.sriyank.javatokotlindemo.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Repo.class}, version = 2, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class RepoDatabase extends RoomDatabase {

    public abstract RepoDao mRepoDao();

    private static RepoDatabase sRepoDatabase;

    public static RepoDatabase getRepoDatabase(Context context) {
        if (sRepoDatabase == null)  {
            sRepoDatabase = Room.databaseBuilder(context.getApplicationContext(),
            RepoDatabase.class, "Repo_Database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return sRepoDatabase;
    }
}
