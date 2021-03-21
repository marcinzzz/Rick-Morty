package com.example.rickmorty.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Place {
    @JsonProperty("name")
    String name;

    @JsonProperty("url")
    String url;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
