package com.iamriyaz.tringo;

import android.app.Application;
import timber.log.Timber;

/**
 * Tringo {@link Application} class
 *
 * Created on 30 May, 2018
 * @author Riyaz
 */
public final class Tringo extends Application {

  @Override public void onCreate() {
    super.onCreate();

    if(BuildConfig.DEBUG){
      // plant a debug tree
      Timber.plant(new Timber.DebugTree());
    }
    // TODO: plant a tree for some remote logging service in production, if we happen to use one
  }
}
