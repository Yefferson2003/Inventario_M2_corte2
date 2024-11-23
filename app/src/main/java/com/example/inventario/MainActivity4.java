package com.example.inventario;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity4 extends AppCompatActivity {

    TableLayout tableLayout;
    FirebaseAuth auth;
    ImageButton imageButton;
    Button button, searchBtn;
    TextView textView;
    FirebaseUser user;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main4);

        auth = FirebaseAuth.getInstance();
        imageButton = findViewById(R.id.exit);
        button = findViewById(R.id.addProduct);
        searchBtn = findViewById(R.id.button2);
        textView = findViewById(R.id.userDetails);
        tableLayout = findViewById(R.id.tableLayout);

        user = auth.getCurrentUser();

        searchBtn.setOnClickListener(view -> {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("product");

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    tableLayout.removeAllViews();

                    TableRow headerRow = new TableRow(MainActivity4.this);
                    headerRow.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT
                    ));

                    TextView header1 = new TextView(MainActivity4.this);
                    header1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                    header1.setTextSize(18);
                    header1.setPadding(16, 16, 16, 16);
                    header1.setBackgroundColor(getResources().getColor(R.color.amarillo));
                    header1.setTextColor(getResources().getColor(R.color.white));
                    header1.setText("Nit");
                    headerRow.addView(header1);

                    TextView header2 = new TextView(MainActivity4.this);
                    header2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                    header2.setTextSize(18);
                    header2.setPadding(16, 16, 16, 16);
                    header2.setBackgroundColor(getResources().getColor(R.color.amarillo));
                    header2.setTextColor(getResources().getColor(R.color.white));
                    header2.setText("Nombre");
                    headerRow.addView(header2);

                    TextView header3 = new TextView(MainActivity4.this);
                    header3.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                    header3.setTextSize(18);
                    header3.setPadding(16, 16, 16, 16);
                    header3.setBackgroundColor(getResources().getColor(R.color.amarillo));
                    header3.setTextColor(getResources().getColor(R.color.white));
                    header3.setText("Cantidad");
                    headerRow.addView(header3);

                    TextView header4 = new TextView(MainActivity4.this);
                    header4.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                    header4.setTextSize(18);
                    header4.setPadding(16, 16, 16, 16);
                    header4.setBackgroundColor(getResources().getColor(R.color.amarillo));
                    header4.setTextColor(getResources().getColor(R.color.white));
                    header4.setText("Acciones");
                    headerRow.addView(header4);

                    tableLayout.addView(headerRow);

                    boolean alternateColor = true;

                    // Iterar sobre los productos en Firebase
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.exists()) {
                            // Verificar si los campos existen antes de acceder a ellos
                            String productNit = snapshot.child("nit").getValue() != null ? snapshot.child("nit").getValue().toString() : "N/A";
                            String productName = snapshot.child("name").getValue() != null ? snapshot.child("name").getValue().toString() : "N/A";
                            String productQuantity = snapshot.child("quantity").getValue() != null ? snapshot.child("quantity").getValue().toString() : "0";

                            TableRow row = new TableRow(MainActivity4.this);
                            row.setLayoutParams(new TableRow.LayoutParams(
                                    TableRow.LayoutParams.MATCH_PARENT,
                                    TableRow.LayoutParams.WRAP_CONTENT
                            ));

                            row.setBackgroundColor(alternateColor ? getResources().getColor(R.color.white) : getResources().getColor(R.color.yellow_light));
                            alternateColor = !alternateColor;

                            TextView txtId = new TextView(MainActivity4.this);
                            txtId.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                            txtId.setText(productNit);
                            row.addView(txtId);

                            TextView txtName = new TextView(MainActivity4.this);
                            txtName.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                            txtName.setText(productName);
                            row.addView(txtName);

                            TextView txtQuantity = new TextView(MainActivity4.this);
                            txtQuantity.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                            txtQuantity.setText(productQuantity);
                            txtQuantity.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            row.addView(txtQuantity);

                            Button btnEdit = new Button(MainActivity4.this);
                            txtQuantity.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                            btnEdit.setText("Editar");
                            btnEdit.setOnClickListener(v -> {
                                Intent intent = new Intent(MainActivity4.this, MainActivity6.class);
                                intent.putExtra("ProductoId", productNit);
                                startActivity(intent);
                            });
                            row.addView(btnEdit);

                            tableLayout.addView(row);
                        }
                    }

                    // Manejar el caso de que no existan productos
                    if (!dataSnapshot.exists()) {
                        Toast.makeText(getApplicationContext(), "NO EXISTEN PRODUCTOS REGISTRADOS", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "La búsqueda de datos fue cancelada. Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });




        if (user == null){
            Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
            startActivity(intent);
            finish();
        }
        else{
            textView.setText(user.getEmail());
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                startActivity(intent);
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity5.class);
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