package com.example.workoutlog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private TextView tvLoginRegistriraj;
    private Button btnPrijava;
    private EditText etLoginUserName;
    private EditText etLoginLozinka;
    private String sEmail;
    private String sPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
         tvLoginRegistriraj = findViewById(R.id.tvLoginRegistriraj);
         btnPrijava = findViewById(R.id.btnPrijava);
        etLoginUserName = findViewById(R.id.etLoginUserName);
        etLoginLozinka = findViewById(R.id.etLoginLozinka);

        tvLoginRegistriraj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRegistracija = new Intent(MainActivity.this, RegistracijaActivity.class);
                startActivity(intentRegistracija);
            }
        });
        btnPrijava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sEmail = etLoginUserName.getText().toString();
                sPassword = etLoginLozinka.getText().toString();
                /*
                Intent intentMainMenu = new Intent(MainActivity.this, MainMenuActivity.class);
                startActivity(intentMainMenu);

                 */
                mAuth.signInWithEmailAndPassword(sEmail, sPassword)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(MainActivity.this, getResources().getString(R.string.PogresnoUneseniPodatci),
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            }
                        });

            }
        });
    }
    public void updateUI(FirebaseUser user)
    {
        if(user != null){
            Intent MainMenu = new Intent(MainActivity.this, MainMenuActivity.class);
            startActivity(MainMenu);
        }
    }
}