package com.example.gtc_movies_kayla_wagner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gtc_movies_kayla_wagner.adapter.MovieAdapter;
import com.example.gtc_movies_kayla_wagner.data.Movie;
import com.example.gtc_movies_kayla_wagner.databinding.ActivityMainBinding;
import com.example.gtc_movies_kayla_wagner.viewmodel.MovieViewModel;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private static final int SMS_PERMISSION_CODE = 100;
    private MovieViewModel movieViewModel;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupUI();
        checkForSmsPermission();
        initializeSampleData();
    }

    private void setupUI() {
        MovieAdapter adapter = new MovieAdapter(movie -> {
            movieViewModel.toggleFavorite(movie);
            if (movie.isFavorite()) {
                sendSmsMessage("Added to favorites: " + movie.getTitle());
            }
        });

        binding.recyclerViewMovies.setAdapter(adapter);
        binding.recyclerViewMovies.setLayoutManager(new LinearLayoutManager(this));

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        movieViewModel.getAllMovies().observe(this, adapter::submitList);

        binding.fabShare.setOnClickListener(v -> shareAllFavorites());
    }

    private void initializeSampleData() {
        movieViewModel.insert(new Movie("The Shawshank Redemption", 
            "Two imprisoned men bond over a number of years."));
        movieViewModel.insert(new Movie("The Godfather", 
            "The aging patriarch of an organized crime dynasty transfers control."));
        movieViewModel.insert(new Movie("The Dark Knight", 
            "Batman confronts the mysterious and deadly Joker."));
    }

    private void shareAllFavorites() {
        movieViewModel.getFavoriteMovies().observe(this, movies -> {
            if (!movies.isEmpty()) {
                StringBuilder messageBuilder = new StringBuilder("My favorite movies:\n");
                for (Movie movie : movies) {
                    messageBuilder.append("- ").append(movie.getTitle()).append("\n");
                }
                sendSmsMessage(messageBuilder.toString());
            }
        });
    }

    private void checkForSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    SMS_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_CODE) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Snackbar.make(binding.getRoot(), "SMS sharing features disabled", 
                    Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void sendSmsMessage(String movieInfo) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                String phoneNumber = "1234567890"; // Replace with user-selected number
                smsManager.sendTextMessage(phoneNumber, null, movieInfo, null, null);
                Toast.makeText(this, "Movie info shared via SMS", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Snackbar.make(binding.getRoot(), 
                    "Failed to send SMS: " + e.getMessage(), 
                    Snackbar.LENGTH_LONG).show();
            }
        }
    }
}