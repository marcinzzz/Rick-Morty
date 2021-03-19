package com.example.rickmorty.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

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
    private String image;

    @JsonProperty("episode")
    private String[] episode;

    @JsonProperty("url")
    private String url;

    @JsonProperty("created")
    private String created;

    public String getName() {
        return name;
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
                ", image='" + image + '\'' +
                ", episode=" + Arrays.toString(episode) +
                ", url='" + url + '\'' +
                ", created='" + created + '\'' +
                '}';
    }
}
