package com.crossover.auctionproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by stevyhacker on 8.12.15..
 */
public class DatabaseAdapter {

    static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "auction";

    static final String USERS_TABLE = "USERS";
    static final String USERS_USERNAME = "USERNAME";
    static final String USERS_EMAIL = "EMAIL";
    static final String USERS_PASSWORD = "PASSWORD";


    // SQL Statement to create a new database.
    static final String USER_TABLE_CREATE = "CREATE TABLE " + USERS_TABLE + "( " + "ID" + " INTEGER PRIMARY KEY AUTOINCREMENT," + USERS_USERNAME + " TEXT, EMAIL TEXT, PASSWORD TEXT);";

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

    public void close(){
        db.close();
    }

    public void addUserItem(UserItem user) {
        db = dbHelper.getWritableDatabase();

        ContentValues newValues = new ContentValues();
        // Assign values for each row.
        newValues.put(USERS_USERNAME, user.username);
        newValues.put(USERS_PASSWORD, user.password);
        newValues.put(USERS_EMAIL, user.email);

        // Insert the row into your table
        db.insert(USERS_TABLE, null, newValues);

        db.close();
    }

    public void deleteUserItem(String UserName) {
        db = dbHelper.getWritableDatabase();

        String where = USERS_USERNAME + "=?";
        db.delete(USERS_TABLE, where, new String[]{UserName});
        db.close();
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

    public void updateUserItem(String userName, String email, String password) {
        db = dbHelper.getWritableDatabase();

        // Define the updated row content.
        ContentValues updatedValues = new ContentValues();
        // Assign values for each row.
        updatedValues.put(USERS_USERNAME, userName);
        updatedValues.put(USERS_PASSWORD, password);
        updatedValues.put(USERS_EMAIL, email);

        String where = USERS_USERNAME + " = ?";
        db.update(USERS_TABLE, updatedValues, where, new String[]{userName});

        db.close();
    }


}
