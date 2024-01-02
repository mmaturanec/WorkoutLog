package com.example.workoutlog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class UrediProfilActivity extends AppCompatActivity {

    private ImageView ivBackUredi;
    private Button btnSpremiP;
    private EditText etImeP, etPrezimeP, etEmailP, etLozinkaP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uredi_profil);

        ivBackUredi = findViewById(R.id.ivBackUredi);
        btnSpremiP = findViewById(R.id.btnSpremiP);
        etImeP = findViewById(R.id.edImeP);
        etPrezimeP = findViewById(R.id.etPrezimeP);
        etEmailP = findViewById(R.id.etEmailP);
        etLozinkaP = findViewById(R.id.etLozinkaP);


        ivBackUredi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSettings = new Intent(UrediProfilActivity.this, SettingsActivity.class);
                startActivity(intentSettings);
                overridePendingTransition(0, 0);
            }
        });
    }
}