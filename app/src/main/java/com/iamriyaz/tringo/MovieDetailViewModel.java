package com.iamriyaz.tringo;

import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
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

  // create new view model using api and movie id
  MovieDetailViewModel(@NonNull Tmdb.Api api, long id){
    fetchMovieTask = new FetchMovieTask(api);
    fetchMovieTask.execute(id);
  }

  // mark movie as favorited or not
  public void toggleFavorite(){
    // TODO: need to make a database call here
    // for now just update the favorite marker in MovieDetail
    MovieDetail movie = this.movie.getValue();
    if(null != movie){
      movie.setFavorited(!movie.isFavorited());
      favorite.setValue(movie.isFavorited());
    } // else movie is not yet loaded! do nothing
  }

  @Override protected void onCleared() {
    super.onCleared();

    // cancel fetching if we happen to leave before the load is complete
    if(null != fetchMovieTask && fetchMovieTask.getStatus() != AsyncTask.Status.FINISHED){
      fetchMovieTask.cancel(true);
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
        // TODO: make a database call to get the favorited status
        return requireNonNull(response.body());
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
}
