package com.example.rickmorty;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.example.rickmorty.Data.Character;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static android.content.Context.MODE_PRIVATE;

public class ExportCharactersTask extends AsyncTask<Character, Void, Void> {
    private final AtomicReference<Context> context = new AtomicReference<>();

    public ExportCharactersTask(Context context) {
        this.context.set(context);
    }

    @Override
    protected Void doInBackground(Character... characters) {
        StringBuilder fileNames = new StringBuilder();
        for (Character c : characters) {
            fileNames.append(c.getId()).append(";");
            exportCharacter(c);
        }
        fileNames.deleteCharAt(fileNames.length() - 1);

        try {
            FileOutputStream fos = context.get().openFileOutput(
                    context.get().getResources().getString(R.string.file_characters),
                    MODE_PRIVATE
            );
            fos.write(new String(fileNames).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void exportCharacter(Character character) {
        int id = character.getId();
        try {
            FileOutputStream fos = context.get().openFileOutput(
                    context.get().getResources().getString(R.string.file_character) + id,
                    MODE_PRIVATE
            );
            fos.write(character.toString().getBytes());
            FileOutputStream img = context.get().openFileOutput(
                    context.get().getResources().getString(R.string.file_image) + id,
                    MODE_PRIVATE
            );
            character.getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, img);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
