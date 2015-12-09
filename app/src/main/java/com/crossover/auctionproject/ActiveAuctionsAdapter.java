package com.crossover.auctionproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.crossover.auctionproject.database.AuctionItem;

import java.util.ArrayList;

/**
 * Created by stevyhacker on 9.12.15..
 */
public class ActiveAuctionsAdapter extends ArrayAdapter<AuctionItem> {

    LayoutInflater layoutinflater;
    Context context;
    ArrayList<AuctionItem> auctionItems;
    int layoutResID;

    public ActiveAuctionsAdapter(Context context, int resource, ArrayList<AuctionItem> auctionItems) {
        super(context, resource, auctionItems);
        this.context = context;
        layoutinflater = LayoutInflater.from(context);
        this.auctionItems = auctionItems;
        this.layoutResID = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        View view = convertView;

        if (view == null) {

            viewHolder = new ViewHolder();

            view = layoutinflater.inflate(layoutResID, parent, false);

            viewHolder.itemName = (TextView) view.findViewById(R.id.itemNameTextView);
            viewHolder.priceTextView = (TextView) view.findViewById(R.id.itemPriceTextView);
            viewHolder.highestBidderTextView = (TextView) view.findViewById(R.id.itemHighestBidderTextView);
            viewHolder.timeLeftTextView = (TextView) view.findViewById(R.id.timeLeftTextView);


            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();

        }

        AuctionItem auctionItem = (AuctionItem) this.auctionItems.get(position);

        viewHolder.itemName.setText(auctionItem.getName());
        viewHolder.highestBidderTextView.setText(auctionItem.getHighest_bidder());
        viewHolder.timeLeftTextView.setText(String.valueOf(auctionItem.getDays_active()));
        viewHolder.priceTextView.setText(String.valueOf(auctionItem.getHighest_bid()));


        return view;
    }

    private static class ViewHolder {
        TextView itemName;
        TextView priceTextView;
        TextView highestBidderTextView;
        TextView timeLeftTextView;
    }
}
