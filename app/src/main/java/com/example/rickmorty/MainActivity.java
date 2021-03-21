package com.example.rickmorty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rickmorty.Data.Character;
import com.example.rickmorty.Data.Data;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private ListView charactersListView;
    private Spinner statusSpinner;
    private InfoDialog infoDialog;
    private Character[] characters;

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

        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        String json = null;
        Data data = null;
        if (isConnected) {
            try {
                json = new JsonTask().execute("https://rickandmortyapi.com/api/character/").get();
            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(
                        this,
                        "SOMETHING WENT WRONG, COULDN'T GET CHARACTERS",
                        Toast.LENGTH_LONG
                ).show();
            }

            if (json != null) {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    data = mapper.readValue(json, Data.class);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(
                        this,
                        "SOMETHING WENT WRONG, COULDN'T GET CHARACTERS",
                        Toast.LENGTH_LONG
                ).show();
            }

            if (data != null) {
                characters = data.getCharacters();
                try {
                    for (Character character : characters) {
                        character.setLike(false);
                        character.downloadImage();
                    }
                    charactersListView.setAdapter(new MyArrayAdapter(this, data.getCharacters()));
                } catch (ExecutionException | InterruptedException e) {
                    Toast.makeText(
                            this,
                            "SOMETHING WENT WRONG, COULDN'T GET IMAGES",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }
        } else {
            importCharacters();
            charactersListView.setAdapter(new MyArrayAdapter(this, characters));
        }

        statusSpinner = findViewById(R.id.spinner_status);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.status,
                android.R.layout.simple_spinner_item
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(spinnerAdapter);

        Button filterButton = findViewById(R.id.button_filter);
        filterButton.setOnClickListener((v) -> {
            Character[] filtered = filterStatus((String) statusSpinner.getSelectedItem());
            charactersListView.setAdapter(new MyArrayAdapter(this, filtered));
        });
    }

    private Character[] filterStatus(String status) {
        if (status.equals(getResources().getStringArray(R.array.status)[0]))
            return characters;

        List<Character> filtered = new LinkedList<>();
        for (Character character : characters) {
            if (character.getStatus().equals(status))
                filtered.add(character);
        }
        return filtered.toArray(new Character[0]);
    }

    private void exportCharacters() {
        StringBuilder fileNames = new StringBuilder();
        for (Character c : characters) {
            fileNames.append(c.getId()).append(";");
            exportCharacter(c);
        }
        fileNames.deleteCharAt(fileNames.length() - 1);

        try {
            FileOutputStream fos = openFileOutput("characters", MODE_PRIVATE);
            fos.write(new String(fileNames).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exportCharacter(Character character) {
        try {
            FileOutputStream fos = openFileOutput("character_" + character.getId(), Context.MODE_PRIVATE);
            fos.write(character.toString().getBytes());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void importCharacters() {
        try {
            FileInputStream fis = openFileInput("characters");
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader reader = new BufferedReader(isr);
            String characters = reader.readLine();
            if (characters != null) {
                String[] ids = characters.split(";");
                List<Character> imported = new LinkedList<>();
                for (String s : ids)
                    imported.add(importCharacter("character_" + s));
                this.characters = imported.toArray(new Character[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Character importCharacter(String fileName) {
        try {
            FileInputStream fis = openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader reader = new BufferedReader(isr);
            String data = reader.readLine();
            if (data != null) {
                Character c = new Character();
                c.set(data);
                return c;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onStop() {
        super.onStop();

        exportCharacters();
    }
}