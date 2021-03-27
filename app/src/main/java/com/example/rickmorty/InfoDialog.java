package com.example.rickmorty;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.rickmorty.Data.Character;

public class InfoDialog extends Dialog {
    private final Character character;

    public InfoDialog(@NonNull Context context, Character character) {
        super(context);
        this.character = character;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_info);

        ImageView characterImage = findViewById(R.id.image_character);
        characterImage.setImageBitmap(character.getBitmap());

        makeItem(R.id.item_name, "NAME", character.getName());
        makeItem(R.id.item_status, "STATUS", character.getStatus());
        makeItem(R.id.item_species, "SPECIES", character.getSpecies());
        makeItem(R.id.item_type, "TYPE", character.getType());
        makeItem(R.id.item_gender, "GENDER", character.getGender());
        makeItem(R.id.item_origin, "ORIGIN", character.getOrigin().getName());
        makeItem(R.id.item_location, "LOCATION", character.getLocation().getName());
    }

    private void makeItem(int viewId, String label, String value) {
        View view = findViewById(viewId);

        TextView textLabel = view.findViewById(R.id.label);
        textLabel.setText(label);

        TextView textValue = view.findViewById(R.id.value);
        textValue.setText(value);
    }
}
