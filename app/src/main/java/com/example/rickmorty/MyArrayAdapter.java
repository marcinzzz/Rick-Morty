package com.example.rickmorty;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rickmorty.Data.Character;

public class MyArrayAdapter extends ArrayAdapter<Character> {
    private final Context context;
    private final Character[] values;

    public MyArrayAdapter(Context context, Character[] values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
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
        imageView.setImageBitmap(values[position].getBitmap());

        ImageButton imageButton = rowView.findViewById(R.id.button_like);
        setIcon(position, imageButton);
        imageButton.setOnClickListener((v) -> {
            values[position].changeLike();
            setIcon(position, imageButton);
        });

        return rowView;
    }

    private void setIcon(int position, ImageButton imageButton) {
        if (values[position].isLiked())
            imageButton.setImageResource(R.drawable.ic_star);
        else
            imageButton.setImageResource(R.drawable.ic_star_outline);
    }
}
