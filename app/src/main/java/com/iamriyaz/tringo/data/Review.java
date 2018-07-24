package com.iamriyaz.tringo.data;

import com.google.gson.annotations.SerializedName;

/**
 * Model POJO for a single review object
 *
 * Created on 11 Jun, 2018
 * @author Riyaz
 */
public class Review {
  @SerializedName("id")
    private String id;
  @SerializedName("author")
    private String author;
  @SerializedName("content")
    private String content;

  //------------------ GENERATED ------------------//

  public String getId() {
    return id;
  }

  public String getAuthor() {
    return author;
  }

  public String getContent() {
    return content;
  }

  @Override public String toString() {
    return "Review{" +
        "id='" + id + '\'' +
        ", author='" + author + '\'' +
        ", content='" + content + '\'' +
        '}';
  }
}
