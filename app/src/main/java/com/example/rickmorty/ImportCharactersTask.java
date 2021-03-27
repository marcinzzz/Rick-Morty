package com.example.rickmorty;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.example.rickmorty.Data.Character;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ImportCharactersTask extends AsyncTask<Void, Void, Character[]> {
    private final AtomicReference<Context> context = new AtomicReference<>();

    public ImportCharactersTask(Context context) {
        this.context.set(context);
    }

    @Override
    protected Character[] doInBackground(Void... voids) {
        try {
            FileInputStream fis = context.get().openFileInput("characters");
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader reader = new BufferedReader(isr);
            String characters = reader.readLine();
            if (characters != null) {
                String[] ids = characters.split(";");
                List<Character> imported = new LinkedList<>();
                for (String s : ids)
                    imported.add(importCharacter(s));
                return imported.toArray(new Character[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Character importCharacter(String index) {
        try {
            FileInputStream fis = context.get().openFileInput("character_" + index);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader reader = new BufferedReader(isr);
            String data = reader.readLine();
            if (data != null) {
                Character c = new Character();
                c.set(data);
                FileInputStream img = context.get().openFileInput("image_" + index);
                Bitmap bitmap = BitmapFactory.decodeStream(img);
                c.setBitmap(bitmap);
                return c;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
