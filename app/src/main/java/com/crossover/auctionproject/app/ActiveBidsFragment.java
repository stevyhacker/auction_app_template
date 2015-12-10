package com.crossover.auctionproject.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crossover.auctionproject.R;
import com.crossover.auctionproject.database.AuctionItem;
import com.crossover.auctionproject.database.DatabaseAdapter;

import java.util.ArrayList;

/**
 * Created by stevyhacker on 10.12.15..
 */
public class ActiveBidsFragment extends Fragment {


    ListView activeBidsListView;
    ActiveBidsAdapter adapter;
    ArrayList<AuctionItem> auctionItemList;
    PreferencesHelper prefs;
    DatabaseAdapter db;

    public ActiveBidsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_of_activebids,
                container, false);

        prefs = new PreferencesHelper(getActivity());
        db = new DatabaseAdapter(getActivity());
        activeBidsListView = (ListView) view.findViewById(R.id.activeBidsListView);
        TextView emptyAuctionTextView = (TextView) view.findViewById(R.id.bidding_items_list_empty);
        auctionItemList = new ArrayList<AuctionItem>();
        auctionItemList = db.getActiveBids(prefs.getString("currentUser", "noUser"));
        //todo debug

        for (AuctionItem item : auctionItemList) {
            Log.e("AUCTION ITEM DBG", item.name);
            Toast.makeText(getContext(),"AUCTION ITEM NAME " + item.name,Toast.LENGTH_SHORT).show();
        }
        adapter = new ActiveBidsAdapter(getActivity(), R.layout.active_bids_item,
                auctionItemList);
        activeBidsListView.setEmptyView(emptyAuctionTextView);
        activeBidsListView.setAdapter(adapter);


        adapter.notifyDataSetChanged();


        activeBidsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        getActivity().setTitle(getString(R.string.active_bids));

    }

}
