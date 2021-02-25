package com.example.myapplication.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.example.myapplication.R;

import java.io.File;
import java.util.Arrays;

public class CustomAdaptor extends ArrayAdapter<String> {

    Integer [] vectores;

    public CustomAdaptor(Context context, String[] values, File[] files) {
        super(context, R.layout.main_fragment, values);
        if (files.length==1) {
            File [] f = new File[1];
            f[0] = new File("infoadaptor");
            if (Arrays.equals(files, f)) {
                vectores = new Integer[5];
                vectores[0] = R.drawable.name;
                vectores[1] = R.drawable.path;
                vectores[2] = R.drawable.size;
                vectores[3] = R.drawable.read;
                vectores[4] = R.drawable.write;
            }
            else {
                vectores = new Integer[values.length];
                for (int i = 0; i < values.length; i++) {
                    if (files[i].isDirectory()) {
                        vectores[i] = R.drawable.folder;
                    } else {
                        vectores[i] = R.drawable.file;
                    }
                }
            }
        }
        else {
            vectores = new Integer[values.length];
            for (int i = 0; i < values.length; i++) {
                if (files[i].isDirectory()) {
                    vectores[i] = R.drawable.folder;
                } else {
                    vectores[i] = R.drawable.file;
                }
            }
        }
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        String s = getItem(position);



        @SuppressLint("ViewHolder") View theView = LayoutInflater.from(getContext()).inflate(R.layout.customadaptorlayout, parent, false);
        TextView text = theView.findViewById(R.id.textView2);
        assert s != null;
        if (s.length() > 10) {
            s = s.substring(0, Math.min(s.length(), 10));
            s = s + " ....";
        }
        text.setText(s);
        ImageView image = theView.findViewById(R.id.imageView);

        image.setImageResource(vectores[position]);
        return theView;
    }
}
