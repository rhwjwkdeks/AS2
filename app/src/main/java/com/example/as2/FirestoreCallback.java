package com.example.as2;

//use this interface to have thread wait until values are retrieved from database
public interface FirestoreCallback {
    //returns address1 and address2
    void onCallback(String[] addresses);
    //returns suggested price and total price when calculation is complete
    void onCallback(double svalue, double tvalue);
}
