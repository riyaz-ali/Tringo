package com.iamriyaz.tringo.data;

import android.support.v7.util.DiffUtil;
import android.text.TextUtils;
import com.google.gson.annotations.SerializedName;


/**
 * Movie data class
 *
 * Created on 04 Jun, 2018
 * @author Riyaz
 */
public class Movie {
  // id of the movie
  private long id;

  // title of the movie
  private String title;

  // path to movie's poster
  @SerializedName("poster_path")
  private String poster;

  // DiffCallback to assist Adapter
  public static final DiffUtil.ItemCallback<Movie> DIFF_CALLBACK = new DiffUtil.ItemCallback<Movie>() {
    @Override public boolean areItemsTheSame(Movie oldItem, Movie newItem) {
      return oldItem.id == newItem.id;
    }

    @Override public boolean areContentsTheSame(Movie oldItem, Movie newItem) {
      return TextUtils.equals(oldItem.poster, newItem.poster)
          && TextUtils.equals(oldItem.title, newItem.title);
    }
  };

  // ... other fields not taken into account as they are not used on the UI

  //---------------- GENERATED ---------------------//

  public long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getPoster() {
    return poster;
  }

  @Override public String toString() {
    return "Movie{" +
        "id=" + id +
        ", title='" + title + '\'' +
        ", poster='" + poster + '\'' +
        '}';
  }
}