package com.example.rickmorty.Data;

import android.graphics.Bitmap;

import com.example.rickmorty.ImageTask;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class Character {
    @JsonProperty("id")
    private int id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("status")
    private String status;

    @JsonProperty("species")
    private String species;

    @JsonProperty("type")
    private String type;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("origin")
    private Origin origin;

    @JsonProperty("location")
    private Location location;

    @JsonProperty("image")
    private String imageUrl;

    @JsonProperty("episode")
    private String[] episode;

    @JsonProperty("url")
    private String url;

    @JsonProperty("created")
    private String created;

    private Bitmap image;
    private boolean like;

    public void downloadImage() throws ExecutionException, InterruptedException {
        image = new ImageTask().execute(imageUrl).get();
    }

    public void changeLike() {
        like = !like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getSpecies() {
        return species;
    }

    public String getType() {
        return type;
    }

    public String getGender() {
        return gender;
    }

    public Origin getOrigin() {
        return origin;
    }

    public Location getLocation() {
        return location;
    }

    public Bitmap getImage() {
        return image;
    }

    public boolean isLiked() {
        return like;
    }

    @Override
    public String toString() {
        return "Character.Character{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", species='" + species + '\'' +
                ", type='" + type + '\'' +
                ", gender='" + gender + '\'' +
                ", origin=" + origin +
                ", location=" + location +
                ", image='" + imageUrl + '\'' +
                ", episode=" + Arrays.toString(episode) +
                ", url='" + url + '\'' +
                ", created='" + created + '\'' +
                '}';
    }
}
