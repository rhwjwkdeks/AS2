package com.example.as2;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FuelQuoteFormDatabase implements FuelQuoteFormMVP.Model {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private String userID;
    private double gal;
    private String date;
    private String address;
    private double svalue;
    private double tvalue;

    private final String TAG = "FuelQuoteForm";

    public FuelQuoteFormDatabase() {
        this.userID = Objects.requireNonNull(firebaseAuth.getCurrentUser().getUid());
        gal = 0;
        date = "1-1-90";
        address = "";
        svalue = 0;
        tvalue = 0;
    }

    public FuelQuoteFormDatabase(double gal, String date, String address, double svalue, double tvalue) {
        this.userID = Objects.requireNonNull(firebaseAuth.getCurrentUser().getUid());
        this.gal = gal;
        this.date = date;
        this.address = address;
        this.svalue = svalue;
        this.tvalue = tvalue;
    }

    //getters and setters

    @Override
    public String getUserID() {
        return userID;
    }

    @Override
    public double getGallons() {
        return gal;
    }

    @Override
    public String getDate() {
        return date;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public double getSuggestedPrice() {
        return svalue;
    }

    @Override
    public double getTotalPrice() {
        return tvalue;
    }

    @Override
    public void accessAddresses(final FirestoreCallback callback) {
        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String ad1 = Objects.requireNonNull(task.getResult().getString("Address1"));
                    String ad2 = Objects.requireNonNull(task.getResult().getString("Address2"));
                    String[] options;
                    if (ad2.isEmpty()) {
                        options = new String[]{ad1};
                    } else {
                        options = new String[]{ad1, ad2};
                    }
                    callback.onCallback(options);
                }
            }
        });
    }

    @Override
    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    public void setGallons(double gal) {
        this.gal = gal;
    }

    @Override
    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public void setSuggestedPrice(double suggestedPrice) {
        this.svalue = suggestedPrice;
    }

    @Override
    public void setTotalPrice(double totalPrice) {
        this.tvalue = totalPrice;
    }

    @Override
    //returns an array containing the suggested and total price of gas
    public void pricingModule(final FirestoreCallback callback) {
        final double gallons = this.gal;
        final double gallonsRequestedFactor = gallons > 1000 ? 0.02 : 0.03;
        final double currentPrice = 1.50;
        //query database to get the user's state
        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    final String state = Objects.requireNonNull(task.getResult().getString("State"));
                    //query database to see if user has a fuel quote history
                    firebaseFirestore.collection("fuelQuoteHistory").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                double locationFactor = state.equals("Texas") ? 0.02 : 0.04;
                                double rateHistoryFactor = Objects.requireNonNull(task.getResult().size()) == 0 ? 0 : 0.01;
                                double margin = currentPrice * (locationFactor - rateHistoryFactor + gallonsRequestedFactor + 0.1);
                                svalue = currentPrice + margin;
                                Log.d(TAG, "pricing module: " + svalue);
                                tvalue = gallons * svalue;
                                Log.d(TAG, "Pricing module: " + tvalue);
                                callback.onCallback(svalue, tvalue);
                            } else {
                                Log.d(TAG, "error retrieving fuel quote history from database");
                            }
                        }
                    });
                } else {
                    Log.d(TAG, "error retrieving state from database");
                }
            }
        });
    }

    //store set fields in Database
    public void storeFieldsInDatabase() {
        CollectionReference collectionReference = firebaseFirestore.collection("fuelQuoteHistory");
        Map<String, Object> data = new HashMap<>();
        data.put("gallonsRequested", gal);
        data.put("date", date);
        data.put("address", address);
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
    }
}
