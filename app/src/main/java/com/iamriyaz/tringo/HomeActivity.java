package com.iamriyaz.tringo;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.CheckedTextView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.iamriyaz.tringo.adapter.MovieAdapter;
import java.util.List;

import static com.iamriyaz.tringo.data.sources.MoviesDataSource.MODE_POPULAR;

/**
 * {@link Activity} class for the home screen
 *
 * The {@link android.arch.paging Android Support Paging} implementation is adapted from
 * https://medium.com/@anvith/paging-android-architecture-components-3134212b83bb
 */
public class HomeActivity extends AppCompatActivity {

  @BindViews({
      R.id.filter_popular_movies,
      R.id.filter_top_rated_movies,
      R.id.filter_now_playing,
      R.id.filter_upcoming
  }) List<CheckedTextView> filters;

  private CheckedTextView previousFilter = null;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    ButterKnife.bind(this);

    // set default filter
    setFilter(findViewById(R.id.filter_popular_movies));

    // setup recycler view
    RecyclerView recycler = findViewById(R.id.movies);
    recycler.setLayoutManager(new GridLayoutManager(this, 2));

    // prepare configuration
    PagedList.Config config = new PagedList.Config.Builder()
        .setPageSize(10)
        .setPrefetchDistance(5)
        .build();

    // prepare view model
    MovieViewModel vm = ViewModelProviders.of(this,
        new MovieViewModel.Factory(Tringo.api(this), MODE_POPULAR, config))
        .get(MovieViewModel.class);

    // create new movie adapter
    MovieAdapter adapter = new MovieAdapter(this);
    // add list to adapter
    vm.getMovies().observe(this, adapter::submitList);
    // bind to network changes
    vm.getLiveNetworkState().observe(this, adapter::setNetworkState);

    // finally, add adapter to recycler view
    recycler.setAdapter(adapter);
  }

  @OnClick({
      R.id.filter_popular_movies,
      R.id.filter_top_rated_movies,
      R.id.filter_now_playing,
      R.id.filter_upcoming
  }) public void setFilter(CheckedTextView selectedFilter){
    if(selectedFilter == previousFilter)
      return;
    if(previousFilter != null)
      previousFilter.setChecked(false);
    selectedFilter.setChecked(true);
    previousFilter = selectedFilter;
  }
}
