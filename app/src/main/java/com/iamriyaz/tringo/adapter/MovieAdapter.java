package com.iamriyaz.tringo.adapter;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.iamriyaz.tringo.R;
import com.iamriyaz.tringo.data.Tmdb;
import com.squareup.picasso.Picasso;

import static com.iamriyaz.tringo.Utils.aspect;
import static com.iamriyaz.tringo.Utils.calculateOtherDimension;
import static com.iamriyaz.tringo.Utils.createTmdbImageUrl;

/**
 * Created on 30 May, 2018
 *
 * @author Riyaz
 */
public class MovieAdapter extends PagedListAdapter<Tmdb.Movie, MovieAdapter.ViewHolder> {

  // ViewHolder implementation
  class ViewHolder extends RecyclerView.ViewHolder {
    ImageView poster;

    ViewHolder(View itemView) {
      super(itemView);
      poster = itemView.findViewById(R.id.movie_poster);
    }

    // bind data to the view
    void bind(@NonNull Tmdb.Movie movie) {
      // height can be made dynamic later to support high density devices
      Picasso.get()
          .load(createTmdbImageUrl(movie.getPoster(), 300,
              calculateOtherDimension(aspect(2, 3), 300)))
          .placeholder(R.drawable.placeholder)
          .into(poster);
    }
  }

  private final LayoutInflater inflater;

  /**
   * Create new movie adapter
   */
  public MovieAdapter(@NonNull Context context) {
    super(Tmdb.Movie.DIFF_CALLBACK);
    inflater = LayoutInflater.from(context);
  }

  @NonNull @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new ViewHolder(inflater.inflate(R.layout.adapter_movie_item, parent, false));
  }

  @Override public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    if(null != getItem(position)) //noinspection ConstantConditions
      holder.bind(getItem(position));
  }
}