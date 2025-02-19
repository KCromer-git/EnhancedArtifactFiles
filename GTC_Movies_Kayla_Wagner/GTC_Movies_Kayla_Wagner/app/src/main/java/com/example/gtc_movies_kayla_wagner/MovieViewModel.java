package com.example.gtc_movies_kayla_wagner.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;
import com.example.gtc_movies_kayla_wagner.data.*;

public class MovieViewModel extends AndroidViewModel {
    private MovieRepository repository;
    private LiveData<List<Movie>> allMovies;
    private LiveData<List<Movie>> favoriteMovies;

    public MovieViewModel(Application application) {
        super(application);
        repository = new MovieRepository(application);
        allMovies = repository.getAllMovies();
        favoriteMovies = repository.getFavoriteMovies();
    }

    public void insert(Movie movie) {
        repository.insert(movie);
    }

    public void update(Movie movie) {
        repository.update(movie);
    }

    public void delete(Movie movie) {
        repository.delete(movie);
    }

    public void toggleFavorite(Movie movie) {
        movie.setFavorite(!movie.isFavorite());
        update(movie);
    }

    public LiveData<List<Movie>> getAllMovies() {
        return allMovies;
    }

    public LiveData<List<Movie>> getFavoriteMovies() {
        return favoriteMovies;
    }
}
