package com.example.ngeteh;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {
    private ListView listViewProducts;
    private Button buttonAddProduct;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        listViewProducts = findViewById(R.id.listViewProducts);
        buttonAddProduct = findViewById(R.id.buttonAddProduct);

        db = new DatabaseHelper(this);
        loadProducts();

        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, EditProductActivity.class);
                startActivity(intent);
            }
        });

        listViewProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                int productId = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                String productName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                double productPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));

                Intent intent = new Intent(AdminActivity.this, EditProductActivity.class);
                intent.putExtra("PRODUCT_ID", productId);
                intent.putExtra("PRODUCT_NAME", productName);
                intent.putExtra("PRODUCT_PRICE", productPrice);
                startActivity(intent);
            }
        });
    }

    private void loadProducts() {
        Cursor cursor = db.getAllProducts();
        String[] from = {"name", "price"};
        int[] to = {android.R.id.text1, android.R.id.text2};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, from, to, 0);
        listViewProducts.setAdapter(adapter);
    }
}

