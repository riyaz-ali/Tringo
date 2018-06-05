package com.iamriyaz.tringo.data;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

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
}
