package com.example.as2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class Clientprofile extends AppCompatActivity {
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientprofile);

        button = (Button) findViewById(R.id.backButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backHome();
            }
        });


        Spinner mySpinner = (Spinner) findViewById(R.id.stateSpinner);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(Clientprofile.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.states));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);
    }

    public void backHome(){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }
}































