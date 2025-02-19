package com.example.gtc_movies_kayla_wagner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database and table names
    public static final String DATABASE_NAME = "gtc_movies.db";
    public static final String TABLE_USERS = "users";
    public static final String TABLE_MOVIES = "movies";

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    // onCreate - Creating tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME TEXT, PASSWORD TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_MOVIES + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, TITLE TEXT, GENRE TEXT)");
    }

    // onUpgrade - Drop and recreate tables if the database version changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);
        onCreate(db);
    }

    // Add a new user
    public boolean addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("USERNAME", username);
        contentValues.put("PASSWORD", password);
        long result = db.insert(TABLE_USERS, null, contentValues);
        return result != -1; // Returns true if insertion is successful
    }

    // Add a new movie
    public boolean addMovie(String title, String genre) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TITLE", title);
        contentValues.put("GENRE", genre);
        long result = db.insert(TABLE_MOVIES, null, contentValues);
        return result != -1; // Returns true if insertion is successful
    }

    // Get all movies
    public Cursor getAllMovies() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_MOVIES, null);
    }

    // Update a movie's details
    public boolean updateMovie(int id, String newTitle, String newGenre) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TITLE", newTitle);
        contentValues.put("GENRE", newGenre);
        int result = db.update(TABLE_MOVIES, contentValues, "ID = ?", new String[]{String.valueOf(id)});
        return result > 0; // Returns true if update is successful
    }

    // Delete a movie
    public boolean deleteMovie(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_MOVIES, "ID = ?", new String[]{String.valueOf(id)});
        return result > 0; // Returns true if deletion is successful
    }
}
