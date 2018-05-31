package com.iamriyaz.tringo.adapter;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.iamriyaz.tringo.R;
import com.iamriyaz.tringo.data.Movie;

/**
 * Created on 30 May, 2018
 *
 * @author Riyaz
 */
public class MovieAdapter extends PagedListAdapter<Movie, MovieAdapter.ViewHolder> {

  // ViewHolder implementation
  static class ViewHolder extends RecyclerView.ViewHolder {
    TextView title;

    ViewHolder(View itemView) {
      super(itemView);
      title = itemView.findViewById(R.id.movie_title);
    }

    // bind data to the view
    void bind(@NonNull Movie movie){
      title.setText(movie.getTitle());
    }
  }

  private final LayoutInflater inflater;

  /**
   * Create new movie adapter
   */
  public MovieAdapter(@NonNull Context context, @NonNull DiffUtil.ItemCallback<Movie> diffCallback) {
    super(diffCallback);
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