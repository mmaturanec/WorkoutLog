package com.example.workoutlog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    private Button btnOdjava, btnUrediProfil;
    private ImageView ivProfile, ivBackSettings;
    private TextView tvPromjeniProfilnu, tvime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnOdjava = findViewById(R.id.btnOdjava);
        btnUrediProfil = findViewById(R.id.btnUrediProfil);
        ivProfile = findViewById(R.id.ivProfile);
        ivBackSettings = findViewById(R.id.ivBackSettings);
        tvPromjeniProfilnu = findViewById(R.id.tvPromjeniProfilnu);
        tvime = findViewById(R.id.tvime);


        btnUrediProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentUrediProfil = new Intent(SettingsActivity.this, UrediProfilActivity.class);
                startActivity(intentUrediProfil);
                overridePendingTransition(0, 0);
            }
        });
        tvPromjeniProfilnu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentKamera = new Intent(SettingsActivity.this, KameraActivity.class);
                startActivity(intentKamera);
                overridePendingTransition(0, 0);
            }
        });

        ivBackSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMainMenu = new Intent(SettingsActivity.this, MainMenuActivity.class);
                startActivity(intentMainMenu);
                overridePendingTransition(0, 0);
            }
        });

        btnOdjava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Intent intentPrijava = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intentPrijava);
                overridePendingTransition(0, 0);

            }
        });
    }
}