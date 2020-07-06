package com.example.as2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FuelQuoteHistoryAdapter extends RecyclerView.Adapter<FuelQuoteHistoryAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView totalDue;
        public TextView gallonsRequested;
        public TextView date;
        public TextView address;
        public TextView pricePerGallon;

        public ViewHolder(View view) {
            super(view);
            totalDue = (TextView) view.findViewById(R.id.fqh_total_due);
            gallonsRequested = (TextView) view.findViewById(R.id.fqh_gallons_requested);
            date = (TextView) view.findViewById(R.id.fqh_date);
            address = (TextView) view.findViewById(R.id.fqh_address);
            pricePerGallon = (TextView) view.findViewById(R.id.fqh_price_per_gallon);

        }
    }

    private List<FuelQuote> fuelQuoteList;

    public FuelQuoteHistoryAdapter(List<FuelQuote> fuelQuoteList_) {
        fuelQuoteList = fuelQuoteList_;
    }

    @NonNull
    @Override
    public FuelQuoteHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View fuelQuoteView = inflater.inflate(R.layout.fragment_fuel_quote, parent, false);

        ViewHolder viewHolder = new ViewHolder(fuelQuoteView);

        return viewHolder;

        /*
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_contact, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
         */
    }

    @Override
    public void onBindViewHolder(@NonNull FuelQuoteHistoryAdapter.ViewHolder holder, int position) {
        FuelQuote fuelQuote = fuelQuoteList.get(position);

        TextView totalDue = holder.totalDue;
        TextView gallonsRequested = holder.gallonsRequested;
        TextView date = holder.date;
        TextView address = holder.address;
        TextView pricePerGallon = holder.pricePerGallon;

        totalDue.setText(Float.toString(fuelQuote.getTotalDue()));
        gallonsRequested.setText(Float.toString(fuelQuote.getGallonsRequested()));
        date.setText(fuelQuote.getDate());
        address.setText(fuelQuote.getAddress());
        pricePerGallon.setText(Float.toString(fuelQuote.getPricePerGallon()));
    }

    @Override
    public int getItemCount() {
        return fuelQuoteList.size();
    }
}
