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

public class MainActivity extends AppCompatActivity {
    private ListView listViewProducts;
    private Button buttonLogout;
    private Button buttonViewCart;
    private Button buttonViewProfile;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewProducts = findViewById(R.id.listViewProducts);
        buttonLogout = findViewById(R.id.buttonLogout);
        buttonViewCart = findViewById(R.id.buttonViewCart);
        buttonViewProfile = findViewById(R.id.buttonViewProfile);

        db = new DatabaseHelper(this);

        loadProducts();

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        buttonViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra("USERNAME", "current_username"); // Replace with actual username
                startActivity(intent);
            }
        });

        listViewProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                String productName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                double productPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));

                Intent intent = new Intent(MainActivity.this, PurchaseActivity.class);
                intent.putExtra("PRODUCT_NAME", productName);
                intent.putExtra("PRODUCT_PRICE", productPrice);
                startActivity(intent);
            }
        });
    }

    private void loadProducts() {
        Cursor cursor = db.getReadableDatabase().rawQuery("SELECT * FROM products", null);
        String[] from = {"name", "price"};
        int[] to = {android.R.id.text1, android.R.id.text2};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, from, to, 0);
        listViewProducts.setAdapter(adapter);
    }
}
