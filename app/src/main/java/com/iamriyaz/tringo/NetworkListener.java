package com.iamriyaz.tringo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Listener interface to be implemented by classes interested in receiving network state callbacks
 *
 * Created on 03 Jun, 2018
 * @author Riyaz
 */
public abstract class NetworkListener {

  // enumeration of network status
  public enum Status {
    RUNNING,
    SUCCESS,
    FAILED
  }

  public static class State {
    public final Status status;
    public final String message;

    State(Status status, @Nullable String message){
      this.status = status;
      this.message = message;
    }

    State(Status status){
      this(status, null);
    }

    @Override public String toString() {
      return "State{" +
          "status=" + status +
          ", message='" + message + '\'' +
          '}';
    }
  }

  // predefined network states
  // - Loading state
  public static final State LOADING = new State(Status.RUNNING);

  // - Loaded state
  public static final State LOADED = new State(Status.SUCCESS);

  // - Failed state
  public static State error(@Nullable String message){
    return new State(Status.FAILED, message);
  }

  /**
   * Called by the parent to notify implementor about a change in state
   *
   * @param newState new state the network has transitioned into
   */
  public abstract void onNetworkStateChange(@NonNull State newState);
}
