package com.example.gtc_movies_kayla_wagner.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import com.example.gtc_movies_kayla_wagner.data.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MovieRepository {
    private MovieDao movieDao;
    private LiveData<List<Movie>> allMovies;
    private LiveData<List<Movie>> favoriteMovies;
    private ExecutorService executorService;

    public MovieRepository(Application application) {
        MovieDatabase database = MovieDatabase.getInstance(application);
        movieDao = database.movieDao();
        allMovies = movieDao.getAllMovies();
        favoriteMovies = movieDao.getFavoriteMovies();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Movie movie) {
        executorService.execute(() -> movieDao.insert(movie));
    }

    public void update(Movie movie) {
        executorService.execute(() -> movieDao.update(movie));
    }

    public void delete(Movie movie) {
        executorService.execute(() -> movieDao.delete(movie));
    }

    public LiveData<List<Movie>> getAllMovies() {
        return allMovies;
    }

    public LiveData<List<Movie>> getFavoriteMovies() {
        return favoriteMovies;
    }
}