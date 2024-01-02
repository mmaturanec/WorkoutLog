package com.example.workoutlog;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KameraActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_CAMERA_PERMISSION = 200;
    private static final String[] PERMISSIONS = {Manifest.permission.CAMERA};

    private static final String TAG = "CameraXBasic";
    private static final String FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS";
    private boolean isBackCamera = true;
    Button camera_capture_button;
    PreviewView view_finder;
    ExecutorService executor;
    ImageCapture imageCapture;
    File outputDirectory;
    ImageView ivFlipCamera, ivBackCamera, ivBrowseHistory;

    //dio sa camerax - preview
    ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private CameraSelector currentCameraSelector;
    private ProcessCameraProvider cameraProvider;

    private long mLastAnalysisResultTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kamera);

        view_finder = findViewById(R.id.viewFinder);

        camera_capture_button = findViewById(R.id.image_capture_button);
        ivFlipCamera = findViewById(R.id.ivFlipCamera);
        ivBackCamera = findViewById(R.id.ivBackCamera);
        ivBrowseHistory = findViewById(R.id.ivBrowseHistory);

        ivFlipCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipCamera();
            }
        });




        ivBackCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMainMenu = new Intent(KameraActivity.this, SettingsActivity.class);
                startActivity(intentMainMenu);
                overridePendingTransition(0, 0);
            }
        });
        camera_capture_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File photoFile = new File(getBaseContext().getExternalCacheDir() + File.separator + System.currentTimeMillis() + ".png");
                String name = new SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis());

                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                    contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image");
                }
                //dio sa camerax - capture
                ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions.Builder(getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues).build();
                imageCapture.takePicture(outputOptions, executor,
                        new ImageCapture.OnImageSavedCallback() {
                            @Override
                            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                                Uri savedUri = Uri.fromFile(photoFile);
                                //Uspjesno sprema u galeriju, potrebno spremit u firebase i otvorit intent settings!
                                /*
                                Intent otvoriFragmente = new Intent(getApplicationContext(), CreateNewRecordActivity.class);
                                otvoriFragmente.putExtra("savedUri", savedUri.toString());
                                startActivity(otvoriFragmente);

                                 */

                            }

                            @Override
                            public void onError(@NonNull ImageCaptureException exception) {
                                Log.d("myTag", exception.getMessage());
                            }
                        });
            }
        });

        if (checkPermission()){
            startCamera();
        }
    }

    private boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS,
                    REQUEST_CODE_CAMERA_PERMISSION);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(
                                this,
                                "You can't use image classification example without granting CAMERA permission",
                                Toast.LENGTH_LONG)
                        .show();
                finish();
            } else {
                startCamera();
            }
        }
    }

    private void startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    cameraProvider = cameraProviderFuture.get();
                    bindPreview(); // Call bindPreview without arguments initially
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, ContextCompat.getMainExecutor(this));
    }

    void bindPreview()
    {
        if (cameraProvider == null) {
        // Handle the case where cameraProvider is not yet initialized
        return;
    }

    Preview preview = new Preview.Builder()
            .setTargetAspectRatio(AspectRatio.RATIO_4_3)
            .build();

    CameraSelector cameraSelector;
        if (isBackCamera) {
        cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();
    } else {
        cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build();
    }

    executor = Executors.newSingleThreadExecutor();

    imageCapture = new ImageCapture.Builder().build();

        cameraProvider.unbindAll();

        try {
        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner) this,
                cameraSelector, preview, imageCapture);

        preview.setSurfaceProvider(view_finder.getSurfaceProvider());
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    private void flipCamera() {
        if (cameraProvider == null) {
            // Handle the case where cameraProvider is not yet initialized
            return;
        }

        isBackCamera = !isBackCamera; // Toggle the flag for the next camera switch

        CameraSelector newCameraSelector;
        if (isBackCamera) {
            newCameraSelector = new CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build();
        } else {
            newCameraSelector = new CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                    .build();
        }

        try {
            bindPreview(); // Rebind with the updated camera selector
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}