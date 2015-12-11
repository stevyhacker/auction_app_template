package com.crossover.auctionproject.app;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crossover.auctionproject.R;
import com.crossover.auctionproject.database.AuctionItem;
import com.crossover.auctionproject.database.DatabaseAdapter;
import com.crossover.auctionproject.database.UserItem;

import java.util.ArrayList;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllAuctionsFragment extends Fragment {
    ListView activeAuctionsListView;
    AllAuctionsAdapter adapter;
    ArrayList<AuctionItem> auctionItemList;
    PreferencesHelper prefs;
    DatabaseAdapter db;

    public AllAuctionsFragment() {
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
        TextView emptyAuctionTextView = (TextView) view.findViewById(R.id.bidding_items_list_empty);


        auctionItemList = new ArrayList<AuctionItem>();
        auctionItemList = db.getAvailableAuctions(prefs.getString("currentUser", "noUser"));

        adapter = new AllAuctionsAdapter(getActivity(), R.layout.active_auctions_item,
                auctionItemList);
        activeAuctionsListView.setEmptyView(emptyAuctionTextView);
        activeAuctionsListView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        activeAuctionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final AuctionItem auctionItem = auctionItemList.get(position);


                TextInputLayout inputTextLayout = new TextInputLayout(getActivity());
                final EditText yourBidEditText = new EditText(getActivity());
                yourBidEditText.setHint("Your bid: ");
                yourBidEditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                inputTextLayout.addView(yourBidEditText);

                AlertDialog.Builder bidDialogBuilder = new AlertDialog.Builder(getActivity());
                bidDialogBuilder.setTitle("Item: " + auctionItem.getName());
                bidDialogBuilder.setMessage("Highest bid: " + auctionItem.getHighest_bid() + " $");
                bidDialogBuilder.setView(inputTextLayout, 20, 10, 20, 10);
                bidDialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Double.parseDouble(yourBidEditText.getText().toString()) > auctionItem.getHighest_bid()) {
                            auctionItem.setHighest_bid(Double.parseDouble(yourBidEditText.getText().toString()));
                            auctionItem.setHighest_bidder(prefs.getString("currentUser", "noUser"));
                            UserItem user = db.getUserItem(prefs.getString("currentUser", "noUser"));
                            ArrayList<Integer> allbids = user.getAll_bids();
                            allbids.add(auctionItem.getId());
                            user.setAll_bids(allbids);
                            db.updateUserItem(user);

                            db.updateAuctionItem(auctionItem);
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();

                        } else {
                            Toast.makeText(getActivity(), "Your bid needs to be higher than the current bid.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                bidDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                bidDialogBuilder.create().show();


            }
        });
        mHandler = new Handler();
        startRepeatingTask();

        return view;
    }

    //Handler for internal user bot
    private int mInterval = 30000; //30 seconds
    private Handler mHandler;

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            randomBid();
            auctionItemList.clear();
            auctionItemList.addAll(db.getAvailableAuctions(prefs.getString("currentUser", "noUser")));
            adapter.notifyDataSetChanged();
            mHandler.postDelayed(mStatusChecker, mInterval);
        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }


    private void randomBid() {
        ArrayList<AuctionItem> allAuctions = db.getAllAuctions();
        if (allAuctions.size() > 0) {

            Random random = new Random();
            int randomInt = random.nextInt(allAuctions.size());

            AuctionItem auctionItem = allAuctions.get(randomInt);

            auctionItem.setHighest_bid(auctionItem.getHighest_bid() + random.nextInt(10 - 5) + 5);
            auctionItem.setHighest_bidder("internalBot");

            db.updateAuctionItem(auctionItem);

        }
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
