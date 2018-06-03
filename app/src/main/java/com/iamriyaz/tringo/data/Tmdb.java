package com.iamriyaz.tringo.data;

import android.support.annotation.NonNull;
import com.iamriyaz.tringo.StethoUtils;
import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
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

    // build retrofit
    Retrofit retrofit = new Retrofit.Builder()
        .client(okHttpClient.build())
        .baseUrl(base)
        .addConverterFactory(GsonConverterFactory.create())
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
}
