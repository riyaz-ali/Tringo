package com.iamriyaz.tringo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.iamriyaz.tringo.R;
import com.iamriyaz.tringo.data.Movie;
import com.pascalwelsch.arrayadapter.ArrayAdapter;
import com.squareup.picasso.Picasso;
import java.util.List;

import static com.iamriyaz.tringo.Utils.aspect;
import static com.iamriyaz.tringo.Utils.calculateOtherDimension;
import static com.iamriyaz.tringo.Utils.createTmdbImageUrl;
import static java.util.Objects.requireNonNull;

/**
 * Created on 23 Jul, 2018
 *
 * @author Riyaz
 */
public class FavoriteMovieAdapter extends ArrayAdapter<Movie, FavoriteMovieAdapter.ViewHolder> {

  // Click listener interface
  public interface OnMovieClickListener {
    // called when the movie is clicked
    void onMovieClicked(@NonNull Movie movie, @NonNull View view);
  }

  // ViewHolder impl
  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ImageView poster;

    ViewHolder(View itemView) {
      super(itemView);
      poster = itemView.findViewById(R.id.movie_poster);
      poster.setOnClickListener(this);
    }

    void bind(@NonNull Movie movie) {
      // bind data to the view
      // set transition name for shared element transition
      ViewCompat.setTransitionName(poster, "" + movie.getId());

      // height can be made dynamic later to support high density devices
      Picasso.get()
          .load(createTmdbImageUrl(movie.getPoster(), 300,
              calculateOtherDimension(aspect(2, 3), 300)))
          .placeholder(R.drawable.loading_gradient_indicator)
          .into(poster);
    }

    @Override public void onClick(View v) {
      movieClickListener.onMovieClicked(requireNonNull(getItem(getAdapterPosition())), v);
    }
  }

  private LayoutInflater inflater;
  private OnMovieClickListener movieClickListener;

  public FavoriteMovieAdapter(@NonNull Context context, @NonNull OnMovieClickListener movieClickListener){
    this.inflater = LayoutInflater.from(context);
    this.movieClickListener = movieClickListener;
  }

  @Nullable @Override public Object getItemId(@NonNull Movie item) {
    return item.getId();
  }

  @NonNull @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new ViewHolder(inflater.inflate(R.layout.adapter_movie_item, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.bind(requireNonNull(getItem(position)));
  }

  public void replaceWith(@NonNull List<Movie> movies){
    clear();
    addAll(movies);
  }
}
