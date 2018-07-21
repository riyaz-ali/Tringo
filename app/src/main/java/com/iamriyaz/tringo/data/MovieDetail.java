package com.iamriyaz.tringo.data;

import com.google.gson.annotations.SerializedName;
import java.util.Date;
import java.util.List;

/**
 * Movie detail data class
 *
 * Created on 05 Jun, 2018
 * @author Riyaz
 */
public class MovieDetail {
  @SerializedName("id")
    private long id;
  @SerializedName("imdb_id")
    private String imdb;
  @SerializedName("poster_path")
    private String poster;
  @SerializedName("backdrop_path")
    private String backdrop;
  @SerializedName("original_title")
    private String title;
  @SerializedName("overview")
    private String overview;
  @SerializedName("runtime")
    private int runtime;
  @SerializedName("release_date")
    private Date release;
  @SerializedName("vote_average")
    private float rating;
  @SerializedName("reviews")
    private DumbResponse<Review> reviews;
  @SerializedName("videos")
    private DumbResponse<Video> videos;
  @SerializedName("casts")
    private DumbCastResponse cast;

  //------------------ GENERATED ------------------//

  public long getId() {
    return id;
  }

  public String getPoster() {
    return poster;
  }

  public String getBackdrop() {
    return backdrop;
  }

  public String getTitle() {
    return title;
  }

  public String getOverview() {
    return overview;
  }

  public int getRuntime() {
    return runtime;
  }

  public Date getRelease() {
    return release;
  }

  public float getRating() {
    return rating;
  }

  public String getImdb() {
    return imdb;
  }

  public List<Review> getReviews() {
    return reviews.results;
  }

  public List<Video> getVideos(){
    return videos.results;
  }

  public List<Cast> getCast(){
    return cast.results;
  }

  @Override public String toString() {
    return "MovieDetail{" +
        "id=" + id +
        ", imdb='" + imdb + '\'' +
        ", poster='" + poster + '\'' +
        ", backdrop='" + backdrop + '\'' +
        ", title='" + title + '\'' +
        ", overview='" + overview + '\'' +
        ", release=" + release +
        ", rating=" + rating +
        '}';
  }

  // placeholder class for envelop'ing response object
  private static class DumbResponse<T> {
    @SerializedName("results")
      List<T> results;
  }

  private static class DumbCastResponse {
    @SerializedName("cast")
      List<Cast> results;
  }
}
