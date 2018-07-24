package com.iamriyaz.tringo.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.v7.util.DiffUtil;
import android.text.TextUtils;
import com.google.gson.annotations.SerializedName;


/**
 * Movie data class
 *
 * Created on 04 Jun, 2018
 * @author Riyaz
 */
@Entity(
    tableName = "movies"
) public class Movie {

  // id of the movie
  @PrimaryKey
    private long id;

  // title of the movie
  @ColumnInfo(name = "title")
    private String title;

  // path to movie's poster
  @SerializedName("poster_path")
  @ColumnInfo(name = "poster")
    private String poster;

  // required empty constructor for gson and room
  public Movie(){
    // void
  }

  // constructor for building the movie object manually
  // must be ignored by Room
  @Ignore public Movie(long id, String title, String poster){
    this.id = id;
    this.title = title;
    this.poster = poster;
  }


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

  public void setId(long id) {
    this.id = id;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setPoster(String poster) {
    this.poster = poster;
  }

  @Override public String toString() {
    return "Movie{" +
        "id=" + id +
        ", title='" + title + '\'' +
        ", poster='" + poster + '\'' +
        '}';
  }
}