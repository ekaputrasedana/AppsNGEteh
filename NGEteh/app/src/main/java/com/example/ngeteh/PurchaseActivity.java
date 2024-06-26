package com.example.ngeteh;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PurchaseActivity extends AppCompatActivity {
    private ListView listViewCartItems;
    private TextView textTotalPrice;
    private RadioGroup radioGroupDelivery;
    private LinearLayout layoutAddress;
    private EditText editTextAddress;
    private RadioGroup radioGroupPayment;
    private Button buttonConfirmOrder;
    private DatabaseHelper db;
    private double totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        listViewCartItems = findViewById(R.id.listViewCartItems);
        textTotalPrice = findViewById(R.id.textTotalPrice);
        radioGroupDelivery = findViewById(R.id.radioGroupDelivery);
        layoutAddress = findViewById(R.id.layoutAddress);
        editTextAddress = findViewById(R.id.editTextAddress);
        radioGroupPayment = findViewById(R.id.radioGroupPayment);
        buttonConfirmOrder = findViewById(R.id.buttonConfirmOrder);

        db = new DatabaseHelper(this);

        loadCartItems();

        radioGroupDelivery.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonDelivery) {
                    layoutAddress.setVisibility(View.VISIBLE);
                } else {
                    layoutAddress.setVisibility(View.GONE);
                }
            }
        });

        buttonConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioGroupDelivery.getCheckedRadioButtonId() == R.id.radioButtonDelivery && editTextAddress.getText().toString().isEmpty()) {
                    Toast.makeText(PurchaseActivity.this, "Please enter your address", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (radioGroupPayment.getCheckedRadioButtonId() == R.id.radioButtonNonCash) {
                    Toast.makeText(PurchaseActivity.this, "Order processed and will be delivered. Contact the shop at 123-456-7890 for payment.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(PurchaseActivity.this, "Order completed. Please pick it up at our shop.", Toast.LENGTH_LONG).show();
                }

                db.clearCart();
                finish();
            }
        });
    }

    private void loadCartItems() {
        Cursor cursor = db.getCartItems();
        String[] from = {"name", "price", "quantity"};
        int[] to = {android.R.id.text1, android.R.id.text2, android.R.id.text2};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, from, to, 0);
        listViewCartItems.setAdapter(adapter);

        totalPrice = 0.0;
        if (cursor.moveToFirst()) {
            do {
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                totalPrice += price * quantity;
            } while (cursor.moveToNext());
        }
        textTotalPrice.setText("Total Price: " + totalPrice);
    }
}
