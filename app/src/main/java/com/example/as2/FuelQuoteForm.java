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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class FuelQuoteForm extends AppCompatActivity {
    private EditText gallons_value;
    private TextView date_value;
    DatePickerDialog picker;
    private TextView sprice_value, tprice_value;
    private Spinner address_value;

    private Button submit_button, back_button;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    private String userID;
    private final String TAG = "FuelQuoteForm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_quote_form);
        firebaseAuth = FirebaseAuth.getInstance();
        try {
            userID = Objects.requireNonNull(firebaseAuth.getCurrentUser().getUid(), "Error fetching user ID");
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "There was an error in fetching the user ID. Please exit the app and login again.", Toast.LENGTH_LONG).show();
            backHome();
        }
        firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference c = firebaseFirestore.collection("users");
        c.whereEqualTo(FieldPath.documentId(), userID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if(task.getResult().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please create a Client Profile before requesting a Fuel Quote.", Toast.LENGTH_LONG).show();
                        backHome();
                    }
                }
            }
        });
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

            @Override
            public void afterTextChanged(Editable editable) {
                String edit_text = editable.toString();
                if (!edit_text.isEmpty()) {
                    double parse_gal = 0;
                    try {
                        parse_gal = Double.parseDouble(edit_text);
                    }
                    catch (Exception e) {
                        gallons_value.setError("Please enter a valid number.");
                    }
                    if (parse_gal > 0) {
                        final double gal = parse_gal;
                        final double gallonsRequestedFactor = gal > 1000 ? 0.02 : 0.03;
                        Log.d(TAG, "" + gallonsRequestedFactor);
                        pricingModule(gallonsRequestedFactor, gal);
                    }
                    else {
                        gallons_value.setError("Gallons must be greater than 0.");
                        sprice_value.setText("$0.00");
                        tprice_value.setText("$0.00");
                    }
                }
            }
        });
        initCurrentDate();
        initDeliveryAddress();
        //add validation on submit before adding info to database
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String g = gallons_value.getText().toString();
                double gvalue = 0;
                boolean enteredGallons = true;
                try {
                    gvalue = Double.parseDouble(g);
                }
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Please enter a valid number of gallons.", Toast.LENGTH_SHORT).show();
                    enteredGallons = false;
                }
                if(gvalue <= 0) {
                    Toast.makeText(getApplicationContext(), "Please enter a valid number of gallons.", Toast.LENGTH_SHORT).show();
                    enteredGallons = false;
                }
                if(enteredGallons) {
                    String a = address_value.getSelectedItem().toString().trim();
                    boolean enteredAddress = true;
                    if (a.equals("Select the address to deliver to...")) {
                        Toast.makeText(getApplicationContext(), "Please select your delivery address.", Toast.LENGTH_SHORT).show();
                        enteredAddress = false;
                    }
                    if (enteredAddress) {
                        String prefDate = date_value.getText().toString();
                        boolean enteredDate = true;
                        if (prefDate.isEmpty() || prefDate.matches(".*[a-z].*")) {
                            Toast.makeText(getApplicationContext(), "Please enter a valid date.", Toast.LENGTH_SHORT).show();
                            enteredDate = false;
                        }
                        if (enteredDate) {
                            String s = sprice_value.getText().toString().substring(1);
                            double svalue = Double.parseDouble(s);
                            String t = tprice_value.getText().toString().substring(1);
                            double tvalue = Double.parseDouble(t);
                            CollectionReference collectionReference = firebaseFirestore.collection("fuelQuoteHistory");
                            Map<String, Object> data = new HashMap<>();
                            data.put("gallonsRequested", gvalue);
                            data.put("date", prefDate);
                            data.put("address", a);
                            data.put("pricePerGallon", svalue);
                            data.put("totalDue", tvalue);
                            data.put("userID", userID);
                            collectionReference.add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
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
                            Toast.makeText(getApplicationContext(), "Form submitted successfully!", Toast.LENGTH_LONG).show();
                            backHome();
                        }
                    }
                }
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
                        if (y < year || (y == year && (m < month || (m == month && d < day)))) {
                            date_value.setText((month + 1) + "-" + day + "-" + year);
                            Toast.makeText(getApplicationContext(), "Delivery Date cannot be set before today.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            date_value.setText((m + 1) + "-" + d + "-" + y);
                        }
                    }
                }, year, month, day);
                picker.show();
            }
        });
    }
    //sets delivery address to addresses in client profile
    private void initDeliveryAddress() {
        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    String ad1 = task.getResult().getString("Address1");
                    String ad2 = task.getResult().getString("Address2");
                    String[] options;
                    if(ad2.isEmpty()) {
                        options = new String[] {"Select the address to deliver to...", ad1};
                    }
                    else {
                        options = new String[] {"Select the address to deliver to...", ad1, ad2};
                    }
                    Spinner s = findViewById(R.id.address_value);
                    ArrayAdapter<String> a = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, options);
                    a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    s.setAdapter(a);
                }
            }
        });
    }
    //updates the text of suggested and total price when number of gallons is entered.
    private void pricingModule(final double gallonsRequestedFactor, final double gal) {
        final double currentPrice = 1.50;
        //query database to get the user's state
        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    final String state = task.getResult().getString("State");
                    Log.d(TAG, "state success");
                    //query database to see if user has a fuel quote history
                    firebaseFirestore.collection("fuelQuoteHistory").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                double locationFactor = state.equals("Texas") ? 0.02 : 0.04;
                                double rateHistoryFactor = task.getResult().size() == 0 ? 0 : 0.01;
                                double margin = currentPrice * (locationFactor - rateHistoryFactor + gallonsRequestedFactor + 0.1);
                                double suggestedPrice = currentPrice + margin;
                                sprice_value.setText("$" + String.format("%.2f", suggestedPrice));
                                double total_price = gal * suggestedPrice;
                                tprice_value.setText("$" + String.format("%.2f", total_price));
                            } else {
                                Log.d(TAG, "error retrieving fuel quote history from database");
                            }
                        }
                    });
                } else {
                    Log.d(TAG, "error retrieving date from database");
                }
            }
        });
    }
    public void backHome(){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        finish();
    }
}