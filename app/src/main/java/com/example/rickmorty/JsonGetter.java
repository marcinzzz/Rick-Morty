package com.example.rickmorty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class JsonGetter implements Runnable {
    private String json;
    private final String url;

    public JsonGetter(String url) {
        this.url = url;
    }

    @Override
    public void run() {
        try {
            URL oracle = new URL(url);
            BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));
            this.json = in.readLine();
            in.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public String getJson() {
        return json;
    }
}
