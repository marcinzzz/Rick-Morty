package com.example.rickmorty.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Info {
    @JsonProperty("count")
    int count;

    @JsonProperty("pages")
    int pages;

    @JsonProperty("next")
    String next;

    @JsonProperty("prev")
    String prev;
}
