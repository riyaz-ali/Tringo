package com.iamriyaz.tringo.data.sources;

import android.arch.paging.DataSource;
import android.arch.paging.PageKeyedDataSource;
import android.os.Handler;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import com.iamriyaz.tringo.data.Movie;
import com.iamriyaz.tringo.data.MovieResponse;
import com.iamriyaz.tringo.data.Tmdb;
import java.util.Collections;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static java.util.Objects.requireNonNull;

/**
 * {@link DataSource} for loading popular movies
 *
 * Created on 30 May, 2018
 * @author Riyaz
 */
public final class MoviesDataSource extends PageKeyedDataSource<Integer, Movie> {

  // constant max retry count
  private static final int MAX_RETRY = 3;

  // data source modes
  public static final int MODE_POPULAR = 0x1;
  public static final int MODE_TOP_RATED = 0x2;

  @IntDef({ MODE_POPULAR, MODE_TOP_RATED })
  public @interface DataSourceMode {}

  // data source's current mode
  private final int mode;
  // API client
  private final Tmdb.Api tmdbApi;

  public MoviesDataSource(@NonNull Tmdb.Api tmdbApi, @DataSourceMode int mode) {
    this.mode = mode;
    this.tmdbApi = requireNonNull(tmdbApi);
  }

  @Override public void loadInitial(@NonNull LoadInitialParams<Integer> params,
      @NonNull LoadInitialCallback<Integer, Movie> callback) {

    create(1 /* page */).enqueue(new CallbackWithRetry<MovieResponse>(MAX_RETRY) {
      @Override public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
        if (response.isSuccessful()) {
          MovieResponse movieResponse = requireNonNull(response.body());
          callback.onResult(movieResponse.getMovies(), null,
              movieResponse.getPage() + 1);
        } else {
          onFailure(call, new Exception("unknown error"));
        }
      }

      @Override public void onFinalFailure(Call<MovieResponse> call, Throwable t) {
        Timber.e(t, "Failed to get popular movies from API");
        callback.onResult(Collections.emptyList(), null, null);
      }
    });
  }

  @Override public void loadBefore(@NonNull LoadParams<Integer> params,
      @NonNull LoadCallback<Integer, Movie> callback) {

    create(params.key).enqueue(new CallbackWithRetry<MovieResponse>(MAX_RETRY) {
      @Override public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
        if (response.isSuccessful()) {
          MovieResponse movieResponse = requireNonNull(response.body());
          callback.onResult(movieResponse.getMovies(), movieResponse.getPage() + 1);
        } else {
          onFailure(call, new Exception("unknown error"));
        }
      }

      @Override public void onFinalFailure(Call<MovieResponse> call, Throwable t) {
        Timber.e(t, "Failed to get popular movies from API");
        callback.onResult(Collections.emptyList(), null);
      }
    });
  }

  @Override public void loadAfter(@NonNull LoadParams<Integer> params,
      @NonNull LoadCallback<Integer, Movie> callback) {

    create(params.key).enqueue(new CallbackWithRetry<MovieResponse>(MAX_RETRY) {
      @Override public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
        if(response.isSuccessful()){
          MovieResponse movieResponse = requireNonNull(response.body());
          callback.onResult(movieResponse.getMovies(), movieResponse.getPage() + 1);
        } else {
          onFailure(call, new Exception("unknown error"));
        }
      }

      @Override public void onFinalFailure(Call<MovieResponse> call, Throwable t) {
        Timber.e(t,"Failed to get popular movies from API");
        callback.onResult(Collections.emptyList(), null);
      }
    });
  }

  // helper to create api calls
  @NonNull private Call<MovieResponse> create(int page){
    if(mode == MODE_POPULAR)
      return tmdbApi.getPopularMovies(page);
    else
      return tmdbApi.getTopRatedMovies(page);
  }

  // retrofit callback implementation with retry policy
  // adapted from https://stackoverflow.com/a/41884400/6611700
  // adapted from https://gist.github.com/milechainsaw/811c1b583706da60417ed10d35d2808f (exponential delay)
  private abstract static class CallbackWithRetry<T> implements Callback<T> {
    // exponential backoff delay
    private static final int RETRY_DELAY = 300; /* ms */
    // max allowed retries
    private final int maxAttempts;
    // current attempts
    private int attempts = 0;

    CallbackWithRetry(int max){
      this.maxAttempts = max;
    }

    @Override public final void onFailure(Call<T> call, Throwable t) {
      attempts++;
      if(attempts < maxAttempts){
        int delay = (int) (RETRY_DELAY * Math.pow(2, Math.max(0, attempts - 1)));
        new Handler().postDelayed(() -> retry(call), delay);
      } else {
        onFinalFailure(call, t);
      }
    }

    // failure hook called when retries are consumed
    public abstract void onFinalFailure(Call<T> call, Throwable t);

    private void retry(Call<T> call){
      call.clone().enqueue(this);
    }
  }
}
