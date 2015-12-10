package com.crossover.auctionproject.database;

import java.util.ArrayList;

/**
 * Created by stevyhacker on 8.12.15..
 */
public class UserItem {

    public int id;
    public String username, email, password;
    public ArrayList<Integer> all_bids;


    public UserItem(int id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public UserItem() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public ArrayList<Integer> getAll_bids() {
        return all_bids;
    }

    public void setAll_bids(ArrayList<Integer> all_bids) {
        this.all_bids = all_bids;
    }

}
