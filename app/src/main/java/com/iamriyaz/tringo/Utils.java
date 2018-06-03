package com.iamriyaz.tringo;

import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Created on 31 May, 2018
 *
 * @author Riyaz
 */
public final class Utils {
  // Google's image caching and resizing proxy
  private static final String GOOGLE_CACHE_BASE =
      "https://images1-focus-opensocial.googleusercontent.com/gadgets/proxy";

  private Utils(){
    throw new AssertionError("no instantiation!");
  }

  @NonNull private static String tmdbUrl(@NonNull String poster){
    return String.format("%s/original%s", BuildConfig.TMDB_IMAGE_BASE, poster);
  }

  /**
   * Calculate aspect
   */
  public static int aspect(int width, int height) {
    return height / width;
  }
  /**
   * Calculate the other dimension (either height or width) given one dimension and an aspect
   *
   * Use as: calculateOtherDimension(aspect(4, 3), 400) -> 300 // for a 4:3 aspect and one dimension=400 other is 300
   *
   * @param aspect aspect ratio
   * @param dimension measurement of one dimension
   */
  public static int calculateOtherDimension(int aspect, int dimension){
    return aspect * dimension;
  }

  @NonNull public static String createTmdbImageUrl(@NonNull String poster, int height, int width){
    return Uri.parse(GOOGLE_CACHE_BASE).buildUpon()
        .appendQueryParameter("container", "focus")
        .appendQueryParameter("resize_h", "" + height)
        .appendQueryParameter("resize_w", "" + width)
        .appendQueryParameter("url", tmdbUrl(poster))
        .toString();
  }
}
