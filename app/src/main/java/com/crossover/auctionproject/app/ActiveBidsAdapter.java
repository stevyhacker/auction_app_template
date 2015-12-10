package com.crossover.auctionproject.app;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.crossover.auctionproject.R;
import com.crossover.auctionproject.database.AuctionItem;

import java.util.ArrayList;

/**
 * Created by stevyhacker on 10.12.15..
 */
public class ActiveBidsAdapter extends ArrayAdapter<AuctionItem> {
    LayoutInflater layoutinflater;
    Context context;
    ArrayList<AuctionItem> auctionItems;
    int layoutResID;

    public ActiveBidsAdapter(Context context, int resource, ArrayList<AuctionItem> auctionItems) {
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

        AuctionItem auctionItem = auctionItems.get(position);

        viewHolder.itemName.setText(auctionItem.getName());
        if (auctionItem.highest_bidder.equalsIgnoreCase(auctionItem.created_by)) {
            viewHolder.highestBidderTextView.setText("Submitted by: " + auctionItem.getHighest_bidder());
        } else {
            viewHolder.highestBidderTextView.setText("Highest bidder: " + auctionItem.getHighest_bidder());
        }
        if (auctionItem.getHours_active() > 1) {
            viewHolder.timeLeftTextView.setText(String.valueOf(auctionItem.getHours_active()) + " hours left");
        } else {
            viewHolder.timeLeftTextView.setText(1 + " hour left");
        }
        viewHolder.priceTextView.setText(String.valueOf(auctionItem.getHighest_bid()) + " $");

        return view;
    }

    private static class ViewHolder {
        TextView itemName;
        TextView priceTextView;
        TextView highestBidderTextView;
        TextView timeLeftTextView;
    }
}
