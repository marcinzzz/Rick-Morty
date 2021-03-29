package com.example.rickmorty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rickmorty.Data.Character;
import com.example.rickmorty.Data.Data;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileInputStream;
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
    private boolean favourites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        favourites = false;

        charactersListView = findViewById(R.id.view_list);
        statusSpinner = findViewById(R.id.spinner_status);
        ImageButton buttonFavourites = findViewById(R.id.button_favourites);
        Button buttonMore = findViewById(R.id.button_more_characters);

        if (isConnected())
            importDataFromInternet();
        else
            importDataFromFile();

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.status,
                android.R.layout.simple_spinner_item
        );

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(spinnerAdapter);
        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Character[] filtered = filterStatus((String) statusSpinner.getSelectedItem());
                charactersListView.setAdapter(new MyArrayAdapter(getApplicationContext(), filtered));
                favourites = false;
                buttonFavourites.setImageResource(R.drawable.ic_star_outline);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        charactersListView.setOnItemClickListener((parent, view, position, id) -> {
            Character character = (Character) parent.getAdapter().getItem(position);
            infoDialog = new InfoDialog(this, character);
            infoDialog.show();
        });

        buttonFavourites.setOnClickListener((v) -> {
            favourites = !favourites;
            Character[] toShow;
            if (favourites) {
                toShow = filterLike();
                buttonFavourites.setImageResource(R.drawable.ic_star);
            } else {
                toShow = filterStatus((String) statusSpinner.getSelectedItem());
                buttonFavourites.setImageResource(R.drawable.ic_star_outline);
            }
            charactersListView.setAdapter(new MyArrayAdapter(getApplicationContext(), toShow));
        });

        buttonMore.setOnClickListener((v) -> {

        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        exportLikes();
    }

    private void importDataFromInternet() {
        try {
            String json = new JsonTask().execute(getResources().getString(R.string.url_characters)).get();

            ObjectMapper mapper = new ObjectMapper();
            Data data = mapper.readValue(json, Data.class);
            characters = data.getCharacters();

            for (Character character : characters) {
                character.downloadImage();
            }

            importLikes();
            charactersListView.setAdapter(new MyArrayAdapter(this, data.getCharacters()));

            ExportCharactersTask exportCharactersTask = new ExportCharactersTask(this);
            exportCharactersTask.execute(characters);
        } catch (ExecutionException | InterruptedException e) {
            Toast.makeText(
                    this,
                    getResources().getString(R.string.error_characters),
                    Toast.LENGTH_LONG
            ).show();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void importDataFromFile() {
        try {
            characters = new ImportCharactersTask(this).execute().get();
            importLikes();
            charactersListView.setAdapter(new MyArrayAdapter(this, characters));
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
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

    private Character[] filterLike() {
        List<Character> filtered = new LinkedList<>();
        for (Character character : characters) {
            if (character.isLiked())
                filtered.add(character);
        }
        return filtered.toArray(new Character[0]);
    }

    private void exportLikes() {
        StringBuilder builder = new StringBuilder();
        for (Character character : characters) {
            if (character.isLiked())
                builder.append(character.getId()).append(";");
        }

        if (builder.length() > 0)
            builder.deleteCharAt(builder.length() - 1);

        try {
            FileOutputStream fos = openFileOutput(
                    getResources().getString(R.string.file_favourites),
                    Context.MODE_PRIVATE
            );
            fos.write(builder.toString().getBytes());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void importLikes() {
        try {
            FileInputStream fis = openFileInput(getResources().getString(R.string.file_favourites));
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader reader = new BufferedReader(isr);
            String likes = reader.readLine();
            if (likes != null) {
                String[] likesArray = likes.split(";");
                for (String s : likesArray) {
                    int id = Integer.parseInt(s);
                    if (id - 1 < characters.length)
                        characters[id - 1].setLike(true);
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }

    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}