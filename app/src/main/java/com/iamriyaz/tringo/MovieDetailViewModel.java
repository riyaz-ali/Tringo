package com.iamriyaz.tringo;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import com.iamriyaz.tringo.data.MovieDetail;
import com.iamriyaz.tringo.data.Tmdb;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created on 21 Jul, 2018
 *
 * @author Riyaz
 */
@SuppressWarnings("WeakerAccess")
public class MovieDetailViewModel extends ViewModel {

  // movie's data stream
  public final MutableLiveData<MovieDetail> movie = new MutableLiveData<>();

  MovieDetailViewModel(@NonNull Tmdb.Api api, long id){
    api.getMovieById(id)
        .enqueue(new Callback<MovieDetail>() {
          @Override public void onResponse(@NonNull Call<MovieDetail> call,
              @NonNull Response<MovieDetail> response) {
            if(response.isSuccessful()){
              MovieDetail mv = response.body();
              if(null != mv){
                movie.setValue(mv);
                return;
              }
            }

            onFailure(call, new Exception("API error"));
          }

          @Override public void onFailure(@NonNull Call<MovieDetail> call, @NonNull Throwable t) {
            Timber.e(t);
          }
        });
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
}
