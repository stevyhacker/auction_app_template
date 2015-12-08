package com.crossover.auctionproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.crossover.auctionproject.database.DatabaseAdapter;
import com.crossover.auctionproject.database.UserItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    private DatabaseAdapter db;
    PreferencesHelper prefs;
    android.support.v7.app.AlertDialog addAuctionDialog;
    AlertDialog.Builder addAuctionDialogBuilder;
    LinearLayout ll;

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

        addAuctionDialogBuilder = new AlertDialog.Builder(this);
        addAuctionDialogBuilder.setTitle("Add item for auction"); //todo SAVE TO STRINGS.XML
        addAuctionDialogBuilder.setMessage("Enter your auction details:");
        addAuctionDialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        addAuctionDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        addAuctionDialogBuilder.setView(addAuctionView, 0, 0, 0, 0);

        addAuctionDialog = addAuctionDialogBuilder.create();
//
//        ll = new LinearLayout(this);
//        ll.setOrientation(LinearLayout.VERTICAL);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT
//        );
//        params.setMargins(50, 15, 50, 15);
//        ll.setLayoutParams(params);
//
//        EditText startPriceEditText = new EditText(this);
//        startPriceEditText.setHint("Starting price:");
//        startPriceEditText.setGravity(Gravity.CENTER);
//
//        EditText timeEditText = new EditText(this);
//        timeEditText.setHint("Time active:");
//        timeEditText.setGravity(Gravity.CENTER);
//
//        EditText nameEditText = new EditText(this);
//        nameEditText.setHint("Item name:");
//        nameEditText.setGravity(Gravity.CENTER);
//
//        ll.addView(nameEditText, params);
//        ll.addView(startPriceEditText, params);
//        ll.addView(timeEditText, params);

//        addAuctionDialogBuilder.setView(ll);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAuctionDialog.show();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


//        TextView usernameTextView = (TextView) findViewById(R.id.usernameTextView);
//        TextView emailTextView = (TextView) findViewById(R.id.emailTextView);
//      TODO MAKE THIS WORK
//
//        usernameTextView.setText(getCurrentUser().username);
//        emailTextView.setText(getCurrentUser().email);


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
        } else if (id == R.id.add_auction) {

           addAuctionDialog.show();


        } else if (id == R.id.log_out) {

            prefs.putString("currentUser", "noUser");
            finish();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);


        } else if (id == R.id.active_bids) {

        } else if (id == R.id.won_auctions) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public UserItem getCurrentUser() {
        UserItem user = db.getUserItem(prefs.getString("currentUser", "noUser"));

        return user;
    }

}
