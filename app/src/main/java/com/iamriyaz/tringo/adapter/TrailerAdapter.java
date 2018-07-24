package com.iamriyaz.tringo.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.iamriyaz.tringo.R;
import com.iamriyaz.tringo.data.Video;
import com.pascalwelsch.arrayadapter.ArrayAdapter;
import com.squareup.picasso.Picasso;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Created on 07 Jul, 2018
 *
 * @author Riyaz
 */
public class TrailerAdapter extends ArrayAdapter<Video, TrailerAdapter.ViewHolder> {

  // view holder impl
  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private ImageView image;

    ViewHolder(View itemView) {
      super(itemView);
      image = itemView.findViewById(R.id.trailer_image);
      itemView.setOnClickListener(this);
    }

    // bind data item
    void bind(@NonNull Video video){
      Picasso.get()
          .load(getYoutubeThumbnailFor(video.getKey()))
          .placeholder(R.drawable.loading_gradient_indicator)
          .into(image);
    }

    @Override public void onClick(View v) {
      if(v.getId() == itemView.getId()){
        // notify handler
        clickListener.onClick(requireNonNull(getItem(getAdapterPosition())));
      }
    }
  }

  // listener interface
  public interface OnClickListener {
    // called when the trailer
    // thumbnail is clicked
    void onClick(@NonNull Video video);
  }

  // click listener
  private final OnClickListener clickListener;

  public TrailerAdapter(@NonNull List<Video> objects, @NonNull OnClickListener clickListener) {
    super(objects);
    this.clickListener = clickListener;
  }

  @NonNull @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new ViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.adapter_trailer_item, parent, false));
  }

  @Override public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.bind(requireNonNull(getItem(position)));
  }

  @Nullable @Override public Object getItemId(@NonNull Video item) {
    return item.getId();
  }

  // helper to generate youtube thumb urls
  @NonNull private static String getYoutubeThumbnailFor(@NonNull String video){
    return String.format("https://img.youtube.com/vi/%s/0.jpg", video);
  }
}
