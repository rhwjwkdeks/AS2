package com.example.as2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class FuelQuoteForm extends AppCompatActivity implements FuelQuoteFormMVP.View {
    private EditText gallons_value;
    private TextView date_value;
    DatePickerDialog picker;
    private TextView sprice_value, tprice_value;
    private Spinner address_value;

    private Button submit_button, back_button;
    private FuelQuoteFormPresenter presenter;

    FirebaseAuth firebaseAuth;

    private final String TAG = "FuelQuoteForm";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_quote_form);
        presenter = new FuelQuoteFormPresenter(this);
        firebaseAuth = FirebaseAuth.getInstance();
        presenter.validateProfile();
        gallons_value = (EditText) findViewById(R.id.gallons_value);
        date_value = (TextView) findViewById(R.id.date_value);
        address_value = findViewById(R.id.address_value);
        sprice_value = (TextView) findViewById(R.id.sprice_value);
        tprice_value = (TextView) findViewById(R.id.tprice_value);
        submit_button = (Button) findViewById(R.id.submit_button);
        back_button = (Button) findViewById(R.id.back_button);
        gallons_value.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        gallons_value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            //sets suggested & total price when user enters a number
            @Override
            public void afterTextChanged(Editable editable) {
                final String edit_text = editable.toString();
                presenter.validateGallons(edit_text);
                presenter.calculatePrices();
            }
        });
        initCurrentDate();
        initDeliveryAddress();
        //add validation on submit before adding info to database
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String g = gallons_value.getText().toString();
                String a = address_value.getSelectedItem().toString().trim();
                String prefDate = date_value.getText().toString();
                String s = sprice_value.getText().toString();
                String t = tprice_value.getText().toString();
                presenter.onSubmit(g, a, prefDate, s, t);
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

    @Override
    public void setGallonsError() {
        Toast.makeText(getApplicationContext(), "Please enter a valid number of gallons (must be a positive number).", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setDateError() {
        Toast.makeText(getApplicationContext(), "Please enter a valid delivery date (cannot be set before today).", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setAddressError() {
        Toast.makeText(getApplicationContext(), "Please select your delivery address.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setProfileError() {
        Toast.makeText(getApplicationContext(), "Please create a Client Profile before requesting a Fuel Quote.", Toast.LENGTH_LONG).show();
        backHome();
    }

    @Override
    public void setUserError() {
        Toast.makeText(getApplicationContext(), "There was an error in fetching the user ID. Please exit the app and login again.", Toast.LENGTH_LONG).show();
        backHome();
    }

    @Override
    public void setDate(String date) {
        date_value.setText(date);
    }

    @Override
    public void setAddresses(String[] addresses) {
        Spinner s = findViewById(R.id.address_value);
        ArrayAdapter<String> a = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, addresses);
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(a);
    }

    @Override
    public void setSuggestedPrice(String suggestedPrice) {
        sprice_value.setText(suggestedPrice);
    }

    @Override
    public void setTotalPrice(String totalPrice) {
        tprice_value.setText(totalPrice);
    }

    @Override
    public void successSubmit() {
        Toast.makeText(getApplicationContext(), "Form submitted successfully!", Toast.LENGTH_LONG).show();
        backHome();
    }

    //initializes date to current date when opened
    //checks for invalid dates, otherwise set textview date to selected date
    private void initCurrentDate() {
        date_value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar currentDate = Calendar.getInstance();
                final int year = currentDate.get(Calendar.YEAR);
                final int month = currentDate.get(Calendar.MONTH);
                final int day = currentDate.get(Calendar.DAY_OF_MONTH);
                picker = new DatePickerDialog(FuelQuoteForm.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                        presenter.validateDate(y, m, d);
                    }
                }, year, month, day);
                picker.show();
            }
        });
    }

    //sets delivery address to addresses in client profile
    private void initDeliveryAddress() {
        presenter.setAddresses();
    }

    public void backHome(){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        finish();
    }
}