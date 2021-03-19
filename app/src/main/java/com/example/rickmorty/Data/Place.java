package com.example.rickmorty.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Place {
    @JsonProperty("name")
    String name;

    @JsonProperty("url")
    String url;
}
