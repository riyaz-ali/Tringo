package com.iamriyaz.tringo.data;

import android.support.annotation.NonNull;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.iamriyaz.tringo.StethoUtils;
import java.io.IOException;
import java.util.List;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Class wrapping the complete retrofit implementation of the TMDB API
 *
 * Created on 30 May, 2018
 * @author Riyaz
 */
public final class Tmdb {

  /**
   * REST Service interface implemented by retrofit.
   */
  public interface Api {
    /**
     * Get top rated movies
     * @param page page number
     */
    @GET("movie/top_rated") Call<MovieResponse> getTopRatedMovies(@Query("page") int page);

    /**
     * Get popular movies
     * @param page page number
     */
    @GET("movie/popular") Call<MovieResponse> getPopularMovies(@Query("page") int page);

    /**
     * Get upcoming movies
     * @param page page number
     */
    @GET("movie/upcoming") Call<MovieResponse> getUpcomingMovies(@Query("page") int page);

    /**
     * Get now playing movies
     * @param page page number
     */
    @GET("movie/now_playing") Call<MovieResponse> getNowPlayingMovies(@Query("page") int page);

    /**
     * Get movie by id
     */
    @GET("movie/{id}") Call<MovieDetail> getMovieById(@Path("id") long id);
  }

  /**
   * Create an instance of the {@link Api API} service
   *
   * @param base base url
   * @param key api key
   */
  @NonNull public static Api create(@NonNull String base, @NonNull String key){
    // create a custom http client to inject api key
    OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder()
        .addInterceptor(new TmdbKeyInjectionInterceptor(key));

    // it injects stetho interceptor in debug build else does nothing!
    StethoUtils.inject(okHttpClient);

    // custom GSON instance with date format
    Gson gson = new GsonBuilder()
        .setDateFormat("yyyy-mm-dd")  // used for release date
        .create();

    // build retrofit
    Retrofit retrofit = new Retrofit.Builder()
        .client(okHttpClient.build())
        .baseUrl(base)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build();

    // create the service
    return retrofit.create(Api.class);
  }

  // OkHttp3 interceptor to inject api_key param into every request
  private static class TmdbKeyInjectionInterceptor implements Interceptor {
    private final String apiKey;

    private TmdbKeyInjectionInterceptor(String apiKey) {
      this.apiKey = apiKey;
    }

    @Override public Response intercept(Chain chain) throws IOException {
      // get the originals
      Request original = chain.request();
      HttpUrl originalUrl = original.url();
      // modify params
      HttpUrl newUrl = originalUrl.newBuilder()
          .addQueryParameter("api_key", apiKey)
          .build();
      Request newResuest = original.newBuilder().url(newUrl).build();
      // continue...
      return chain.proceed(newResuest);
    }
  }

  /**
   * TMDb movie response
   */
  public static class MovieResponse {
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
}
