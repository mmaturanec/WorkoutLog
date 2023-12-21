package com.example.workoutlog;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class PrijedloziTreningaFragment extends Fragment {

    FloatingActionButton btnAddTemplate;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_prijedlozi_treninga, container, false);

        btnAddTemplate = view.findViewById(R.id.btnAddTemplate);

        btnAddTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentNewTemplateMain = new Intent(getActivity(), NewTemplateMain.class);
                startActivity(intentNewTemplateMain);

            }
        });
        return view;
    }
}