package com.example.gtc_movies_kayla_wagner.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Movie.class}, version = 1)
public abstract class MovieDatabase extends RoomDatabase {
    private static MovieDatabase instance;
    public abstract MovieDao movieDao();

    public static synchronized MovieDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                context.getApplicationContext(),
                MovieDatabase.class, 
                "movie_database"
            ).build();
        }
        return instance;
    }
}