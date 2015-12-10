package com.crossover.auctionproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by stevyhacker on 8.12.15..
 */
public class DatabaseAdapter {

    static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "auctionDB";

    static final String USERS_TABLE = "USERS";
    static final String USERS_USERNAME = "USERNAME";
    static final String USERS_EMAIL = "EMAIL";
    static final String USERS_PASSWORD = "PASSWORD";

    private static String AUCTIONS_TABLE = "AUCTIONS";
    private static String AUCTIONS_NAME = "NAME";
    private static String AUCTIONS_DAYS_ACTIVE = "DAYS_ACTIVE";
    private static String AUCTIONS_STARTING_PRICE = "STARTING_PRICE";
    private static String AUCTIONS_HIGHEST_BIDDER = "HIGHEST_BIDDER";
    private static String AUCTIONS_HIGHEST_BID = "HIGHEST_BID";
    private static String AUCTIONS_CREATED_BY = "CREATED_BY";
    private static String USERS_ALL_BIDS = "ALL_BIDS";

    // SQL Statement to create new tables.
    static final String USER_TABLE_CREATE = "CREATE TABLE " + USERS_TABLE + "( " + "ID" + " INTEGER PRIMARY KEY AUTOINCREMENT," + USERS_USERNAME + " TEXT, EMAIL TEXT, PASSWORD TEXT," + USERS_ALL_BIDS + " TEXT);";
    static final String AUCTION_TABLE_CREATE = "CREATE TABLE " + AUCTIONS_TABLE + "( " + "ID" + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + AUCTIONS_NAME + " TEXT, " + AUCTIONS_STARTING_PRICE + " REAL, " + AUCTIONS_DAYS_ACTIVE + " INTEGER, " + AUCTIONS_CREATED_BY + " TEXT, " + AUCTIONS_HIGHEST_BIDDER + " TEXT, "
            + AUCTIONS_HIGHEST_BID + " REAL" + ");";


    // Variable to hold the database instance
    public SQLiteDatabase db;

    // Context of the application using the database.
    private final Context context;

    // Database open/upgrade helper
    private DatabaseHelper dbHelper;

    public DatabaseAdapter(Context _context) {
        context = _context;
        dbHelper = new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }

    public void close() {
        db.close();
    }

    public void addUserItem(UserItem user) {
        db = dbHelper.getWritableDatabase();

        ContentValues newValues = new ContentValues();
        // Assign values for each row.
        newValues.put(USERS_USERNAME, user.username);
        newValues.put(USERS_PASSWORD, user.password);
        newValues.put(USERS_EMAIL, user.email);
        newValues.put(USERS_ALL_BIDS, convertArrayToString(convertIntegerArrayToStringArray(user.all_bids)));

        // Insert the row into your table
        db.insert(USERS_TABLE, null, newValues);

        db.close();
    }

    public void addAuctionItem(AuctionItem auction) {
        db = dbHelper.getWritableDatabase();

        ContentValues newValues = new ContentValues();
        // Assign values for each row.
        newValues.put(AUCTIONS_NAME, auction.name);
        newValues.put(AUCTIONS_STARTING_PRICE, auction.starting_price);
        newValues.put(AUCTIONS_CREATED_BY, auction.created_by);
        newValues.put(AUCTIONS_HIGHEST_BID, auction.highest_bid);
        newValues.put(AUCTIONS_HIGHEST_BIDDER, auction.highest_bidder);
        newValues.put(AUCTIONS_DAYS_ACTIVE, auction.days_active);


        // Insert the row into your table
        db.insert(AUCTIONS_TABLE, null, newValues);

        db.close();
    }

    public void deleteAuctionItem(int id) {
        db = dbHelper.getWritableDatabase();

        String where = "ID" + "=?";
        db.delete(AUCTIONS_TABLE, where, new String[]{String.valueOf(id)});

        db.close();
    }

    public void deleteUserItem(String UserName) {
        db = dbHelper.getWritableDatabase();

        String where = USERS_USERNAME + "=?";
        db.delete(USERS_TABLE, where, new String[]{UserName});
        db.close();
    }

    public AuctionItem getAuctionItem(int id) {
        db = dbHelper.getReadableDatabase();
        AuctionItem auction = new AuctionItem();

        Cursor cursor = db.query(AUCTIONS_TABLE, null, " ID" + " =?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.getCount() < 1) // item doesn't exist
        {
            cursor.close();
            return null;
        }
        cursor.moveToFirst();

        auction.name = cursor.getString(cursor.getColumnIndex(AUCTIONS_NAME));
        auction.starting_price = Double.parseDouble(cursor.getString(cursor.getColumnIndex(AUCTIONS_STARTING_PRICE)));
        auction.created_by = cursor.getString(cursor.getColumnIndex(AUCTIONS_CREATED_BY));
        auction.highest_bid = Double.parseDouble(cursor.getString(cursor.getColumnIndex(AUCTIONS_HIGHEST_BID)));
        auction.highest_bidder = cursor.getString(cursor.getColumnIndex(AUCTIONS_HIGHEST_BIDDER));
        auction.days_active = Integer.parseInt(cursor.getString(cursor.getColumnIndex(AUCTIONS_DAYS_ACTIVE)));


        db.close();
        return auction;
    }

    public UserItem getUserItem(String userName) {
        db = dbHelper.getReadableDatabase();
        UserItem user = new UserItem();
        user.username = userName;
        Cursor cursor = db.query(USERS_TABLE, null, " " + USERS_USERNAME + " =?", new String[]{userName}, null, null, null);
        if (cursor.getCount() < 1) // Username doesn't exist
        {
            cursor.close();
            return null;
        }
        cursor.moveToFirst();

        user.id = cursor.getInt(cursor.getColumnIndex("ID"));
        user.email = cursor.getString(cursor.getColumnIndex(USERS_EMAIL));
        user.password = cursor.getString(cursor.getColumnIndex(USERS_PASSWORD));
        user.all_bids = convertStringArrayToIntegerArray(convertStringToArray(cursor.getString(cursor.getColumnIndex(USERS_ALL_BIDS))));
        Log.e("get.user.all_bids", cursor.getString(cursor.getColumnIndex(USERS_ALL_BIDS)));
        cursor.close();
        db.close();
        return user;
    }

    public Boolean checkEntry(String userName) {
        db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(USERS_TABLE, null, " " + USERS_USERNAME + " =?", new String[]{userName}, null, null, null);
        if (cursor.getCount() < 1) // UserName Not Exist
        {
            cursor.close();
            return false;
        }

        cursor.close();
        db.close();
        return true;
    }

    public void updateAuctionItem(AuctionItem auction) {
        db = dbHelper.getWritableDatabase();

        ContentValues newValues = new ContentValues();
        // Assign values for each row.
        newValues.put(AUCTIONS_NAME, auction.name);
        newValues.put(AUCTIONS_STARTING_PRICE, auction.starting_price);
        newValues.put(AUCTIONS_CREATED_BY, auction.created_by);
        newValues.put(AUCTIONS_HIGHEST_BID, auction.highest_bid);
        newValues.put(AUCTIONS_HIGHEST_BIDDER, auction.highest_bidder);
        newValues.put(AUCTIONS_DAYS_ACTIVE, auction.days_active);


        String where = "ID" + " = " + auction.id;
        db.update(AUCTIONS_TABLE, newValues, where , null);

        db.close();
    }

    public void updateUserItem(UserItem user) {
        db = dbHelper.getWritableDatabase();

        // Define the updated row content.
        ContentValues updatedValues = new ContentValues();
        // Assign values for each row.
        updatedValues.put(USERS_USERNAME, user.username);
        updatedValues.put(USERS_PASSWORD, user.password);
        updatedValues.put(USERS_EMAIL, user.email);
        updatedValues.put(USERS_ALL_BIDS, convertArrayToString(convertIntegerArrayToStringArray(user.all_bids)));

        String where = USERS_USERNAME + " = '" + user.username +"'u";
        db.update(USERS_TABLE, updatedValues, where, null);

        db.close();
    }

    public ArrayList<AuctionItem> getAvailableAuctions(String currentUser) {
        ArrayList<AuctionItem> activeAuctionsList = new ArrayList<AuctionItem>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + AUCTIONS_TABLE + " WHERE " + AUCTIONS_CREATED_BY + " != '" + currentUser + "';", null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                while (cursor.isAfterLast() == false) {
                    AuctionItem auction = new AuctionItem();

                    auction.id = cursor.getInt(cursor.getColumnIndex("ID"));
                    auction.name = cursor.getString(cursor.getColumnIndex(AUCTIONS_NAME));
                    auction.starting_price = Double.parseDouble(cursor.getString(cursor.getColumnIndex(AUCTIONS_STARTING_PRICE)));
                    auction.created_by = cursor.getString(cursor.getColumnIndex(AUCTIONS_CREATED_BY));
                    auction.highest_bid = Double.parseDouble(cursor.getString(cursor.getColumnIndex(AUCTIONS_HIGHEST_BID)));
                    auction.highest_bidder = cursor.getString(cursor.getColumnIndex(AUCTIONS_HIGHEST_BIDDER));
                    auction.days_active = Integer.parseInt(cursor.getString(cursor.getColumnIndex(AUCTIONS_DAYS_ACTIVE)));

                    activeAuctionsList.add(auction);
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }

        return activeAuctionsList;

    }

    public ArrayList<AuctionItem> getWonAuctions(String currentUser) {
        ArrayList<AuctionItem> wonAuctions = new ArrayList<AuctionItem>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + AUCTIONS_TABLE + " WHERE " + AUCTIONS_HIGHEST_BIDDER + " = '" + currentUser + "' " + "AND " + AUCTIONS_DAYS_ACTIVE + " = 0 " + "AND " + AUCTIONS_CREATED_BY + " = '" + currentUser + "';", null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                while (cursor.isAfterLast() == false) {
                    AuctionItem auction = new AuctionItem();

                    auction.id = cursor.getInt(cursor.getColumnIndex("ID"));
                    auction.name = cursor.getString(cursor.getColumnIndex(AUCTIONS_NAME));
                    auction.starting_price = Integer.parseInt(cursor.getString(cursor.getColumnIndex(AUCTIONS_STARTING_PRICE)));
                    auction.created_by = cursor.getString(cursor.getColumnIndex(AUCTIONS_CREATED_BY));
                    auction.highest_bid = Integer.parseInt(cursor.getString(cursor.getColumnIndex(AUCTIONS_HIGHEST_BID)));
                    auction.highest_bidder = cursor.getString(cursor.getColumnIndex(AUCTIONS_HIGHEST_BIDDER));
                    auction.days_active = Integer.parseInt(cursor.getString(cursor.getColumnIndex(AUCTIONS_DAYS_ACTIVE)));

                    wonAuctions.add(auction);
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }

        return wonAuctions;

    }

    public ArrayList<AuctionItem> getActiveBids(String currentUser) {

        ArrayList<AuctionItem> activeBids = new ArrayList<AuctionItem>();

        UserItem user = getUserItem(currentUser);

        for (Integer id : user.all_bids) {
            activeBids.add(getAuctionItem(id));
        }

        return activeBids;
    }

    //Array converters for all user bids
    public static String strSeparator = ":_;_:";

    public static String convertArrayToString(ArrayList<String> array) {
        return TextUtils.join(strSeparator, array);
    }

    public static ArrayList<String> convertStringToArray(String str) {
        return new ArrayList<>(Arrays.asList(str.split(strSeparator)));
    }

    public static ArrayList<Integer> convertStringArrayToIntegerArray(ArrayList<String> str) {
        ArrayList<Integer> arr = new ArrayList<Integer>();
        try {

            if (str.size() >= 1) {
                for (int i = 0; i < str.size(); i++) {
                    arr.add(Integer.parseInt(str.get(i)));

                }
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return arr;

    }

    public static ArrayList<String> convertIntegerArrayToStringArray(ArrayList<Integer> ints) {
        ArrayList<String> arr = new ArrayList<String>();

        for (int i = 0; i < ints.size(); i++) {
            arr.add(String.valueOf(ints.get(i)));
        }

        return arr;
    }


}
