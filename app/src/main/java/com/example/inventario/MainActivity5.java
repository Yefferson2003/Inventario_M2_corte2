package com.example.inventario;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity5 extends AppCompatActivity {

    EditText editTextName, editTextDescription, editTextNit, editTextPrice, editTextQuantity;
    Button buttonSaveProduct;
    ImageButton exit;


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
}