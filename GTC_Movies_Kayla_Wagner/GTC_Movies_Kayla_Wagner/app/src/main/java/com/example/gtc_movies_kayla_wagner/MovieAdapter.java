package com.example.gtc_movies_kayla_wagner.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gtc_movies_kayla_wagner.R;
import com.example.gtc_movies_kayla_wagner.data.Movie;

public class MovieAdapter extends ListAdapter<Movie, MovieAdapter.MovieViewHolder> {
    private final OnMovieClickListener listener;

    public interface OnMovieClickListener {
        void onFavoriteClick(Movie movie);
    }

    public MovieAdapter(OnMovieClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<Movie> DIFF_CALLBACK = new DiffUtil.ItemCallback<Movie>() {
        @Override
        public boolean areItemsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                   oldItem.getDescription().equals(newItem.getDescription()) &&
                   oldItem.isFavorite() == newItem.isFavorite();
        }
    };

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = getItem(position);
        holder.bind(movie, listener);
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleView;
        private final TextView descriptionView;
        private final ImageButton favoriteButton;

        MovieViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.textTitle);
            descriptionView = itemView.findViewById(R.id.textDescription);
            favoriteButton = itemView.findViewById(R.id.buttonFavorite);
        }

        void bind(Movie movie, OnMovieClickListener listener) {
            titleView.setText(movie.getTitle());
            descriptionView.setText(movie.getDescription());
            favoriteButton.setImageResource(movie.isFavorite() ? 
                android.R.drawable.star_big_on : 
                android.R.drawable.star_big_off);
                
            favoriteButton.setOnClickListener(v -> listener.onFavoriteClick(movie));
        }
    }
}
