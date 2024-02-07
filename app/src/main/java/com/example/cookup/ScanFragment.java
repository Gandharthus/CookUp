package com.example.cookup;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.zxing.BarcodeFormat;
/*import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;*/

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ScanFragment extends Fragment {
    private SurfaceView barcodeView;
    private Button btnScanBarcode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);

        barcodeView = view.findViewById(R.id.cameraPreview);
        btnScanBarcode = view.findViewById(R.id.btnScanBarcode);

        // Request camera permission
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    1);
        } else {
            setupBarcodeScanner();
        }

        btnScanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //barcodeView.resume();
            }
        });

        return view;
    }

    private void setupBarcodeScanner() {
      /*  barcodeView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                // Handle barcode result
                if (result.getText() != null) {
                    // Handle the barcode result (e.g., navigate to a new screen)
                    // Example: Start a new activity with the scanned barcode
                    Intent intent = new Intent(requireContext(), RecipeDetailsActivity.class);
                    intent.putExtra("barcodeResult", result.getText());
                    startActivity(intent);
                }
            }

            @Override
            public void possibleResultPoints(List resultPoints) {
                // Not needed for now
            }
        });*/
    }

    @Override
    public void onResume() {
        super.onResume();
        //barcodeView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
       // barcodeView.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //barcodeView.getBarcodeView().stopDecoding();
    }
}
