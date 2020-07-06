package com.example.as2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Clientprofile extends AppCompatActivity {
    private Button back_Button;
    private Button confirm_Button;

    SharedPreferences SP;
    EditText fullName, Address1, Address2, City, Zip;
    Spinner State;

    public static final String mypreference = "mypref";
    public static final String FN = "fullnameKey";
    public static final String AD1 = "ad1Key";
    public static final String AD2 = "ad2Key";
    public static final String CITY = "cityKey";
    public static final String ZIP = "zipKey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientprofile);

        fullName = findViewById(R.id.fullnameEditText); // up to 50 char
        Address1 = findViewById(R.id.address1EditText); // up to 100 char
        Address2 = findViewById(R.id.address2EditText); // up to 100 char
        City = findViewById(R.id.cityEditText);         // up to 100 char
        Zip = findViewById(R.id.zipcodeEditText);
        confirm_Button = findViewById(R.id.confirmButton);
        State = findViewById(R.id.stateSpinner);

        SP = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        if(SP.contains(FN)){
            fullName.setText(SP.getString(FN, ""));
        }
        if(SP.contains(AD1)){
            Address1.setText(SP.getString(AD1, ""));
        }
        if(SP.contains(AD2)){
            Address2.setText(SP.getString(AD2, ""));
        }
        if(SP.contains(CITY)){
            City.setText(SP.getString(CITY, ""));
        }
        if(SP.contains(ZIP)){
            Zip.setText(SP.getString(ZIP, ""));
        }


        confirm_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = fullName.getText().toString().trim();
                String ad1 = Address1.getText().toString().trim();
                String ad2 = Address2.getText().toString().trim();
                String city = City.getText().toString().trim();
                String zip = Zip.getText().toString().trim();
                String state = String.valueOf(State.getSelectedItem());

                if(state == "Select your state" || state.length() < 1){
                    TextView errorText = (TextView)State.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);
                    errorText.setText("Select state");
                }

                if(name.length() > 50 || name.length() < 1){
                    fullName.setError("Full name length must be same or less than 50 characters");
                    return;
                }


                if(ad1.length() > 100 || ad1.length() < 1){
                    Address1.setError("Address1 length must be same or less than 100 characters");
                    return;
                }


                if(ad2.length() > 100){
                    Address2.setError("Address2 length must be same or less than 100 characters");
                    return;
                }


                if(city.length() > 100 || city.length() < 1){
                    City.setError("City length must be same or less than 100 characters");
                    return;
                }

                if(zip.length() != 5 && zip.length() != 9){
                    Zip.setError("Zip code must be five or nine length");
                    return;
                }

                SharedPreferences.Editor editor = SP.edit();
                editor.putString(FN, name);
                editor.putString(AD1, ad1);
                editor.putString(AD2, ad2);
                editor.putString(CITY, city);
                editor.putString(ZIP, zip);
                editor.commit();

            }
        });

        back_Button = (Button) findViewById(R.id.backButton);
        back_Button.setOnClickListener(new View.OnClickListener() {
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

    public String validateProfile(String state, String name, String ad1, String ad2, String city, String zip) {
        if(state == "Select your state" || state.length() < 1){
            return "Invalid State";
        }

        if(name.length() > 50 || name.length() < 1){
            return ("Full name length must be same or less than 50 characters");
        }


        if(ad1.length() > 100 || ad1.length() < 1){
            return("Address1 length must be same or less than 100 characters");
        }


        if(ad2.length() > 100){
            return ("Address2 length must be same or less than 100 characters");
        }


        if(city.length() > 100 || city.length() < 1){
            return("City length must be same or less than 100 characters");
        }

        if(zip.length() != 5 && zip.length() != 9){
            return("Zip code must be five or nine length");
        }
        return "Profile Completed!";
    }
}





