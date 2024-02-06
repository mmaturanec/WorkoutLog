package com.example.workoutlog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private TextView tvLoginRegistriraj;
    private Button btnPrijava;
    private EditText etLoginUserName;
    private EditText etLoginLozinka;
    private String sEmail;
    private String sPassword;
    private FirebaseAuth mAuth;
    private TextView tvResetLozinke;

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
        tvResetLozinke = findViewById(R.id.tvZaboravljenaLozinka);

        tvResetLozinke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PasswordResetDialog.showInfoDialog(MainActivity.this);

            }
        });

        tvLoginRegistriraj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRegistracija = new Intent(MainActivity.this, RegistracijaActivity.class);
                startActivity(intentRegistracija);
                overridePendingTransition(0, 0);

            }
        });
        btnPrijava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sEmail = etLoginUserName.getText().toString();
                sPassword = etLoginLozinka.getText().toString();
                if(TextUtils.isEmpty(sEmail) || TextUtils.isEmpty(sPassword))
                {
                    Toast.makeText(MainActivity.this, getString(R.string.UnesiPasswordemial), Toast.LENGTH_SHORT).show();
                }
                else {
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

            }
        });
    }
    public void updateUI(FirebaseUser user)
    {
        if(user != null){
            Intent MainMenu = new Intent(MainActivity.this, MainMenuActivity.class);
            startActivity(MainMenu);
            overridePendingTransition(0, 0);

        }
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
/*
            String userUid = currentUser.getUid();
            String Email = currentUser.getEmail();

            DatabaseReference userExerciseTemplateRef = FirebaseDatabase.getInstance().getReference()
                    .child("user_profile")
                    .child(userUid);

            userExerciseTemplateRef.orderByChild("email").equalTo(Email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            {
                                UserInformation userInfo = snapshot.getValue(UserInformation.class);

                                UserSingleton.getInstance().setUser(userInfo);
                            }

                        }
                    }else {
                        Intent inentPostaviProfil = new Intent(MainActivity.this, PostavljanjeProfilaActivity.class);
                        overridePendingTransition(0, 0);
                        startActivity(inentPostaviProfil);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle potential database error
                }
            });



 */

            //ovdje ide logika za to
            Intent MainMenu = new Intent(MainActivity.this, MainMenuActivity.class);
            startActivity(MainMenu);
            overridePendingTransition(0, 0);

        }
    }
}