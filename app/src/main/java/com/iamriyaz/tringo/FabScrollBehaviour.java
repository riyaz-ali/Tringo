package com.iamriyaz.tringo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Scrolling behaviour for Floating Action Button taken from https://guides.codepath.com/android/floating-action-buttons
 *
 * @author Riyaz
 */
@SuppressWarnings("unused")
public class FabScrollBehaviour extends FloatingActionButton.Behavior {

  public FabScrollBehaviour(Context context, AttributeSet attrs) {
    super();
  }

  @Override public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
      @NonNull FloatingActionButton child, @NonNull View directTargetChild, @NonNull View target,
      int axes) {
    return axes == ViewCompat.SCROLL_AXIS_VERTICAL ||
        super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target,
            axes);
  }

  @Override public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
      @NonNull FloatingActionButton child, @NonNull View target, int dxConsumed, int dyConsumed,
      int dxUnconsumed, int dyUnconsumed) {
    super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed,
        dyUnconsumed);

    if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
      child.hide(new FloatingActionButton.OnVisibilityChangedListener() {
        @Override public void onHidden(FloatingActionButton fab) {
          super.onHidden(fab);
          // set fab's visibility to invisible instead of gone
          // bug fix for https://issuetracker.google.com/issues/37130108
          fab.setVisibility(View.INVISIBLE);
        }
      });
    } else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE) {
      child.show();
    }
  }
}
