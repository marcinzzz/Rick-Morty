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

    public void set(String data) {
        String[] values = data.split(";");

        this.id = Integer.parseInt(values[0]);
        this.name = values[1];
        this.status = values[2];
        this.species = values[3];
        this.type = values[4];
        this.gender = values[5];
        this.origin = new Origin();
        this.origin.setName(values[6]);
        this.location = new Location();
        this.location.setName(values[7]);
        this.like = values[8].equals("1");
        this.image = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
    }

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
        return  id + ";" +
                name + ";" +
                status + ";" +
                species + ";" +
                type + ";" +
                gender + ";" +
                origin.getName() + ";" +
                location.getName() + ";" +
                (like ? "1" : "0");
    }
}
