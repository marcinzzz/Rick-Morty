package com.example.rickmorty;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.rickmorty.Data.Character;
import com.example.rickmorty.Data.Data;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private ListView charactersListView;
    private MyArrayAdapter charactersAdapter;
    private InfoDialog infoDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        charactersListView = findViewById(R.id.view_list);
        charactersListView.setOnItemClickListener((parent, view, position, id) -> {
            Character character = (Character) parent.getAdapter().getItem(position);
            infoDialog = new InfoDialog(this, character);
            infoDialog.show();
        });

        String json;
        try {
            json = new JsonTask().execute("https://rickandmortyapi.com/api/character/").get();

            ObjectMapper mapper = new ObjectMapper();
            Data data = mapper.readValue(json, Data.class);

            for (Character character : data.getCharacters())
                character.downloadImage();

            makeListView(data.getCharacters());
        } catch (JsonProcessingException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        Spinner statusSpinner = findViewById(R.id.spinner_status);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.status,
                android.R.layout.simple_spinner_item
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(spinnerAdapter);

        Button filterButton = findViewById(R.id.button_filter);
        filterButton.setOnClickListener((v) -> {
            List<Character> filtered = new LinkedList<>();
            for (Character character : charactersAdapter.getValues()) {
                if (character.getStatus().equals(statusSpinner.getSelectedItem()))
                    filtered.add(character);
            }
            Character[] characters = filtered.toArray(new Character[0]);
            makeListView(characters);
        });
    }

    private void makeListView(Character[] characters) {
        charactersAdapter = new MyArrayAdapter(this, characters);
        charactersListView.setAdapter(charactersAdapter);
    }
}