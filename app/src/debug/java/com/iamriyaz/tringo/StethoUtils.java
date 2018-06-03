package com.iamriyaz.tringo;

import android.app.Application;
import android.support.annotation.NonNull;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import okhttp3.OkHttpClient;

/**
 * Created on 02 Jun, 2018
 *
 * @author Riyaz
 */
public class StethoUtils {

  public static void install(@NonNull Application application){
    Stetho.initializeWithDefaults(application);
  }

  public static void inject(@NonNull OkHttpClient.Builder clientBuilder){
    clientBuilder.addNetworkInterceptor(new StethoInterceptor());
  }
}
