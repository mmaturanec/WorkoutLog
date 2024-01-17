package com.example.workoutlog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class PostavljanjeProfilaActivity extends AppCompatActivity {

    private ImageView ivOdaberiProfilnu, ivAddOdaberiProfilnu;
    private EditText etOdaberiIme, etOdaberiPrezime;
    private TextView tvOdaberiProfilnu;
    private Button btnPostaviProfil;
    String ImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postavljanje_profila);

        ivOdaberiProfilnu = findViewById(R.id.ivOdaberiProfilnu);
        ivAddOdaberiProfilnu = findViewById(R.id.ivAddOdaberiProfilnu);
        etOdaberiIme = findViewById(R.id.etOdaberiIme);
        etOdaberiPrezime = findViewById(R.id.etOdaberiPrezime);
        tvOdaberiProfilnu = findViewById(R.id.tvOdaberiProfilnu);
        btnPostaviProfil = findViewById(R.id.btnPostaviProfil);

        final Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            String ime = bundle.getString("ime");
            String prezime = bundle.getString("prezime");
            if(!TextUtils.isEmpty(ime))
            {
                etOdaberiIme.setText(ime);
            }
            if(!TextUtils.isEmpty(prezime))
            {
                etOdaberiPrezime.setText(prezime);
            }
            ImageUri = bundle.getString("savedUri");

            String updateFromGallery = "";
            if(bundle.containsKey("gallery"))
            {
                updateFromGallery = bundle.getString("gallery");

            }
            Boolean BackCamera = bundle.getBoolean("isBackCamera");
            if(updateFromGallery.equals("gallery"))
            {
                Uri UriFromBundle = Uri.parse(ImageUri);
                try {
                    InputStream inputStream = getContentResolver().openInputStream(UriFromBundle);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    Bitmap rotatedBitmap = rotateBitmap(bitmap, false);
                    ivOdaberiProfilnu.setImageBitmap(rotatedBitmap);
                    uploadImageToFirebase();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{

            String[] projection = new String[]{
                    MediaStore.Images.ImageColumns._ID,
                    MediaStore.Images.ImageColumns.DATA,
                    MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.ImageColumns.DATE_TAKEN,
                    MediaStore.Images.ImageColumns.MIME_TYPE
            };
            final Cursor cursor = getContentResolver()
                    .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
                            null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

            // Put it in the image view
            if (cursor.moveToFirst()) {
                final ImageView imageView = findViewById(R.id.ivOdaberiProfilnu);
                String imageLocation = cursor.getString(1);
                File imageFile = new File(imageLocation);
                if (imageFile.exists()) {
                    Bitmap originalBitmap = BitmapFactory.decodeFile(imageLocation);
                    Bitmap rotatedBitmap = rotateBitmap(originalBitmap, BackCamera);
                    imageView.setImageBitmap(rotatedBitmap);
                }
            }
            }

            ivAddOdaberiProfilnu.setVisibility(View.GONE);

        }

        ivOdaberiProfilnu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentKamera = new Intent(PostavljanjeProfilaActivity.this, KameraActivity.class);
                intentKamera.putExtra("type", "setProfilePicture");
                intentKamera.putExtra("ime", etOdaberiIme.getText().toString());
                intentKamera.putExtra("prezime", etOdaberiIme.getText().toString());

                startActivity(intentKamera);
                overridePendingTransition(0, 0);
            }
        });
        ivAddOdaberiProfilnu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentKamera = new Intent(PostavljanjeProfilaActivity.this, KameraActivity.class);
                intentKamera.putExtra("type", "setProfilePicture");
                intentKamera.putExtra("ime", etOdaberiIme.getText().toString());
                intentKamera.putExtra("prezime", etOdaberiPrezime.getText().toString());
                startActivity(intentKamera);
                overridePendingTransition(0, 0);
            }
        });
        tvOdaberiProfilnu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentKamera = new Intent(PostavljanjeProfilaActivity.this, KameraActivity.class);
                intentKamera.putExtra("type", "setProfilePicture");
                intentKamera.putExtra("ime", etOdaberiIme.getText().toString());
                intentKamera.putExtra("prezime", etOdaberiIme.getText().toString());
                startActivity(intentKamera);
                overridePendingTransition(0, 0);
            }
        });

        btnPostaviProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProfileToFirebase();
            }
        });
    }

    public Bitmap rotateBitmap(Bitmap source, Boolean isBackCamera) {
        Matrix matrix = new Matrix();
        if (isBackCamera) {
            matrix.postRotate(90); // Negative angle for 90 degrees left rotation

        } else {
            matrix.postRotate(-90); // Negative angle for 90 degrees left rotation

        }

        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
    private void uploadProfileToFirebase()
    {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userUid = currentUser.getUid();
            String Email = currentUser.getEmail();
            String Ime = etOdaberiIme.getText().toString();
            String Prezime = etOdaberiPrezime.getText().toString();

            if (TextUtils.isEmpty(Ime)) {
                Toast.makeText(PostavljanjeProfilaActivity.this, getString(R.string.UnesiIme), Toast.LENGTH_SHORT).show();

            } else if (TextUtils.isEmpty(Prezime)) {
                Toast.makeText(PostavljanjeProfilaActivity.this, getString(R.string.UnesiPrezime), Toast.LENGTH_SHORT).show();

            } else {

                DatabaseReference userExerciseTemplateRef = FirebaseDatabase.getInstance().getReference()
                        .child("user_profile")
                        .child(userUid);

                userExerciseTemplateRef.orderByChild("email").equalTo(Email).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Toast.makeText(PostavljanjeProfilaActivity.this, getString(R.string.KorisnikVecImaProfil), Toast.LENGTH_SHORT).show();
                            Intent intentMainmenu = new Intent(PostavljanjeProfilaActivity.this, MainMenuActivity.class);
                            overridePendingTransition(0, 0);
                            startActivity(intentMainmenu);

                        } else {
                            UserInformation exerciseTemplate = new UserInformation(Ime, Prezime, Email, userUid, userUid + ".jpg");
                            uploadImageToFirebase();
                            String templateKey = userExerciseTemplateRef.push().getKey();
                            if (templateKey != null) {
                                userExerciseTemplateRef.child(templateKey).setValue(exerciseTemplate)
                                        .addOnSuccessListener(aVoid -> {
                                            ExerciseSingleton.destroyInstance();
                                            Toast.makeText(PostavljanjeProfilaActivity.this, getString(R.string.uspjesnoSpremljenPredlozak), Toast.LENGTH_SHORT).show();
                                            Intent intentMainmenu = new Intent(PostavljanjeProfilaActivity.this, MainMenuActivity.class);
                                            startActivity(intentMainmenu);
                                            overridePendingTransition(0, 0);

                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(PostavljanjeProfilaActivity.this, getString(R.string.neuspjesnoSpremljenPredlozak), Toast.LENGTH_SHORT).show();

                                        });
                            }
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
    private void uploadImageToFirebase() {

        Drawable drawable = ivOdaberiProfilnu.getDrawable();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("images");
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();


        if (drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

            // Convert the bitmap to a byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            // Create a reference to the image in Firebase Storage (change "images" to your desired folder)
            StorageReference imageRef = storageRef.child(user.getUid()+".jpg");

            // Upload the byte array to Firebase Storage
            UploadTask uploadTask = imageRef.putBytes(data);

            // Register observers to listen for upload success or failure
            uploadTask.addOnFailureListener(exception -> {
                // Handle unsuccessful uploads
            }).addOnSuccessListener(taskSnapshot -> {
                // Handle successful uploads
                // You can get the download URL of the uploaded image
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String downloadUrl = uri.toString();
                    // Now you have the download URL which you can use in your app
                    // Perform actions like saving this URL to a database or displaying the image
                });
            });
        }
    }
}