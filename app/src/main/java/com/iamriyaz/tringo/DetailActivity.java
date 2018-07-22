package com.iamriyaz.tringo;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.transition.ChangeBounds;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;
import com.iamriyaz.tringo.adapter.ReviewAdapter;
import com.iamriyaz.tringo.adapter.TrailerAdapter;
import com.iamriyaz.tringo.data.Movie;
import com.iamriyaz.tringo.data.MovieDetail;
import com.iamriyaz.tringo.data.Video;
import com.iamriyaz.tringo.databinding.ActivityDetailBinding;
import com.squareup.picasso.Picasso;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import static com.iamriyaz.tringo.Utils.aspect;
import static com.iamriyaz.tringo.Utils.calculateOtherDimension;
import static com.iamriyaz.tringo.Utils.createTmdbImageUrl;
import static com.iamriyaz.tringo.Utils.createTmdbShareUrl;

public class DetailActivity extends AppCompatActivity {

  // Keys
  public static final String KEY_MOVIE_ID = "TMDB.MOVIE_ID";
  public static final String KEY_TRANSITION_NAME = "TMDB.TRANSITION_NAME";

  private ActivityDetailBinding binding;

  private MovieDetail current;

  public static Intent intent(@NonNull Context context, @NonNull Movie movie){
    return new Intent(context, DetailActivity.class)
        .putExtra(KEY_MOVIE_ID, movie.getId());
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

    Toolbar toolbar = findViewById(R.id.movie_toolbar);
    setSupportActionBar(toolbar);
    ActionBar actionBar = getSupportActionBar();
    if(null != actionBar){
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setDisplayShowTitleEnabled(false);
    }

    ImageView backdrop = findViewById(R.id.movie_backdrop);
    ((AnimationDrawable) backdrop.getDrawable()).start();

    ImageView poster = findViewById(R.id.movie_poster);
    ((AnimationDrawable) poster.getDrawable()).start();

    // shared element transition on poster
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      if (null != getIntent().getStringExtra(KEY_TRANSITION_NAME)){
        poster.setTransitionName(getIntent().getStringExtra(KEY_TRANSITION_NAME));
      }
    }

    long id = getIntent().getLongExtra(KEY_MOVIE_ID, -1L);
    if(-1 == id){
      finish();
      Toast.makeText(this, R.string.movie_invalid_id, Toast.LENGTH_SHORT).show();
      return;
    }

    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
      enableTransitions();

    // get the view model instance
    MovieDetailViewModel vm =
        ViewModelProviders.of(this, new MovieDetailViewModel.Factory(Tringo.api(this), id))
            .get(MovieDetailViewModel.class);

    // ask for the movie
    vm.movie.observe(this, this::render);

    // setup favorite button
    LottieAnimationView av = findViewById(R.id.favorite);
   // av.setScale(0.9f);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.share_movie, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if(android.R.id.home == item.getItemId()) {
      onBackPressed();
      return true;
    } else if(R.id.menu_share_movie == item.getItemId() && null != current){
      Intent share = new Intent(Intent.ACTION_SEND);
      share.setType("text/plain");
      share.putExtra(Intent.EXTRA_TEXT, createTmdbShareUrl(current));

      Intent chooser = Intent.createChooser(share, getString(R.string.movie_share_via));
      if(null != chooser.resolveActivity(getPackageManager())){
        startActivity(chooser);
      }
      return true;
    } else {
      return super.onOptionsItemSelected(item);
    }
  }

  private void render(@NonNull MovieDetail movie){
    current = movie;
    binding.setMovie(movie);

    // load trailer thumbs
    List<Video> youtubeTrailers = FilterUtils.filter(movie.getVideos(), Video::isFromYouTube);
    TrailerAdapter adapter = new TrailerAdapter(youtubeTrailers, video -> {
      // open youtube link
      Intent launchYoutube = new Intent(Intent.ACTION_VIEW);
      launchYoutube.setData(Uri.parse(String.format("https://youtu.be/%s", video.getKey())));
      startActivity(launchYoutube);
    });
    RecyclerView recycler = findViewById(R.id.trailers);
    recycler.setAdapter(adapter);
    // provide some snappy behaviour to recycler
    new LinearSnapHelper().attachToRecyclerView(recycler);

    // load movie reviews
    RecyclerView trailerRecycler = findViewById(R.id.reviews);
    trailerRecycler.setAdapter(new ReviewAdapter(movie.getReviews()));
    // provide some snappy behaviour to recycler
    new LinearSnapHelper().attachToRecyclerView(trailerRecycler);

    // get Picasso
    Picasso picasso = Picasso.get();

    // load backdrop
    picasso.load(
        createTmdbImageUrl(movie.getBackdrop(), 224, calculateOtherDimension(aspect(4, 3), 224))
    ).placeholder(R.drawable.loading_gradient_indicator).into(binding.movieBackdrop);

    picasso.load(
        createTmdbImageUrl(movie.getPoster(), 130, calculateOtherDimension(aspect(11, 13), 130))
    ).placeholder(R.drawable.loading_gradient_indicator).into(binding.moviePoster);
  }

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP) private void enableTransitions(){
    ChangeBounds changeBounds = new ChangeBounds();
    changeBounds.setDuration(500);
    changeBounds.setInterpolator(new DecelerateInterpolator());
    getWindow().setSharedElementEnterTransition(changeBounds);
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
