package com.example.workoutlog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class SettingsActivity extends AppCompatActivity {

    private Button btnOdjava, btnUrediProfil;
    private ImageView ivProfile, ivBackSettings;
    private TextView tvPromjeniProfilnu, tvime;
    String ImageUri;

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


        UserSingleton userSingleton = UserSingleton.getInstance();


        UserInformation userInformation = userSingleton.getUser();


        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/" + userInformation.getProfilePicture());


        final Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            ImageUri = bundle.getString("savedUri");
            Boolean BackCamera = bundle.getBoolean("isBackCamera");
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
                final ImageView imageView = findViewById(R.id.ivProfile);
                String imageLocation = cursor.getString(1);
                File imageFile = new File(imageLocation);
                if (imageFile.exists()) {
                    Bitmap originalBitmap = BitmapFactory.decodeFile(imageLocation);
                    Bitmap rotatedBitmap = rotateBitmap(originalBitmap, BackCamera);
                    imageView.setImageBitmap(rotatedBitmap);
                    uploadImageToFirebase();
                }
            }

        }
// Download the image into a local file
        try {
            File localFile = File.createTempFile("images", ""); // Temporary file to store the image
            storageRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                // Local file created, set it to the ImageView
                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                ivProfile.setImageBitmap(bitmap); // Set the downloaded image to your ImageView
            }).addOnFailureListener(exception -> {
                // Handle any errors
                Log.e("FirebaseStorage", "Error downloading image: " + exception.getMessage());
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        tvime.setText(userInformation.getIme());


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
                UserSingleton.destroyInstance();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Intent intentPrijava = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intentPrijava);
                overridePendingTransition(0, 0);

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
    private void uploadImageToFirebase() {

        Drawable drawable = ivProfile.getDrawable();
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