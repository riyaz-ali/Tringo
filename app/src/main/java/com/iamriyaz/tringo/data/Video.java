package com.iamriyaz.tringo.data;

import android.text.TextUtils;
import com.google.gson.annotations.SerializedName;

/**
 * model POJO for a single trailer video
 *
 * Created on 11 Jun, 2018
 * @author Riyaz
 */
public class Video {

  public enum Type {
    @SerializedName("Trailer")
      TRAILER,
    @SerializedName("Teaser")
      TEASER,
    @SerializedName("Clip")
      CLIP,
    @SerializedName("Featurette")
      FEATURETTE
  }

  @SerializedName("id")
    private String id;
  @SerializedName("key")
    private String key;
  @SerializedName("site")
    private String site;
  @SerializedName("type")
    private Type type;

  public boolean isFromYouTube(){
    return TextUtils.equals("YouTube", site);
  }

  //------------------ GENERATED ------------------//

  public String getId() {
    return id;
  }

  public String getKey() {
    return key;
  }

  public String getSite() {
    return site;
  }

  public Type getType() {
    return type;
  }

  @Override public String toString() {
    return "Video{" +
        "id='" + id + '\'' +
        ", key='" + key + '\'' +
        ", site='" + site + '\'' +
        ", type=" + type +
        '}';
  }
}
