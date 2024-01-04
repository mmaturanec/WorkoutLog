package com.example.workoutlog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UrediProfilActivity extends AppCompatActivity {

    private ImageView ivBackUredi;
    private Button btnSpremiP;
    private EditText etImeP, etPrezimeP, etEmailP;
    private TextView tvLozinkaP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uredi_profil);

        ivBackUredi = findViewById(R.id.ivBackUredi);
        btnSpremiP = findViewById(R.id.btnSpremiP);
        etImeP = findViewById(R.id.edImeP);
        etPrezimeP = findViewById(R.id.etPrezimeP);
        etEmailP = findViewById(R.id.etEmailP);
        tvLozinkaP = findViewById(R.id.tvLozinkaP);


        etImeP.setText(UserSingleton.getInstance().getUser().getIme());
        etPrezimeP.setText(UserSingleton.getInstance().getUser().getPrezime());
        etEmailP.setText(UserSingleton.getInstance().getUser().getEmail());

        ivBackUredi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSettings = new Intent(UrediProfilActivity.this, SettingsActivity.class);
                startActivity(intentSettings);
                overridePendingTransition(0, 0);
            }
        });

        btnSpremiP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserSingleton.getInstance().getUser().setIme(etImeP.getText().toString());
                UserSingleton.getInstance().getUser().setPrezime(etPrezimeP.getText().toString());

                updateUserInfo(UserSingleton.getInstance().getUser());
            }
        });

        tvLozinkaP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = auth.getCurrentUser();

                if (currentUser != null) {
                    String email = currentUser.getEmail();

                    auth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Password reset email sent successfully
                                        Toast.makeText(UrediProfilActivity.this, getString(R.string.ResetEmailSucces), Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Failed to send reset password email
                                        Toast.makeText(UrediProfilActivity.this, getString(R.string.ResetEmailFail), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
    // Update user information in Firebase Database
    private void updateUserInfo(UserInformation newUserInfo) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
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
                            String key = snapshot.getKey();
                            if (key != null) {
                                // Update the existing user information with new values
                                userExerciseTemplateRef.child(key).setValue(newUserInfo)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // Update successful
                                                Intent intentSettings = new Intent(UrediProfilActivity.this, SettingsActivity.class);
                                                startActivity(intentSettings);
                                                overridePendingTransition(0, 0);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Handle failure
                                            }
                                        });
                            }
                        }
                    } else {
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle potential database error
                }
            });
        }
    }

}