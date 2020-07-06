package com.example.as2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

import static com.example.as2.Clientprofile.mypreference;
import static com.example.as2.Clientprofile.AD1;

public class FuelQuoteForm extends AppCompatActivity {
    EditText gallons_value;
    DatePicker date_value;
    TextView address_value, sprice_value, tprice_value;
    SharedPreferences sp;

    private Button submit_button, back_button;

    public static final String mypref = "mypref";
    public static final String gallons = "gallonsKey";
    public static final String address = "addressKey";
    public static final String date = "dateKey";
    public static final String sprice = "spriceKey";
    public static final String tprice = "tpriceKey";


    final Calendar currentDate = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_quote_form);
        gallons_value = (EditText) findViewById(R.id.gallons_value);
        gallons_value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String edit_text = editable.toString();
                if (!edit_text.isEmpty()) {
                    int gal = Integer.parseInt(edit_text);
                    String str_price = sprice_value.getText().toString().substring(1);
                    double price = Double.parseDouble(str_price);
                    double total_price = gal * price;
                    String t_price = "$" + total_price;
                    tprice_value.setText(t_price);
                }
            }
        });
        initCurrentDate();
        initDeliveryAddress();
        sprice_value = (TextView) findViewById(R.id.sprice_value);
        //To be calculated via Pricing Module
        sprice_value.setText("$2.00");
        tprice_value = (TextView) findViewById(R.id.tprice_value);
        sp = getSharedPreferences(mypref, Context.MODE_PRIVATE);
        submit_button = (Button) findViewById(R.id.submit_button);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String g = gallons_value.getText().toString();
                if (g.length() < 1) {
                    gallons_value.setError("Please enter the number of gallons.");
                    return;
                }
                String a = address_value.getText().toString().trim();
                String s = sprice_value.getText().toString();
                String t = tprice_value.getText().toString();
                int selectedYear = date_value.getYear();
                int selectedMonth = date_value.getMonth();
                int selectedDay =  date_value.getDayOfMonth();
                String prefDate = selectedYear + "-" + selectedMonth + "-" + selectedDay;

                SharedPreferences.Editor editor = sp.edit();
                editor.putString(gallons, g);
                editor.putString(address, a);
                editor.putString(sprice, s);
                editor.putString(tprice, t);
                editor.putString(date, prefDate);
                editor.commit();
            }
        });
        back_button = (Button) findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backHome();
            }
        });

    }
    public void initCurrentDate() {
        date_value = (DatePicker) findViewById(R.id.date_value);
        final int year = currentDate.get(Calendar.YEAR);
        final int month = currentDate.get(Calendar.MONTH);
        final int day = currentDate.get(Calendar.DAY_OF_MONTH);
        date_value.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int y, int m, int d) {
                if (y > year)
                    view.updateDate(year, month, day);

                if (m > month && y == year)
                    view.updateDate(year, month, day);

                if (d > day && y == year && m == month)
                    view.updateDate(year, month, day);
            }
        });
    }
    public void initDeliveryAddress() {
        address_value = (TextView) findViewById(R.id.address_value);
        SharedPreferences sp = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        String address = sp.getString(AD1, "");
        address_value.setText(address);
    }
    public void backHome(){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }
}