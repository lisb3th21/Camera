package com.lisbeth.camera;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    public static final int CAMERA_PERM_CODE = 101;
    String currentPhotoPath;
    private static final int REQUEST_CODE = 1;
    public static final int CAMERA_REQUEST_CODE = 102;
    ImageView selectedImage;
    ImageButton cameraBtn;
    Uri cam_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectedImage = findViewById(R.id.imageView);
        cameraBtn = findViewById(R.id.imageButton);

        cameraBtn.setOnClickListener(v -> askCameraPermissions());
    }

    private void askCameraPermissions() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA }, CAMERA_PERM_CODE);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                openCamera();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void openCamera() {
        String relativePath = Environment.DIRECTORY_PICTURES + File.separator + "miApp";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath);

        cam_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        camera.putExtra(MediaStore.EXTRA_OUTPUT, cam_uri);
        startCamera.launch(camera);
    }

    ActivityResultLauncher<Intent> startCamera = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //Log.d("TAG", String.valueOf(result.getResultCode()));

                    if (result.getResultCode() == RESULT_OK && result.getData() != null ) {

                        selectedImage.setImageURI(cam_uri);
                    }
                }
            });
}