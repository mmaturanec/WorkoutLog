package com.example.workoutlog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
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

public class RegistracijaActivity extends AppCompatActivity {
    private TextView tvRegisterPrijaviSe;
    private FirebaseAuth mAuth;
    private EditText etRegisterUsername;
    private EditText etRegisterLozinka;
    private EditText etPonovnoUnesiteLozinku;
    private Button btnRegister;
    private String sUserName;
    private String sPassword1;
    private String sPassword2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registracija);

         // Initialize Firebase Auth
         mAuth = FirebaseAuth.getInstance();
         tvRegisterPrijaviSe = findViewById(R.id.tvRegisterPrijaviSe);
         etRegisterUsername = findViewById(R.id.etRegisterUsername);
         etRegisterLozinka = findViewById(R.id.etLoginLozinka);
         etPonovnoUnesiteLozinku = findViewById(R.id.etPonovnoUnesiteLozinku);
         btnRegister = findViewById(R.id.btnRegister);


        tvRegisterPrijaviSe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPrijava = new Intent(RegistracijaActivity.this, MainActivity.class);
                startActivity(intentPrijava);
                overridePendingTransition(0, 0);

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sUserName = etRegisterUsername.getText().toString();
                sPassword1 = etRegisterLozinka.getText().toString();
                sPassword2 = etPonovnoUnesiteLozinku.getText().toString();

                if(TextUtils.isEmpty(sUserName) || TextUtils.isEmpty(sPassword1) ||TextUtils.isEmpty(sPassword2) )
                {
                    Toast.makeText(RegistracijaActivity.this, getResources().getString(R.string.Unesitesvapolja), Toast.LENGTH_SHORT).show();
                }
                //TODO Dodaj provjeru postoji li vec isto korisnicko ime
                else if(!(isValidEmail(sUserName))) {
                    Toast.makeText(RegistracijaActivity.this, getResources().getString(R.string.UnesiteispravnuEmailadresu), Toast.LENGTH_SHORT).show();

                }
                else if(!(sPassword1.equals(sPassword2)))
                {
                    Toast.makeText(RegistracijaActivity.this, getResources().getString(R.string.Unesitejednakelozinke), Toast.LENGTH_SHORT).show();

                }
                else if(sPassword1.length() < 6)
                {
                    Toast.makeText(RegistracijaActivity.this, getResources().getString(R.string.UnesiteLozinkuOd6Znakova), Toast.LENGTH_SHORT).show();

                }
                else{
                    mAuth.createUserWithEmailAndPassword(sUserName, sPassword1)
                            .addOnCompleteListener(RegistracijaActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("kreiranuser", "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("kreiranuser", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(RegistracijaActivity.this, getResources().getString(R.string.VecPostojiKorisnikSTomEmailAdresom),
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
            Intent MainMenu = new Intent(RegistracijaActivity.this, MainMenuActivity.class);
            startActivity(MainMenu);
            overridePendingTransition(0, 0);

        }
    }
    public boolean isValidEmail(CharSequence target) {
        return (target != null && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent MainMenu = new Intent(RegistracijaActivity.this, MainMenuActivity.class);
            startActivity(MainMenu);
            overridePendingTransition(0, 0);

        }
    }
}