package com.iamriyaz.tringo;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.iamriyaz.tringo.data.Movie;

public class DetailActivity extends AppCompatActivity {

  public static Intent intent(@NonNull Context context, @NonNull Movie movie){
    return new Intent(context, DetailActivity.class);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);
  }
}
