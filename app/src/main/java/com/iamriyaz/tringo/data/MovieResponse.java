package com.iamriyaz.tringo.data;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created on 30 May, 2018
 *
 * @author Riyaz
 */
public class MovieResponse {
  // page number of this response
  private int page;

  // list of movies
  @SerializedName("results")
  private List<Movie> movies;


  //---------------- GENERATED ---------------------//

  public int getPage() {
    return page;
  }

  public List<Movie> getMovies() {
    return movies;
  }

  @Override public String toString() {
    return "MovieResponse{" +
        "page=" + page +
        ", movies=" + movies +
        '}';
  }
}
