package com.iamriyaz.tringo;

import android.support.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to filter collections on older java versions
 * adapted from https://stackoverflow.com/a/122207/6611700
 *
 * Created on 07 Jul, 2018
 * @author Riyaz
 */
public class FilterUtils {

  // predicate to be implemented caller
  public interface IPredicate<T> {
    boolean apply(T obj);
  }

  // returns a filtered list as result
  @NonNull public static <T> List<T> filter(List<T> target, IPredicate<T> predicate){
    List<T> result = new ArrayList<>();
    for(T element : target)
      if(predicate.apply(element))
        result.add(element);
    return result;
  }
}
