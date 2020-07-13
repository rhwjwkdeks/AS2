package com.example.as2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FuelQuoteForm extends AppCompatActivity {
    private EditText gallons_value;
    private DatePicker date_value;
    private TextView address_value, sprice_value, tprice_value;

    private Button submit_button, back_button;

    private final Calendar currentDate = Calendar.getInstance();

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;


    private final String TAG = "FuelQuoteForm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_quote_form);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        final String userID = firebaseAuth.getCurrentUser().getUid();
        gallons_value = (EditText) findViewById(R.id.gallons_value);
        gallons_value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String edit_text = editable.toString();
                if (!edit_text.isEmpty()) {
                    int gal = Integer.parseInt(edit_text);
                    String str_price = sprice_value.getText().toString().substring(1);
                    double price = Double.parseDouble(str_price);
                    double total_price = gal * price;
                    String t_price = "$" + total_price;
                    tprice_value.setText(t_price);
                    Log.d(TAG, "Total Price=" + t_price);
                }
            }
        });
        initCurrentDate();
        initDeliveryAddress(userID);
        sprice_value = (TextView) findViewById(R.id.sprice_value);
        //To be calculated via Pricing Module
        sprice_value.setText("$2.00");
        tprice_value = (TextView) findViewById(R.id.tprice_value);
        submit_button = (Button) findViewById(R.id.submit_button);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String g = gallons_value.getText().toString();
                if (g.length() < 1) {
                    gallons_value.setError("Please enter the number of gallons.");
                    return;
                }
                float gvalue = Float.parseFloat(g);
                String a = address_value.getText().toString().trim();
                String s = sprice_value.getText().toString().substring(1);
                float svalue = Float.parseFloat(s);
                String t = tprice_value.getText().toString();
                float tvalue = Float.parseFloat(t);
                int selectedYear = date_value.getYear();
                int selectedMonth = date_value.getMonth();
                int selectedDay =  date_value.getDayOfMonth();
                String prefDate = selectedYear + "-" + selectedMonth + "-" + selectedDay;

                DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
                Map<String, Object> data = new HashMap<>();
                data.put("gallonsRequested", gvalue);
                data.put("date", prefDate);
                data.put("address", a);
                data.put("pricePerGallon", svalue);
                data.put("totalDue", tvalue);
                documentReference.collection("fuelQuote").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });

            }
        });
        back_button = (Button) findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backHome();
            }
        });

    }
    private void initCurrentDate() {
        date_value = (DatePicker) findViewById(R.id.date_value);
        final int year = currentDate.get(Calendar.YEAR);
        final int month = currentDate.get(Calendar.MONTH);
        final int day = currentDate.get(Calendar.DAY_OF_MONTH);
        date_value.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int y, int m, int d) {
                if (y > year)
                    view.updateDate(year, month, day);

                if (m > month && y == year)
                    view.updateDate(year, month, day);

                if (d > day && y == year && m == month)
                    view.updateDate(year, month, day);
                Log.d(TAG, "Year=" + year + " Month=" + (month + 1) + " day=" + day);
            }
        });
    }
    private void initDeliveryAddress(String userID) {
        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
        String address = documentReference.get().getResult().getString("Address1");
        if(address.isEmpty()) {
            address = documentReference.get().getResult().getString("Address2");
        }
        address_value = (TextView) findViewById(R.id.address_value);
        address_value.setText(address);
        Log.d(TAG, "Address from Client Profile:" + address);
    }
    public void backHome(){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }
}