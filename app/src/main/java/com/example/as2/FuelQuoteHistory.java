package com.example.as2;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FuelQuoteHistory extends AppCompatActivity {
//    Log.d();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    ArrayList<FuelQuote> fuelQuotes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("FQH - onCreate", "BEGIN");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_fuel_quote_history);


        recyclerView = (RecyclerView) findViewById(R.id.fuelQuoteHistoryRecyclerView);
//        recyclerView.setHasFixedSize(true);
        Log.d("FQH - onCreate", "1");

        fuelQuotes = FuelQuote.createFuelQuoteHistoryList(10);
        Log.d("FQH - onCreate", "2");

        FuelQuoteHistoryAdapter adapter = new FuelQuoteHistoryAdapter(fuelQuotes);
        recyclerView.setAdapter(adapter);
        Log.d("FQH - onCreate", "3");


        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Log.d("FQH - onCreate", "4");
    }
}