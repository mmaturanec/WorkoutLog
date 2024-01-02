package com.example.workoutlog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class PostavljanjeProfilaActivity extends AppCompatActivity {

    private ImageView ivOdaberiProfilnu, ivAddOdaberiProfilnu;
    private EditText etOdaberiIme, etOdaberiPrezime;
    private TextView tvOdaberiProfilnu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postavljanje_profila);

        ivOdaberiProfilnu = findViewById(R.id.ivOdaberiProfilnu);
        ivAddOdaberiProfilnu = findViewById(R.id.ivAddOdaberiProfilnu);
        etOdaberiIme = findViewById(R.id.etOdaberiIme);
        etOdaberiPrezime = findViewById(R.id.etOdaberiPrezime);
        tvOdaberiProfilnu = findViewById(R.id.tvOdaberiProfilnu);


    }
}