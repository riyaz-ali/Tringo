package com.iamriyaz.tringo;

import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.support.annotation.NonNull;
import com.iamriyaz.tringo.data.FavoriteDatabase;
import com.iamriyaz.tringo.data.Movie;
import com.iamriyaz.tringo.data.MovieDetail;
import com.iamriyaz.tringo.data.Tmdb;
import java.io.IOException;
import retrofit2.Response;
import timber.log.Timber;

import static java.util.Objects.requireNonNull;

/**
 * Created on 21 Jul, 2018
 *
 * @author Riyaz
 */
@SuppressWarnings("WeakerAccess")
public class MovieDetailViewModel extends ViewModel {

  // movie's data stream
  public final MutableLiveData<MovieDetail> movie = new MutableLiveData<>();
  // movie's favorite stream
  public final MutableLiveData<Boolean> favorite = new MutableLiveData<>();

  // async tasks
  private FetchMovieTask fetchMovieTask;
  private FavoriteToggleTask favoriteToggleTask;

  // create new view model using api and movie id
  MovieDetailViewModel(@NonNull Tmdb.Api api, long id){
    fetchMovieTask = new FetchMovieTask(api);
    fetchMovieTask.execute(id);
  }

  // mark movie as favorited or not
  public void toggleFavorite(){
    MovieDetail movie = this.movie.getValue();
    if(null != movie){
      if(null != favoriteToggleTask && favoriteToggleTask.getStatus() != Status.FINISHED){
        favoriteToggleTask.cancel(true);
      }
      favoriteToggleTask = new FavoriteToggleTask();
      favoriteToggleTask.execute(movie);
    } // else movie is not yet loaded! do nothing
  }

  @Override protected void onCleared() {
    super.onCleared();

    // cancel fetching if we happen to leave before the load is complete
    if(null != fetchMovieTask && fetchMovieTask.getStatus() != Status.FINISHED){
      fetchMovieTask.cancel(true);
    }
    if(null != favoriteToggleTask && favoriteToggleTask.getStatus() != Status.FINISHED){
      favoriteToggleTask.cancel(true);
    }
  }

  public static class Factory implements ViewModelProvider.Factory {
    private final Tmdb.Api api;
    private final long movie;

    public Factory(@NonNull Tmdb.Api api, long movie){
      this.api = api;
      this.movie = movie;
    }

    @NonNull @Override public <T extends ViewModel> T create(@NonNull Class<T> klass) {
      return klass.cast(new MovieDetailViewModel(api, movie));
    }
  }

  // Async Task to fetch the movie's details from the network and the disk
  @SuppressLint("StaticFieldLeak")
  private class FetchMovieTask extends AsyncTask<Long, Void, MovieDetail> {
    private Tmdb.Api api;

    FetchMovieTask(@NonNull Tmdb.Api api){
      this.api = api;
    }

    @Override protected MovieDetail doInBackground(Long... ids) {
      try {
        // get a response (synchronously)
        Response<MovieDetail> response = api.getMovieById(ids[0]).execute();
        // get the movie object
        MovieDetail movie = requireNonNull(response.body());
        // check in database if the movie is favorited and toggle status accordingly
        movie.setFavorited(
            // movie exists in database?
            FavoriteDatabase.get().movieDao().exists(movie.getId())
        );
        return movie;
      } catch (IOException ioe){
        Timber.e(ioe);
        return null;
      } catch (NullPointerException npe){
        Timber.e(npe);
        return null;
      }
    }

    @Override protected void onPostExecute(MovieDetail _movie) {
      super.onPostExecute(_movie);
      if(null != _movie)
        movie.setValue(_movie);
    }
  }

  // Async Task to mark/unmark movie as favorite
  @SuppressLint("StaticFieldLeak")
  private class FavoriteToggleTask extends AsyncTask<MovieDetail, Void, MovieDetail> {
    @Override protected MovieDetail doInBackground(MovieDetail... movies) {
      // exit early
      if(movies.length == 0)
        return null;

      // create an entity
      Movie _movie = new Movie(movies[0].getId(), movies[0].getTitle(), movies[0].getPoster());
      // toggle favorited status
      movies[0].setFavorited(!movies[0].isFavorited());
      // update in the database
      if(movies[0].isFavorited()){
        FavoriteDatabase.get().movieDao().insert(_movie);
      } else {
        FavoriteDatabase.get().movieDao().delete(_movie);
      }

      return movies[0];
    }

    @Override protected void onPostExecute(MovieDetail movieDetail) {
      super.onPostExecute(movieDetail);
      if(null != movieDetail){ // although it should always be non null
        favorite.setValue(movieDetail.isFavorited());
      }
    }
  }
}
