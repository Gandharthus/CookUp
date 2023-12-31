package com.example.cookup;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.cookup.databinding.ActivityBarcodeScannerBinding;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class BarcodeScanner extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;
    private ActivityBarcodeScannerBinding binding;
    private int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding = ActivityBarcodeScannerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        flag = 0;
        if(hasCameraPermission()){
            bindCameraUseCases();
        }else{
            requestPermission();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        flag = 0;
    }

    private boolean hasCameraPermission(){
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // user granted permissions - we can set up our scanner
            bindCameraUseCases();
        } else {
            // user did not grant permissions - we can't use the camera
            Toast.makeText(this,
                    "Camera permission required",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private void bindCameraUseCases() {
        final ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);


            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider){
        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        Preview previewUseCase = new Preview.Builder().build();
        previewUseCase.setSurfaceProvider(binding.cameraView.getSurfaceProvider());


        com.google.mlkit.vision.barcode.BarcodeScanner scanner = BarcodeScanning.getClient();
        ImageAnalysis analysisUseCase = new ImageAnalysis.Builder().build();
        analysisUseCase.setAnalyzer(
                Executors.newSingleThreadExecutor(),
                (imageProxy) -> processImageProxy(scanner, imageProxy)
        );

        cameraProvider.bindToLifecycle(this,cameraSelector,previewUseCase, analysisUseCase);
    }

    @SuppressLint("UnsafeOptInUsageError")
    private void processImageProxy(com.google.mlkit.vision.barcode.BarcodeScanner
                                           barcodeScanner, ImageProxy imageProxy) {
        Image image = imageProxy.getImage();
        InputImage inputImage = InputImage.fromMediaImage(image, imageProxy.getImageInfo().getRotationDegrees());
        Intent intent = new Intent(getApplicationContext(), foodDetails.class);
        if (flag == 0){
            barcodeScanner.process(inputImage).addOnSuccessListener(barcodes -> {
                for (Barcode barcode: barcodes) {
                    intent.putExtra("barcodeValue", barcode.getRawValue());
                    startActivity(intent);
                    flag = 1;
                    break;
                }
            }).addOnCompleteListener(task -> {
                imageProxy.getImage().close();
                imageProxy.close();
            }).addOnFailureListener(Throwable::printStackTrace);
        }

    }
}