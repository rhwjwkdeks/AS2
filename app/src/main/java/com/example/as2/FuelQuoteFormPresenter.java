package com.example.as2;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FuelQuoteFormPresenter implements FuelQuoteFormMVP.Presenter {
    private FuelQuoteFormMVP.Model db;
    private FuelQuoteFormMVP.View view;

    private final String TAG = "FuelQuoteFormPresenter";

    public FuelQuoteFormPresenter(FuelQuoteFormMVP.View view) {
        this.view = view;
        db = new FuelQuoteFormDatabase();
    }

    @Override
    public void validateGallons(final String galText) {
        //return 0 or -1 if invalid string, gal otherwise
        if (!galText.isEmpty()) {
            double parse_gal = 0;
            try {
                parse_gal = Double.parseDouble(galText);
            } catch (Exception e) {
                //error in parsing double
                Log.d(TAG, "error in parsing double");
                view.setGallonsError();
            }
            if (parse_gal <= 0) {
                //gallons <= 0 is not allowed
                Log.d(TAG, "gallons <= 0");
            }
            db.setGallons(parse_gal);
        } else {
            //empty string
            Log.d(TAG, "empty gallons");
            view.setGallonsError();
        }
    }

    @Override
    public void validateDate(int y, int m, int d) {
        final Calendar currentDate = Calendar.getInstance();
        final int year = currentDate.get(Calendar.YEAR);
        final int month = currentDate.get(Calendar.MONTH);
        final int day = currentDate.get(Calendar.DAY_OF_MONTH);
        if (y < year || (y == year && (m < month || (m == month && d < day)))) {
            view.setDate((month + 1) + "-" + day + "-" + year);
            view.setDateError();
        } else {
            view.setDate((m + 1) + "-" + d + "-" + y);
        }
    }

    @Override
    public void onSubmit(String gallons, String address, String date, String s, String t) {
        double gvalue = 0;
        boolean enteredGallons = true;
        try {
            gvalue = Double.parseDouble(gallons);
        } catch (Exception e) {
            view.setGallonsError();
            enteredGallons = false;
        }
        if (gvalue <= 0) {
            view.setGallonsError();
            enteredGallons = false;
        }
        if (enteredGallons) {
            boolean enteredAddress = true;
            if (address.equals("Select the address to deliver to...")) {
                view.setAddressError();
                enteredAddress = false;
            }
            if (enteredAddress) {
                boolean enteredDate = true;
                if (date.isEmpty() || date.matches(".*[a-z].*")) {
                    view.setDateError();
                    enteredDate = false;
                }
                if (enteredDate) {
                    String stext = s.substring(1);
                    String ttext = t.substring(1);
                    double svalue = Double.parseDouble(stext);
                    double tvalue = Double.parseDouble(ttext);
                    db.setGallons(gvalue);
                    db.setAddress(address);
                    db.setDate(date);
                    db.setSuggestedPrice(svalue);
                    db.setTotalPrice(tvalue);
                    db.storeFieldsInDatabase();
                    view.successSubmit();
                }
            }
        }
    }

    @Override
    public void validateProfile() {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        String userID = "";
        try {
            userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getUid(), "Error fetching user ID");
        }
        catch (Exception e) {
            view.setUserError();
        }
        CollectionReference c = firebaseFirestore.collection("users");
        c.whereEqualTo(FieldPath.documentId(), userID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().isEmpty()) {
                        view.setProfileError();
                    }
                }
            }
        });
    }

    @Override
    public void setAddresses() {
        db.accessAddresses(new FirestoreCallback() {
            @Override
            public void onCallback(String[] addresses) {
                String[] options;
                if (addresses[1].isEmpty()) {
                    options = new String[]{"Select the address to deliver to...", addresses[0]};
                    view.setAddresses(options);
                } else {
                    options = new String[]{"Select the address to deliver to...", addresses[0], addresses[1]};
                    view.setAddresses(options);
                }
            }

            @Override
            public void onCallback(double svalue, double tvalue) {

            }
        });
    }

    @Override
    public void calculatePrices() {
        db.pricingModule(new FirestoreCallback() {
            @Override
            public void onCallback(String[] addresses) {

            }

            @Override
            public void onCallback(double sprice, double tprice) {
                view.setSuggestedPrice("$" + String.format("%.2f", sprice));
                view.setTotalPrice("$" + String.format("%.2f", tprice));
            }
        });
    }
}
