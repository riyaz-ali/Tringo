package com.iamriyaz.tringo.data;

import com.google.gson.annotations.SerializedName;

/**
 * Movie (minimal) model for movie returned by /popular or /top_rated endpoints
 *
 * Created on 30 May, 2018
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
