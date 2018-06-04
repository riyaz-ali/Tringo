package com.iamriyaz.tringo.adapter;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.iamriyaz.tringo.NetworkListener;
import com.iamriyaz.tringo.R;
import com.iamriyaz.tringo.data.Movie;
import com.squareup.picasso.Picasso;

import static com.iamriyaz.tringo.Utils.aspect;
import static com.iamriyaz.tringo.Utils.calculateOtherDimension;
import static com.iamriyaz.tringo.Utils.createTmdbImageUrl;
import static java.util.Objects.requireNonNull;

/**
 * Created on 30 May, 2018
 *
 * adapted from https://is.gd/Kshqfr
 * @author Riyaz
 */
public class MovieAdapter extends PagedListAdapter<Movie, ViewHolder> {

  // ViewHolder implementation for Movie
  class MovieViewHolder extends ViewHolder {
    ImageView poster;

    MovieViewHolder(View itemView) {
      super(itemView);
      poster = itemView.findViewById(R.id.movie_poster);
    }

    // bind data to the view
    void bind(@NonNull Movie movie) {
      // height can be made dynamic later to support high density devices
      Picasso.get()
          .load(createTmdbImageUrl(movie.getPoster(), 300,
              calculateOtherDimension(aspect(2, 3), 300)))
          .placeholder(R.drawable.placeholder)
          .into(poster);
    }
  }

  // ViewHolder implementation for network
  class NetworkViewHolder extends ViewHolder {
    public NetworkViewHolder(View itemView) {
      super(itemView);
    }

    void bind(@Nullable NetworkListener.State state){

    }
  }

  // layout inflater
  private final LayoutInflater inflater;

  // state of network
  private NetworkListener.State networkState = null;

  /**
   * Create new movie adapter
   */
  public MovieAdapter(@NonNull Context context) {
    super(Movie.DIFF_CALLBACK);
    inflater = LayoutInflater.from(context);
  }

  @NonNull @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    if(R.layout.adapter_network_item == viewType)
      return new NetworkViewHolder(inflater.inflate(R.layout.adapter_network_item, parent, false));
    else if(R.layout.adapter_movie_item == viewType)
      return new MovieViewHolder(inflater.inflate(R.layout.adapter_movie_item, parent, false));
    else
      throw new IllegalArgumentException("unknown view type");
  }

  @Override public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    if(R.layout.adapter_network_item == getItemViewType(position))
      ((NetworkViewHolder) holder).bind(networkState);
    else
      ((MovieViewHolder) holder).bind(requireNonNull(getItem(position)));
  }

  @Override public int getItemCount() {
    return super.getItemCount() + (hasExtraRow()? 1 : 0);
  }

  @Override public int getItemViewType(int position) {
    if(hasExtraRow() && position == getItemCount() - 1)
      return R.layout.adapter_network_item;
    else
      return R.layout.adapter_movie_item;
  }

  /**
   * Change network state which is then reflected in the UI
   *
   * @param current new network state
   */
  public void setNetworkState(@NonNull NetworkListener.State current){
    NetworkListener.State previous = this.networkState;
    boolean hadExtraRow = hasExtraRow();
    this.networkState = current;
    boolean hasExtraRow = hasExtraRow();
    if(hadExtraRow != hasExtraRow){
      if(hadExtraRow)
        notifyItemRemoved(super.getItemCount());
      else
        notifyItemInserted(super.getItemCount());
    } else if(hasExtraRow && previous != current) {
      notifyItemChanged(getItemCount() - 1);
    }
  }

  // Got an extra row?
  private boolean hasExtraRow(){
    return networkState != null && networkState != NetworkListener.LOADED;
  }
}