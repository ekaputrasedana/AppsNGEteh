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

public class CartActivity extends AppCompatActivity {
    private ListView listViewCart;
    private Button buttonCheckout;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        listViewCart = findViewById(R.id.listViewCart);
        buttonCheckout = findViewById(R.id.buttonCheckout);

        db = new DatabaseHelper(this);
        loadCartItems();

        listViewCart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                int cartId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));

                Intent intent = new Intent(CartActivity.this, EditCartItemActivity.class);
                intent.putExtra("CART_ID", cartId);
                startActivity(intent);
            }
        });

        buttonCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.clearCart();
                Toast.makeText(CartActivity.this, "Checkout successful", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void loadCartItems() {
        Cursor cursor = db.getCartItems();
        String[] from = {"name", "price", "quantity"};
        int[] to = {R.id.textProductName, R.id.textProductPrice, R.id.textProductQuantity};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.cart_item, cursor, from, to, 0);
        listViewCart.setAdapter(adapter);
    }
}
