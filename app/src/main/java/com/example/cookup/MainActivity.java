package com.example.cookup;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cookup.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    Button btnScan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        btnScan = findViewById(R.id.btn_scn);
        Intent intent = new Intent(getApplicationContext(), BarcodeScanner.class);

        btnScan.setOnClickListener(view -> startActivity(intent));

    }
}



