package com.example.as2;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.*;
import java.util.function.Supplier;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

import static org.junit.Assert.*;

public class FuelQuoteHistoryUnitTest {

    FirebaseFirestore fStore;

    FuelQuote fuelQuote = new FuelQuote("10/01/1971", "100 World Drive", 4.07f, 0.33f, 1.34f);

    ArrayList<FuelQuote> fuelQuoteList = new ArrayList<FuelQuote>(1);

    FuelQuoteHistoryAdapter fuelQuoteHistoryAdapter = new FuelQuoteHistoryAdapter(fuelQuoteList);

    String testUserID = "y968ZYKigxX6mjwgFTvLkFaIIRA3";

    @Test
    public void testRecyclerViewCount() {
        fuelQuoteList.add(fuelQuote); // sets initial capacity to 1

        int result = fuelQuoteHistoryAdapter.getItemCount();
        assertEquals(1, result);
    }

    @Test
    public void testDatabaseConnection() throws Exception {
//        try {
//
//
//            fStore.collection("fuelQuoteHistory")
//
//                    .whereEqualTo("userID", testUserID)
//
//                    .get()
//
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if (task.isSuccessful()) {
//                                assertEquals(1,1);
//                            } else {
//                                assertEquals(1, 2);
//                            }
//                        }
//                    });
//        } catch (Exception e) {
//            assertEquals(1,2);
//        }
    }

}
