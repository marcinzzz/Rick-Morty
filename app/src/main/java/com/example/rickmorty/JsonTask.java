package com.example.rickmorty;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class JsonTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... strings) {
        try {
            URL oracle = new URL(strings[0]);
            BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));
            String json = in.readLine();
            in.close();
            return json;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
