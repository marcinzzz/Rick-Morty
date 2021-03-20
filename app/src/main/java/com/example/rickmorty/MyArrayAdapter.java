package com.example.rickmorty;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rickmorty.Data.Character;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class MyArrayAdapter extends ArrayAdapter<Character> {
    private final Context context;
    private Character[] values;

    public MyArrayAdapter(Context context, Character[] values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    public void filterStatus(String status) {
        List<Character> filtered = new LinkedList<>();
        for (Character character : values) {
            if (character.getStatus().equals(status))
                filtered.add(character);
        }
        values = filtered.toArray(new Character[0]);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        @SuppressLint("ViewHolder")
        View rowView = inflater.inflate(R.layout.list_item, parent, false);

        TextView textName = rowView.findViewById(R.id.label_name);
        textName.setText(values[position].getName());

        ImageView imageView = rowView.findViewById(R.id.view_image);
        imageView.setImageBitmap(values[position].getImage());

        return rowView;
    }

    public Character[] getValues() {
        return values;
    }
}
