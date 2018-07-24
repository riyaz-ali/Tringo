package com.iamriyaz.tringo.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import java.util.List;

/**
 * Created on 23 Jul, 2018
 *
 * @author Riyaz
 */
@Dao public interface MovieDao {
  /**
   * Insert a movie into the favorite database
   *
   * @param movie The movie to insert
   * @return id of the new row
   */
  @Insert long insert(Movie movie);

  /**
   * Remove the movie from the favorite database
   *
   * @param movie movie to remove
   */
  @Delete void delete(Movie movie);

  /**
   * Query database to check if the movie exists in it
   *
   * @param movie id of the movie to check
   * @return true if movie exists (i.e. is favorited) else false
   */
  @Query("SELECT EXISTS(SELECT id FROM movies WHERE id=:movie)")
    boolean exists(long movie);

  /**
   * Get a {@link List} of all favorited {@link Movie}
   *
   * @return list of all favorited movies
   */
  @Query("SELECT * FROM movies") LiveData<List<Movie>> all();
}
