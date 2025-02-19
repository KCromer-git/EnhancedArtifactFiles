package com.example.gtc_movies_kayla_wagner;

// Add the necessary imports
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity; // Import this class
import androidx.recyclerview.widget.RecyclerView; // For RecyclerView
import android.database.Cursor;
import android.widget.Toast;

public class MoviesActivity extends AppCompatActivity {

    RecyclerView movieRecyclerView;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        movieRecyclerView = findViewById(R.id.movieRecyclerView);
        db = new DatabaseHelper(this);

        // Fetch movies from the database and display them
        displayMovies();
    }

    // Method to display movies from the database
    private void displayMovies() {
        Cursor moviesCursor = db.getAllMovies();
        if (moviesCursor.moveToFirst()) {
            // Loop through all movie records and display them in the RecyclerView
            do {
                String title = moviesCursor.getString(moviesCursor.getColumnIndex("TITLE"));
                String genre = moviesCursor.getString(moviesCursor.getColumnIndex("GENRE"));
                // Add logic here to populate RecyclerView with the title and genre
            } while (moviesCursor.moveToNext());
        } else {
            Toast.makeText(this, "No movies found", Toast.LENGTH_SHORT).show();
        }
    }
}
