package com.crossover.auctionproject;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.crossover.auctionproject.database.AuctionItem;
import com.crossover.auctionproject.database.DatabaseAdapter;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ActiveAuctionsFragment extends Fragment {
    ListView activeAuctionsListView;
    ActiveAuctionsAdapter adapter;
    ArrayList<AuctionItem> auctionItemList;
    PreferencesHelper prefs;
    DatabaseAdapter db;

    public ActiveAuctionsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_of_auctions,
                container, false);

        prefs = new PreferencesHelper(getActivity());
        db = new DatabaseAdapter(getActivity());
        activeAuctionsListView = (ListView) view.findViewById(R.id.activeAuctionsListView);
        auctionItemList = new ArrayList<AuctionItem>();
        auctionItemList = db.getAvailableAuctions(prefs.getString("currentUser", "noUser"));

        adapter = new ActiveAuctionsAdapter(getActivity(), R.layout.active_auctions_item,
                auctionItemList);
        activeAuctionsListView.setAdapter(adapter);


        for (AuctionItem item : auctionItemList) {
            Log.e("ADATPER DEBUG", item.created_by + item.highest_bid);
        }

        adapter.notifyDataSetChanged();


        activeAuctionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //todo add bidding
//                Intent intent = new Intent(getActivity(), SingleAuctionItemActivity.class);
//
//                intent.putExtra("id", auctionItemList.get(position).getId());
//                startActivity(intent);
            }
        });


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
        getActivity().setTitle(getString(R.string.active_auctions));

    }

}
