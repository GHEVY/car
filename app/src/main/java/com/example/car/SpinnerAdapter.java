package com.example.car;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;

public class SpinnerAdapter extends ArrayAdapter<String> {

    private final Context context;
    private  ArrayList<String> items;

    public SpinnerAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.context = context;
    }

    public void addAll(ArrayList<String> items) {
        super.addAll(items);
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false);
        }
        TextView textView = convertView.findViewById(R.id.text_view);
        textView.setText(items.get(position));

        if (position == 0) {
            textView.setBackground(getContext().getDrawable(R.drawable.footer));
        } else {
            textView.setBackground(getContext().getDrawable(R.drawable.spinner_menu));
        }

        return convertView;
    }

}
