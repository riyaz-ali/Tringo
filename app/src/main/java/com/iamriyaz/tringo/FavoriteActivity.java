package com.iamriyaz.tringo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.iamriyaz.tringo.adapter.FavoriteMovieAdapter;
import com.iamriyaz.tringo.data.FavoriteDatabase;
import com.iamriyaz.tringo.data.Movie;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity
    implements FavoriteMovieAdapter.OnMovieClickListener {

  @BindView(R.id.movies)
    RecyclerView movies;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_favorite);

    ButterKnife.bind(this);

    Toolbar toolbar = findViewById(R.id.toolbar);

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
    getSupportActionBar().setTitle(R.string.favorites);

    FavoriteMovieAdapter adapter = new FavoriteMovieAdapter(this, this);
    movies.setAdapter(adapter);
    movies.setLayoutManager(new GridLayoutManager(this, 2));

    // initiate view model
    ViewModelProviders.of(this).get(FavoriteViewModel.class)
        .favorites.observe(this, adapter::replaceWith);
  }

  @Override public void onMovieClicked(@NonNull Movie movie, @NonNull View view) {
    Intent intent = DetailActivity.intent(this, movie);
    intent.putExtra(DetailActivity.KEY_TRANSITION_NAME, ViewCompat.getTransitionName(view));

    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view,
        ViewCompat.getTransitionName(view));
    startActivity(intent, options.toBundle());
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if(android.R.id.home == item.getItemId()){
      onBackPressed();
      return true;
    } else {
      return super.onOptionsItemSelected(item);
    }
  }

  // nested ViewModel implementation
  // this impl is so small and used only here that I found it redundant to
  // add it in a separate file
  public static class FavoriteViewModel extends ViewModel {
    public final LiveData<List<Movie>> favorites = FavoriteDatabase.get().movieDao().all();
  }
}
