package com.iamriyaz.tringo;

import android.support.annotation.NonNull;

/**
 * Created on 31 May, 2018
 *
 * @author Riyaz
 */
public final class Utils {

  private Utils(){
    throw new AssertionError("no instatiation!");
  }

  @NonNull public static String createTmdbImageUrl(@NonNull String poster, @NonNull String size){
    return String.format("%s/%s%s", BuildConfig.TMDB_IMAGE_BASE, size, poster);
  }
}
