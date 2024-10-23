package com.example.inventario;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity6 extends AppCompatActivity {

    EditText editTextName, editTextDescription, editTextPrice, editTextQuantity;
    ImageButton exit, deleteProduct;
    Button editProduct;

    DatabaseReference productRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main6);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("product");

        editTextName = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        editProduct = findViewById(R.id.editProduct);
        deleteProduct = findViewById(R.id.deleteProduct);
        exit = findViewById(R.id.exit);

        Intent intent = getIntent();
        String productId = intent.getStringExtra("ProductoId");

        if (productId != null) {
            productRef = FirebaseDatabase.getInstance().getReference("product").child(productId);

            productRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Product product = snapshot.getValue(Product.class);

                        if (product != null) {
                            editTextName.setText(product.getName());
                            editTextDescription.setText(product.getDescription());
                            editTextPrice.setText(String.valueOf(product.getPrice()));
                            editTextQuantity.setText(String.valueOf(product.getQuantity()));
                        } else {
                            Toast.makeText(MainActivity6.this, "Producto no encontrado", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity6.this, "Producto no existe en la base de datos", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity6.this, "Error al buscar producto: ", Toast.LENGTH_SHORT).show();
                }
            });
        }

        editProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name;
                String description;
                String nit;
                double price;
                int quantity;

                description = String.valueOf(editTextDescription.getText());
                name =  String.valueOf(editTextName.getText());
                nit =  productId;
                price =  Double.parseDouble(editTextPrice.getText().toString());
                quantity =  Integer.parseInt(editTextQuantity.getText().toString());

                Product product = new Product(nit, name, description, price, quantity);

                myRef.child(nit).setValue(product)
                        .addOnSuccessListener(aVoid -> {
                            // Éxito al agregar el producto
                            Toast.makeText(MainActivity6.this, "Producto editado", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity4.class);
                            startActivity(intent);
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            // Fallo al agregar el producto
                            Toast.makeText(MainActivity6.this, "Error al editar producto", Toast.LENGTH_SHORT).show();
                        });
            }
        });


        deleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productRef.removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            Toast.makeText(MainActivity6.this, "Error al eliminar producto: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity6.this, "Producto eliminado correctamente", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(MainActivity6.this, MainActivity4.class);
                            startActivity(intent);
                            finish();
                        }
                    }
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