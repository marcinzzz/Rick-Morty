package com.example.rickmorty;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.rickmorty.Data.Character;
import com.example.rickmorty.Data.Data;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String json;
        MyArrayAdapter arrayAdapter = null;
        try {
            json = new JsonTask().execute("https://rickandmortyapi.com/api/character/").get();
            ObjectMapper mapper = new ObjectMapper();
            Data data = mapper.readValue(json, Data.class);
            for (Character character : data.getCharacters())
                character.downloadImage();
            arrayAdapter = new MyArrayAdapter(this, data.getCharacters());
        } catch (JsonProcessingException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        if (arrayAdapter != null) {
            ListView listView = findViewById(R.id.view_list);
            listView.setAdapter(arrayAdapter);
            listView.setOnItemClickListener((parent, view, position, id) -> {

            });
        }
    }
}