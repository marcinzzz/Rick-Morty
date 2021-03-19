package com.example.rickmorty.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

public class Data {
    @JsonProperty("info")
    Info info;

    @JsonProperty("results")
    Character[] characters;

    @Override
    public String toString() {
        return "Data{" +
                "characters=" + Arrays.toString(characters) +
                '}';
    }

    public Character[] getCharacters() {
        return characters;
    }
}
