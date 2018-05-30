package com.iamriyaz.tringo;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import com.iamriyaz.tringo.data.Tmdb;
import timber.log.Timber;

/**
 * Tringo {@link Application} class
 *
 * Created on 30 May, 2018
 * @author Riyaz
 */
public final class Tringo extends Application {

  private Tmdb.Api service;

  @Override public void onCreate() {
    super.onCreate();

    if(BuildConfig.DEBUG){
      // plant a debug tree
      Timber.plant(new Timber.DebugTree());
    }
    // TODO: plant a tree for some remote logging service in production, if we happen to use one

    // create and cache API service
    this.service = Tmdb.create(BuildConfig.TMDB_API_URL, BuildConfig.TMDB_API_KEY);
  }

  @NonNull public static Tmdb.Api api(@NonNull Context context){
    return ((Tringo) context.getApplicationContext()).service;
  }
}
