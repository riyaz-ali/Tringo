package com.iamriyaz.tringo;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;
import com.iamriyaz.tringo.data.Movie;
import com.iamriyaz.tringo.data.MovieDetail;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class DetailActivity extends AppCompatActivity {

  private static final String KEY_MOVIE_ID = "TMDB.MOVIE_ID";

  public static Intent intent(@NonNull Context context, @NonNull Movie movie){
    return new Intent(context, DetailActivity.class)
        .putExtra(KEY_MOVIE_ID, movie.getId());
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);

    ImageView backdrop = findViewById(R.id.movie_backdrop);
    ((AnimationDrawable) backdrop.getDrawable()).start();

    ImageView poster = findViewById(R.id.movie_poster);
    ((AnimationDrawable) poster.getDrawable()).start();

    long id = getIntent().getLongExtra(KEY_MOVIE_ID, -1L);
    if(-1 == id){
      finish();
      Toast.makeText(this, R.string.movie_invalid_id, Toast.LENGTH_SHORT).show();
      return;
    }

    Tringo.api(this)
        .getMovieById(id)
        .enqueue(new Callback<MovieDetail>() {
          @Override public void onResponse(@NonNull Call<MovieDetail> call,
              @NonNull Response<MovieDetail> response) {
            if(response.isSuccessful()){
              MovieDetail movie = Objects.requireNonNull(response.body());
              render(movie);
            } else {
              onFailure(call, new Exception("API error"));
            }
          }

          @Override public void onFailure(@NonNull Call<MovieDetail> call, @NonNull Throwable t) {

          }
        });
  }

  private void render(@NonNull MovieDetail movie){
    Timber.d(movie.toString());
  }
}
