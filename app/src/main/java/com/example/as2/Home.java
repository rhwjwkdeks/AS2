package com.example.as2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {
    private Button button;
    private Button fuelQuoteButton;
    private Button fuelQuoteHistoryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        button = (Button) findViewById(R.id.clientProfileButton);
        fuelQuoteButton = (Button) findViewById(R.id.fuelQuoteButton);
        fuelQuoteHistoryButton = (Button) findViewById(R.id.historyButton);

        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                openClientProfile();
            }
        });
        fuelQuoteButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                openFuelQuote();
            }
        });
        fuelQuoteHistoryButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                openFuelQuoteHistory();
            }
        });
    }
    public void openClientProfile(){
        Intent intent = new Intent(this, Clientprofile.class);
        startActivity(intent);
    }

    public void openFuelQuote(){
        Intent intent = new Intent(this, FuelQuoteForm.class);
        startActivity(intent);
    }

    public void openFuelQuoteHistory(){
        Intent intent = new Intent(this, FuelQuoteHistory.class);
        startActivity(intent);
    }

    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent (getApplicationContext(), MainActivity.class));
        finish();
    }
}