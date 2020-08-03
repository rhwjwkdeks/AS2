package com.example.as2;

import com.google.firebase.firestore.FirebaseFirestore;

public interface FuelQuoteFormMVP {
    interface View {
        //pop up dialogs if there is an error in a field
        void setGallonsError();
        void setDateError();
        void setAddressError();
        void setProfileError();
        //pop up if user access form without login
        void setUserError();
        //initialize date & addresses
        void setDate(String date);
        void setAddresses(String[] addresses);
        //set price when gallons are entered
        void setSuggestedPrice(String suggestedPrice);
        void setTotalPrice(String totalPrice);
        //after successfully submitting a form text
        void successSubmit();
    }
    interface Model {
        String getUserID();
        double getGallons();
        String getDate();
        //return chosen address on form
        String getAddress();
        //tells model to access address1 and address2 in DB and store in callback
        void accessAddresses(FirestoreCallback callback);
        double getSuggestedPrice();
        double getTotalPrice();
        void setUserID(String userID);
        void setGallons(double gallons);
        void setDate(String date);
        void setAddress(String address);
        void setSuggestedPrice(double suggestedPrice);
        void setTotalPrice(double totalPrice);
        //calculates suggested price & total price
        void pricingModule(FirestoreCallback callback);
        //stores set fields in FuelQuoteHistory & return document ID
        void storeFieldsInDatabase();
    }
    interface Presenter {
        //checks for any errors in fields
        //also tells model to upload fields to database if no errors
        String onSubmit(String gallons, String address, String date, String s, String t);
        //check for valid number of gallons
        double validateGallons(final String gallons);
        //check for valid date
        String validateDate(int year, int month, int day);
        //check if client has a profile and if client log in before using form
        void validateProfile();
        //tells model to look up address1 and address2 and sends them to view
        void setAddresses();
        //tells Model to calculate suggested price & total price via Pricing Module
        void calculatePrices();
    }
}
