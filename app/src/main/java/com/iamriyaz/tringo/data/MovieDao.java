package com.iamriyaz.tringo.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;

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
}
