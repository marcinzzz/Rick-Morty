package com.example.rickmorty;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.rickmorty.Data.Data;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String json = getJson("https://rickandmortyapi.com/api/character/");

        MyArrayAdapter arrayAdapter = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            Data data = mapper.readValue(json, Data.class);
            arrayAdapter = new MyArrayAdapter(this, data.getCharacters());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        if (arrayAdapter != null) {
            ListView listView = findViewById(R.id.view_list);
            listView.setAdapter(arrayAdapter);
            listView.setOnItemClickListener((parent, view, position, id) -> {

            });
        }
    }

    private String getJson(String url) {
        try {
            JsonGetter jsonGetter = new JsonGetter(url);
            Thread thread = new Thread(jsonGetter);
            thread.start();
            thread.join();
            return jsonGetter.getJson();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}