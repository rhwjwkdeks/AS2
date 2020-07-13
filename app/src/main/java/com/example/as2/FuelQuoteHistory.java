package com.example.as2;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class FuelQuoteHistory extends AppCompatActivity {
//    Log.d();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    ArrayList<FuelQuote> fuelQuotes = new ArrayList<FuelQuote>(1);
    FuelQuoteHistoryAdapter adapter;

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID;

    String TAG = "FuelQuoteHistory";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("FQH - onCreate", "BEGIN");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_fuel_quote_history);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


        recyclerView = (RecyclerView) findViewById(R.id.fuelQuoteHistoryRecyclerView);
//        recyclerView.setHasFixedSize(true);

//        fuelQuotes = FuelQuote.createFuelQuoteHistoryList(10);
        queryDatabase();
        adapter = new FuelQuoteHistoryAdapter(fuelQuotes);
        recyclerView.setAdapter(adapter);


        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

//        FirestoreRecyclerOptions<FuelQuote> options = new FirestoreRecyclerOptions.Builder<FuelQuote>()
//                .setQuery(query, FuelQuote.class)
//                .build();
    }

    void queryDatabase() {

        // test UID: y968ZYKigxX6mjwgFTvLkFaIIRA3

//        fStore = FirebaseFirestore.getInstance(); // DB instance
        userID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid(); // current logged in UID

        fStore.collection("fuelQuoteHistory")

                .whereEqualTo("userID", userID) // only shows us fuel quotes that we requested

                .get()

                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //FuelQuote(String date_, String address_, float gallonsRequested_, float pricePerGallon_, float totalDue_)
                                fuelQuotes.add(new FuelQuote(document.get("date").toString(), document.get("address").toString(), Float.parseFloat(document.get("gallonsRequested").toString()), Float.parseFloat(document.get("pricePerGallon").toString()), Float.parseFloat(document.get("totalDue").toString())));
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}