package com.iamriyaz.tringo;

import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.iamriyaz.tringo.data.Movie;
import com.iamriyaz.tringo.data.MovieDetail;
import com.iamriyaz.tringo.databinding.ActivityDetailBinding;
import java.text.DateFormat;
import java.util.Date;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

  private static final String KEY_MOVIE_ID = "TMDB.MOVIE_ID";

  private ActivityDetailBinding binding;

  public static Intent intent(@NonNull Context context, @NonNull Movie movie){
    return new Intent(context, DetailActivity.class)
        .putExtra(KEY_MOVIE_ID, movie.getId());
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

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
    binding.setMovie(movie);
  }

  // DataBinding formatting utilities
  public static class FormatUtils {

    private static final DateFormat DATE_FORMAT = DateFormat.getDateInstance();

    @BindingAdapter("release")
    public static void setReleaseDate(@Nullable TextView view, Date release){
      if(null != view && release != null){
        view.setText(DATE_FORMAT.format(release));
      }
    }

    @BindingAdapter("runtime")
    public static void setRuntime(@Nullable TextView view, int duration) {
      if(null != view){
        final Context ctx = view.getContext();

        // get length of suffix
        int suffix = ctx.getString(R.string.movie_runtime_stub).length();
        // get formatted string
        String formatted = ctx.getString(R.string.movie_runtime, duration);
        // create spannable out of it
        SpannableString runtime = new SpannableString(formatted);

        // add size span
        runtime.setSpan(new RelativeSizeSpan(0.5F), formatted.length() - suffix, formatted.length(),
            Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        // add color span
        runtime.setSpan(new ForegroundColorSpan(Color.DKGRAY), formatted.length() - suffix, formatted.length(),
            Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        view.setText(runtime);
      }
    }

    @BindingAdapter("rating")
    public static void setRating(@Nullable TextView view, float rating){
      if(null != view){
        final Context ctx = view.getContext();

        // get length of suffix
        int suffix = ctx.getString(R.string.movie_rating_stub).length();
        // get formatted string
        String formatted = ctx.getString(R.string.movie_rating, rating);
        // create spannable out of it
        SpannableString ratingSpan = new SpannableString(formatted);

        // add size span
        // add size span
        ratingSpan.setSpan(new RelativeSizeSpan(0.5F), formatted.length() - suffix, formatted.length(),
            Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        // add color span
        ratingSpan.setSpan(new ForegroundColorSpan(Color.DKGRAY), formatted.length() - suffix, formatted.length(),
            Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        view.setText(ratingSpan);
      }
    }
  }
}
