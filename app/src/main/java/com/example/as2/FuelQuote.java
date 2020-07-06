package com.example.as2;

import java.util.ArrayList;

public class FuelQuote {

    /*
    public TextView totalDue;
        public TextView gallonsRequested;
        public TextView date;
        public TextView address;
        public TextView pricePerGallon;
     */

    private float totalDue;
    private float gallonsRequested;
    private String date;
    private String address;
    private float pricePerGallon;

    public static int numOfQuotes = 0;


    public FuelQuote() { // default constructor for generating pseudo-content
        totalDue = numOfQuotes * 3;
        gallonsRequested = totalDue / 2;
        date = "01/01/1970";
        address = "1 Infinite Loop";
        pricePerGallon = gallonsRequested * 3;
    }
    public FuelQuote(String date_, String address_, float gallonsRequested_, float pricePerGallon_, float totalDue_) {
        totalDue = totalDue_;
        gallonsRequested = gallonsRequested_;
        date = date_;
        address = address_;
        pricePerGallon = pricePerGallon_;
    }

    // generates fake content for testing purposes
    public static ArrayList<FuelQuote> createFuelQuoteHistoryList(int numQuotes) {
        ArrayList<FuelQuote> fuelQuotes = new ArrayList<FuelQuote>();

        for (int i = 1; i <= numQuotes; i++) {
            fuelQuotes.add(new FuelQuote());
            numOfQuotes++;
        }

        return fuelQuotes;
    }

    public float getTotalDue() {
        return totalDue;
    }

    public void setTotalDue(float totalDue) {
        this.totalDue = totalDue;
    }

    public float getGallonsRequested() {
        return gallonsRequested;
    }

    public void setGallonsRequested(float gallonsRequested) {
        this.gallonsRequested = gallonsRequested;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getPricePerGallon() {
        return pricePerGallon;
    }

    public void setPricePerGallon(float pricePerGallon) {
        this.pricePerGallon = pricePerGallon;
    }
}
