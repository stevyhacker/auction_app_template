package com.crossover.auctionproject.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.crossover.auctionproject.R;
import com.crossover.auctionproject.database.AuctionItem;
import com.crossover.auctionproject.database.DatabaseAdapter;

import java.util.ArrayList;

/**
 * Created by stevyhacker on 9.12.15..
 */
public class WonAuctionsFragment extends Fragment {

    ListView wonAuctionsListView;
    WonAuctionsAdapter adapter;
    ArrayList<AuctionItem> auctionItemList;
    PreferencesHelper prefs;
    DatabaseAdapter db;

    public WonAuctionsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_of_wonauctions,
                container, false);

        prefs = new PreferencesHelper(getActivity());
        db = new DatabaseAdapter(getActivity());

        wonAuctionsListView = (ListView) view.findViewById(R.id.wonAuctionsListView);
        auctionItemList = db.getWonAuctions(prefs.getString("currentUser", "noUser"));

        TextView emptyAuctionTextView = (TextView) view.findViewById(R.id.bidding_items_list_empty);
        wonAuctionsListView.setEmptyView(emptyAuctionTextView);

        adapter = new WonAuctionsAdapter(getActivity(), R.layout.won_auctions_item,auctionItemList);
        wonAuctionsListView.setAdapter(adapter);


        adapter.notifyDataSetChanged();


        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getString(R.string.won_auctions));

    }

}
