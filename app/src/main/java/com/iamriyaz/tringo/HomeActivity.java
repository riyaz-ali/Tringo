package com.iamriyaz.tringo;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeBounds;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.CheckedTextView;
import android.widget.Toast;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.iamriyaz.tringo.adapter.MovieAdapter;
import com.iamriyaz.tringo.data.Movie;
import com.iamriyaz.tringo.data.sources.MoviesDataSource;
import java.util.List;

import static com.iamriyaz.tringo.data.sources.MoviesDataSource.MODE_NOW_PLAYING;
import static com.iamriyaz.tringo.data.sources.MoviesDataSource.MODE_POPULAR;
import static com.iamriyaz.tringo.data.sources.MoviesDataSource.MODE_TOP_RATED;
import static com.iamriyaz.tringo.data.sources.MoviesDataSource.MODE_UPCOMING;

/**
 * {@link Activity} class for the home screen
 *
 * The {@link android.arch.paging Android Support Paging} implementation is adapted from
 * https://medium.com/@anvith/paging-android-architecture-components-3134212b83bb
 */
public class HomeActivity extends AppCompatActivity implements MovieAdapter.OnClickListener {

  @BindViews({
      R.id.filter_popular_movies,
      R.id.filter_top_rated_movies,
      R.id.filter_now_playing,
      R.id.filter_upcoming
  }) List<CheckedTextView> filters;

  private CheckedTextView currentFilter = null;

  // ViewModel
  private MovieViewModel vm = null;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    ButterKnife.bind(this);

    // setup recycler view
    RecyclerView recycler = findViewById(R.id.movies);
    recycler.setLayoutManager(new GridLayoutManager(this, 2));

    // prepare configuration
    PagedList.Config config = new PagedList.Config.Builder()
        .setPageSize(10)
        .setPrefetchDistance(5)
        .build();

    // prepare view model
    vm = ViewModelProviders.of(this,
        new MovieViewModel.Factory(Tringo.api(this), config))
        .get(MovieViewModel.class);

    // create new movie adapter
    MovieAdapter adapter = new MovieAdapter(this, this);
    // add list to adapter
    vm.getMovies().observe(this, adapter::submitList);
    // bind to network changes
    vm.getLiveNetworkState().observe(this, adapter::setNetworkState);

    // finally, add adapter to recycler view
    recycler.setAdapter(adapter);

    // setup element transition
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
      enableTransitions();

    if(null == savedInstanceState) {
      // set default filter
      setFilter(findViewById(R.id.filter_popular_movies));
    } else {
      // restoring state!
      // set current filter to previously selected on
      int id = savedInstanceState.getInt("FILTER");
      for(CheckedTextView filter : filters)
        if(filter.getId() == id)
          currentFilter = filter;
    }
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt("FILTER", currentFilter.getId());
  }

  @OnClick({
      R.id.filter_popular_movies,
      R.id.filter_top_rated_movies,
      R.id.filter_now_playing,
      R.id.filter_upcoming
  }) public void setFilter(CheckedTextView selectedFilter){
    if(selectedFilter == currentFilter)
      return;
    if(currentFilter != null)
      currentFilter.setChecked(false);
    selectedFilter.setChecked(true);
    currentFilter = selectedFilter;

    // change mode
    vm.changeMode(getModeFor(currentFilter));
  }

  @MoviesDataSource.DataSourceMode private int getModeFor(CheckedTextView filter){
    final int id = filter.getId();
    if(R.id.filter_popular_movies == id)
      return MODE_POPULAR;
    else if(R.id.filter_top_rated_movies == id)
      return MODE_TOP_RATED;
    else if(R.id.filter_now_playing == id)
      return MODE_NOW_PLAYING;
    else if(R.id.filter_upcoming == id)
      return MODE_UPCOMING;
    else
      throw new IllegalArgumentException("unknown filter");
  }

  @Override public void onClick(@NonNull Movie movie, @NonNull View view) {
    Intent intent = DetailActivity.intent(this, movie);
    intent.putExtra(DetailActivity.KEY_TRANSITION_NAME, ViewCompat.getTransitionName(view));

    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view,
        ViewCompat.getTransitionName(view));
    startActivity(intent, options.toBundle());
  }

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP) private void enableTransitions(){
    ChangeBounds changeBounds = new ChangeBounds();
    changeBounds.setDuration(500);
    changeBounds.setInterpolator(new AccelerateInterpolator());
    getWindow().setSharedElementExitTransition(changeBounds);
  }

  @OnClick(R.id.favorites) public void onShowFavorite(FloatingActionButton fab){
    // TODO: open the favorite listing view
    Toast.makeText(this, "todo", Toast.LENGTH_SHORT).show();
  }
}
