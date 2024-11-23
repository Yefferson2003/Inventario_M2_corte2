package com.example.inventario;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.Manifest;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

public class MainActivity5 extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSIONS = 10;

    EditText editTextName, editTextDescription, editTextNit, editTextPrice, editTextQuantity;
    Button buttonSaveProduct;
    ImageButton exit, image_capture_button;

    private ImageCapture imageCapture;
    private ExecutorService cameraExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main5);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("product");

        editTextName= findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextNit = findViewById(R.id.editTextNit);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        buttonSaveProduct = findViewById(R.id.editProduct);
        exit = findViewById(R.id.exit);
        image_capture_button = findViewById(R.id.image_capture_button);

//        image_capture_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity5.this, MainActivity7.class);
//                startActivity(intent);
//            }
//        });

        buttonSaveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name;
                String description;
                String nit;
                double price;
                int quantity;

                description = String.valueOf(editTextDescription.getText());
                name =  String.valueOf(editTextName.getText());
                nit =  String.valueOf(editTextNit.getText());
                price =  Double.parseDouble(editTextPrice.getText().toString());
                quantity =  Integer.parseInt(editTextQuantity.getText().toString());

                Product product = new Product(nit, name, description, price, quantity);

                myRef.child(nit).setValue(product)
                        .addOnSuccessListener(aVoid -> {
                            // Éxito al agregar el producto
                            Toast.makeText(MainActivity5.this, "Producto agregado", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity4.class);
                            startActivity(intent);
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            // Fallo al agregar el producto
                            Toast.makeText(MainActivity5.this, "Error al agregar producto", Toast.LENGTH_SHORT).show();
                        });
            }
        });

        if (allPermissionsGranted()) {
            startCamera();
        } else {

            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSIONS);
        }


        // Set up the listeners for take photo and video capture buttons
//        image_capture_button.setOnClickListener(v -> takePhoto());

        cameraExecutor = Executors.newSingleThreadExecutor();

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity4.class);
                startActivity(intent);
                finish();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    private void takePhoto() {
    }

    private void startCamera() {
        // Obtén la instancia del ProcessCameraProvider
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    // Obtén el cameraProvider
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                    // Preview
                    Preview preview = new Preview.Builder()
                            .build();


                    // Selecciona la cámara trasera por defecto
                    CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                    try {
                        // Desvincula cualquier caso de uso anterior antes de vincular los nuevos
                        cameraProvider.unbindAll();

                        // Vincula los casos de uso a la cámara
                        cameraProvider.bindToLifecycle(
                                MainActivity5.this, cameraSelector, preview);

                    } catch (Exception exc) {
                        Log.e("MainActivity5", "Fallo al vincular el caso de uso", exc);
                    }

                } catch (ExecutionException | InterruptedException e) {
                    Log.e("MainActivity5", "Fallo al obtener ProcessCameraProvider", e);
                }
            }
        }, ContextCompat.getMainExecutor(this));
    }





    private boolean allPermissionsGranted() {
        return ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(this, "Permisos no concedidos por el usuario.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


}