package com.crossover.auctionproject.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crossover.auctionproject.R;
import com.crossover.auctionproject.database.AuctionItem;
import com.crossover.auctionproject.database.DatabaseAdapter;
import com.crossover.auctionproject.database.UserItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    DrawerLayout drawer;
    private DatabaseAdapter db;
    PreferencesHelper prefs;
    android.support.v7.app.AlertDialog addAuctionDialog;
    AlertDialog.Builder addAuctionDialogBuilder;
    EditText itemNameTextView;
    EditText hoursActiveTextView;
    EditText startingPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseAdapter(this);
        prefs = new PreferencesHelper(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        View addAuctionView = inflater.inflate(R.layout.add_auction_dialog_layout, null);
        startingPriceTextView = (EditText) addAuctionView.findViewById(R.id.startingPriceTextView);
        itemNameTextView = (EditText) addAuctionView.findViewById(R.id.itemNameTextView);
        hoursActiveTextView = (EditText) addAuctionView.findViewById(R.id.timeTextView);

        addAuctionDialogBuilder = new AlertDialog.Builder(this);
        addAuctionDialogBuilder.setTitle("Add item for auction");
        addAuctionDialogBuilder.setMessage("Enter your auction details:");
        addAuctionDialogBuilder.setCancelable(false);
        addAuctionDialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //THIS IS OVERRIDEN AFTER .show() BECAUSE OF DATA VALIDATION DIALOG CANCELING
                if (!TextUtils.isEmpty(itemNameTextView.getText()) && !TextUtils.isEmpty(hoursActiveTextView.getText()) && !TextUtils.isEmpty(startingPriceTextView.getText()) && Integer.parseInt(hoursActiveTextView.getText().toString()) > 0
                        && Double.parseDouble(startingPriceTextView.getText().toString()) > 0) {

                    AuctionItem auctionItem = new AuctionItem();
                    auctionItem.hours_active = Integer.parseInt(hoursActiveTextView.getText().toString());
                    auctionItem.starting_price = Double.parseDouble(startingPriceTextView.getText().toString());
                    auctionItem.highest_bid = Double.parseDouble(startingPriceTextView.getText().toString());
                    auctionItem.highest_bidder = getCurrentUser().username;
                    auctionItem.name = itemNameTextView.getText().toString();
                    auctionItem.created_by = getCurrentUser().username;
                    db.addAuctionItem(auctionItem);


                    startingPriceTextView.setText("");
                    hoursActiveTextView.setText("");
                    itemNameTextView.setText("");
                    dialog.dismiss();

                } else {
                    Toast.makeText(getApplicationContext(), "All fields are required.", Toast.LENGTH_SHORT).show();
                    addAuctionDialog.show();
                }
            }
        });
        addAuctionDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startingPriceTextView.setText("");
                hoursActiveTextView.setText("");
                itemNameTextView.setText("");
                dialog.cancel();
            }
        });

        addAuctionDialogBuilder.setView(addAuctionView, 0, 0, 0, 0);

        addAuctionDialog = addAuctionDialogBuilder.create();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAuctionDialog.show();
                //Workaround for dialog data validation to override dialog dismissing
                Button positiveButton = addAuctionDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(itemNameTextView.getText()) && !TextUtils.isEmpty(hoursActiveTextView.getText()) && !TextUtils.isEmpty(startingPriceTextView.getText()) && Integer.parseInt(hoursActiveTextView.getText().toString()) > 0 && Double.parseDouble(startingPriceTextView.getText().toString()) > 0) {
                            AuctionItem auctionItem = new AuctionItem();
                            auctionItem.hours_active = Integer.parseInt(hoursActiveTextView.getText().toString());
                            auctionItem.starting_price = Double.parseDouble(startingPriceTextView.getText().toString());
                            auctionItem.highest_bid = Double.parseDouble(startingPriceTextView.getText().toString());
                            auctionItem.highest_bidder = getCurrentUser().username;
                            auctionItem.name = itemNameTextView.getText().toString();
                            auctionItem.created_by = getCurrentUser().username;
                            db.addAuctionItem(auctionItem);

                            startingPriceTextView.setText("");
                            hoursActiveTextView.setText("");
                            itemNameTextView.setText("");
                            addAuctionDialog.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(), "All fields are required.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        TextView usernameTextView = (TextView) header.findViewById(R.id.usernameTextView);
        TextView emailTextView = (TextView) header.findViewById(R.id.emailTextView);

        usernameTextView.setText(getCurrentUser().username);
        emailTextView.setText(getCurrentUser().email);

        if (findViewById(R.id.fragment_container) != null) {

            if (savedInstanceState != null) {
                return;
            }

            AllAuctionsFragment allAuctionsFragment = new AllAuctionsFragment();
            allAuctionsFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, allAuctionsFragment).commit();
            setTitle(getString(R.string.active_auctions));
        }


        timeHandler = new Handler();
        startRepeatingTask();

    }


    //Handler for time
    private int hourInterval = 60 * 60 * 1000;
    private Handler timeHandler;
    boolean runFirstTime = false;
    Runnable hourChecker = new Runnable() {
        @Override
        public void run() {

            if (runFirstTime) {
                ArrayList<AuctionItem> auctions = db.getAllAuctions();
                for (AuctionItem auction : auctions) {
                    auction.setHours_active((auction.getHours_active()) - 1);
                    db.updateAuctionItem(auction);
                }

            }
            runFirstTime = true;
            timeHandler.postDelayed(hourChecker, hourInterval);
        }
    };

    void startRepeatingTask() {
        hourChecker.run();
    }

    void stopRepeatingTask() {
        timeHandler.removeCallbacks(hourChecker);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.all_auctions) {
            Fragment activeAuctionsFragment = new AllAuctionsFragment();
            setTitle(getString(R.string.active_auctions));


            // Replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, activeAuctionsFragment)
                    .commit();

        } else if (id == R.id.add_auction) {

            addAuctionDialog.show();

            //Workaround for dialog data validation to override dialog dismissing
            Button positiveButton = addAuctionDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(itemNameTextView.getText()) && !TextUtils.isEmpty(hoursActiveTextView.getText()) && !TextUtils.isEmpty(startingPriceTextView.getText()) && Integer.parseInt(hoursActiveTextView.getText().toString()) > 0 && Double.parseDouble(startingPriceTextView.getText().toString()) > 0) {
                        AuctionItem auctionItem = new AuctionItem();
                        auctionItem.hours_active = Integer.parseInt(hoursActiveTextView.getText().toString());
                        auctionItem.starting_price = Double.parseDouble(startingPriceTextView.getText().toString());
                        auctionItem.highest_bid = Double.parseDouble(startingPriceTextView.getText().toString());
                        auctionItem.highest_bidder = getCurrentUser().username;
                        auctionItem.name = itemNameTextView.getText().toString();
                        auctionItem.created_by = getCurrentUser().username;
                        db.addAuctionItem(auctionItem);


                        startingPriceTextView.setText("");
                        hoursActiveTextView.setText("");
                        itemNameTextView.setText("");
                        addAuctionDialog.dismiss();

                    } else {
                        Toast.makeText(getApplicationContext(), "All fields are required.", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        } else if (id == R.id.log_out) {

            prefs.putString("currentUser", "noUser");
            finish();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);


        } else if (id == R.id.active_bids) {

            Fragment activeBidsFragment = new ActiveBidsFragment();
            setTitle(getString(R.string.active_bids));

            // Replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, activeBidsFragment)
                    .commit();

        } else if (id == R.id.won_auctions) {
            Fragment wonAuctionsFragment = new WonAuctionsFragment();
            setTitle(getString(R.string.won_auctions));

            // Replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, wonAuctionsFragment)
                    .commit();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public UserItem getCurrentUser() {
        UserItem user = db.getUserItem(prefs.getString("currentUser", "noUser"));

        return user;
    }

}
