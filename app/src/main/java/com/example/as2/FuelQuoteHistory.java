package com.example.as2;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.view.View;

public class FuelQuoteHistory extends AppCompatActivity {

    // TODO: RecyclerView will contain buttons to dates w/ basic info, retrieve info from database on click and open new activity to show all info

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_quote_history);

        FragmentManager fm = getSupportFragmentManager();
    }
}