package com.iamriyaz.tringo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.paging.PagedList;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import com.iamriyaz.tringo.NetworkListener.State;
import com.iamriyaz.tringo.data.Tmdb;
import com.iamriyaz.tringo.data.Movie;
import com.iamriyaz.tringo.data.sources.MoviesDataSource;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * {@link ViewModel} to mediate interactions with {@link Tmdb.Api}
 *
 * Created on 03 Jun, 2018
 * @author Riyaz
 */
public final class MovieViewModel extends ViewModel {
  // tmdb api
  private Tmdb.Api api;
  // paging config
  private PagedList.Config config;

  // movie data source
  private MutableLiveData<PagedList<Movie>> movies = new MutableLiveData<>();

  // TMDb API mode
  private MutableLiveData<Integer> mode = new MutableLiveData<>();

  // live network state
  private MutableLiveData<State> network = new MutableLiveData<>();

  // network state listener
  private final NetworkListener networkListener = new NetworkListener() {
    @Override public void onNetworkStateChange(@NonNull State newState) {
      network.postValue(newState);
    }
  };

  @SuppressWarnings("ConstantConditions")
  private final Observer<Integer> modeChangeObserver = _mode -> {
    MoviesDataSource dataSource = new MoviesDataSource(api, _mode, networkListener);
    PagedList<Movie> list = new PagedList.Builder<>(dataSource, config)
        // get list notifications on Ui thread
        .setNotifyExecutor(new UiThreadExecutor())
        // execute fetches on a background thread
        .setFetchExecutor(new BackgroundThreadExecutor())
        // build!
        .build();
    movies.postValue(list);
  };

  MovieViewModel(@NonNull Tmdb.Api api, @NonNull PagedList.Config config) {
    this.api = api;
    this.config = config;
    this.mode.observeForever(modeChangeObserver);
  }

  public LiveData<PagedList<Movie>> getMovies(){
    return movies;
  }

  public LiveData<State> getLiveNetworkState(){
    return network;
  }

  public void changeMode(@MoviesDataSource.DataSourceMode int mode){
    this.mode.setValue(mode);
  }

  @Override protected void onCleared() {
    super.onCleared();
    this.mode.removeObserver(modeChangeObserver);
  }

  /**
   * Factory class for the view model
   */
  public static class Factory implements ViewModelProvider.Factory {
    private Tmdb.Api api;
    private PagedList.Config config;

    public Factory(@NonNull Tmdb.Api api, @NonNull PagedList.Config config){
      this.api = api;
      this.config = config;
    }

    @SuppressWarnings("unchecked") @NonNull @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
      return (T) new MovieViewModel(api, config);
    }
  }

  // Ui thread executor to execute runnable on UI thread
  private static class UiThreadExecutor implements Executor {
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override public void execute(@NonNull Runnable command) {
      handler.post(command);
    }
  }

  // Background thread executor to execute runnable on background thread
  private static class BackgroundThreadExecutor implements Executor {
    private ExecutorService executorService = Executors.newFixedThreadPool(2);

    @Override public void execute(@NonNull Runnable command) {
      executorService.execute(command);
    }
  }
}
