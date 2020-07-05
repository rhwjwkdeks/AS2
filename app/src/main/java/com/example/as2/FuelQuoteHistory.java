package com.example.as2;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class FuelQuoteHistory extends AppCompatActivity {

    // TODO: RecyclerView will contain buttons to dates w/ basic info, retrieve info from database on click and open new activity to show all info

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_quote_history);

        FragmentManager fm = getSupportFragmentManager();
        ListFragment listFragment_ = (ListFragment)fm.findFragmentById(R.id.container_main);

        if (listFragment_ == null) {
            listFragment_ = ListFragment.newInstance("TODO", "TODO");

            fm.beginTransaction()
                    .add(R.id.container_main, listFragment_)
                    .commit();
        }
    }
}