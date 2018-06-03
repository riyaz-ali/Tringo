package com.iamriyaz.tringo;

import android.app.Activity;
import android.arch.paging.PagedList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.iamriyaz.tringo.adapter.MovieAdapter;
import com.iamriyaz.tringo.data.Tmdb;
import com.iamriyaz.tringo.data.sources.MoviesDataSource;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.iamriyaz.tringo.data.sources.MoviesDataSource.MODE_POPULAR;

/**
 * {@link Activity} class for the home screen
 *
 * The {@link android.arch.paging Android Support Paging} implementation is adapted from
 * https://medium.com/@anvith/paging-android-architecture-components-3134212b83bb
 */
public class HomeActivity extends AppCompatActivity {

  // PagedListAdapter for paging movie data
  private MovieAdapter adapter = null;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    // setup recycler view
    RecyclerView recycler = findViewById(R.id.movies);
    recycler.setLayoutManager(new GridLayoutManager(this, 2));
    // create new movie adapter
    adapter = new MovieAdapter(this);
    // add adapter to recyclerview
    recycler.setAdapter(adapter);

    // trigger initial refresh
    onRefresh(MODE_POPULAR);
  }


  // internal helper to setup data source
  private void onRefresh(@MoviesDataSource.DataSourceMode int mode){
    // create data source
    MoviesDataSource dataSource = new MoviesDataSource(Tringo.api(this), mode);
    // create configuration
    PagedList.Config config = new PagedList.Config.Builder()
        .setPageSize(10)
        .setPrefetchDistance(5).build();
    // create paged list
    PagedList<Tmdb.Movie> pagedList = new PagedList.Builder<>(dataSource, config)
        // get list notifications on Ui thread
        .setNotifyExecutor(new UiThreadExecutor())
        // execute fetches on a background thread
        .setFetchExecutor(new BackgroundThreadExecutor())
        // build!
        .build();
    // update adapter's paged list (and data source)
    adapter.submitList(pagedList);
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
