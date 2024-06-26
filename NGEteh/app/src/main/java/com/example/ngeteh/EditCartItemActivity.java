package com.example.ngeteh;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EditCartItemActivity extends AppCompatActivity {
    private EditText editTextQuantity;
    private Button buttonSave, buttonDelete;
    private DatabaseHelper db;
    private int cartId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cart_item);

        editTextQuantity = findViewById(R.id.editTextQuantity);
        buttonSave = findViewById(R.id.buttonSave);
        buttonDelete = findViewById(R.id.buttonDelete);

        db = new DatabaseHelper(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("CART_ID")) {
            cartId = intent.getIntExtra("CART_ID", -1);

            Cursor cursor = db.getCartItem(cartId);
            if (cursor.moveToFirst()) {
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                editTextQuantity.setText(String.valueOf(quantity));
            }
            cursor.close();
        }

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(editTextQuantity.getText().toString());
                if (db.updateCartItem(cartId, quantity)) {
                    Toast.makeText(EditCartItemActivity.this, "Cart item updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditCartItemActivity.this, "Failed to update cart item", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (db.deleteCartItem(cartId)) {
                    Toast.makeText(EditCartItemActivity.this, "Cart item deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditCartItemActivity.this, "Failed to delete cart item", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
    }
}
