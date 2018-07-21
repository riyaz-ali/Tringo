package com.iamriyaz.tringo.data;

import com.google.gson.annotations.SerializedName;

/**
 * model POJO for a single cast/crew member object
 *
 * Created on 11 Jun, 2018
 * @author Riyaz
 */
public class Cast {
  @SerializedName("id")
    private long id;
  @SerializedName("character")
    private String character;
  @SerializedName("name")
    private String name;
  @SerializedName("profile")
    private String profile;
  @SerializedName("order")
    private int order;

  //------------------ GENERATED ------------------//

  public long getId() {
    return id;
  }

  public String getCharacter() {
    return character;
  }

  public String getName() {
    return name;
  }

  public String getProfile() {
    return profile;
  }

  public int getOrder() {
    return order;
  }

  @Override public String toString() {
    return "Cast{" +
        "id=" + id +
        ", character='" + character + '\'' +
        ", name='" + name + '\'' +
        ", profile='" + profile + '\'' +
        ", order=" + order +
        '}';
  }
}
