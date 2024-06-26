package com.example.ngeteh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EditProductActivity extends AppCompatActivity {
    private EditText editTextName, editTextPrice;
    private Button buttonSave, buttonDelete;
    private DatabaseHelper db;
    private int productId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        editTextName = findViewById(R.id.editTextName);
        editTextPrice = findViewById(R.id.editTextPrice);
        buttonSave = findViewById(R.id.buttonSave);
        buttonDelete = findViewById(R.id.buttonDelete);

        db = new DatabaseHelper(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("PRODUCT_ID")) {
            productId = intent.getIntExtra("PRODUCT_ID", -1);
            String productName = intent.getStringExtra("PRODUCT_NAME");
            double productPrice = intent.getDoubleExtra("PRODUCT_PRICE", 0.0);

            editTextName.setText(productName);
            editTextPrice.setText(String.valueOf(productPrice));

            buttonDelete.setVisibility(View.VISIBLE);
        }

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString();
                double price = Double.parseDouble(editTextPrice.getText().toString());

                if (productId == -1) {
                    if (db.addProduct(name, price)) {
                        Toast.makeText(EditProductActivity.this, "Product added", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditProductActivity.this, "Failed to add product", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (db.updateProduct(productId, name, price)) {
                        Toast.makeText(EditProductActivity.this, "Product updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditProductActivity.this, "Failed to update product", Toast.LENGTH_SHORT).show();
                    }
                }
                finish();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (db.deleteProduct(productId)) {
                    Toast.makeText(EditProductActivity.this, "Product deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditProductActivity.this, "Failed to delete product", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
    }
}
