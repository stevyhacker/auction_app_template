package com.crossover.auctionproject.database;

/**
 * Created by stevyhacker on 8.12.15..
 */
public class AuctionItem {


    public int id;
    public double highest_bid;
    public int starting_price;
    public int days_active;
    public String name, created_by, highest_bidder;
//    public String[] all_bidders;

    public AuctionItem() {

    }

    public AuctionItem(int id, double highest_bid, int starting_price, String name, String created_by, String highest_bidder, int days_active) {
        this.id = id;
        this.highest_bid = highest_bid;
        this.starting_price = starting_price;
        this.name = name;
        this.created_by = created_by;
        this.highest_bidder = highest_bidder;
        this.days_active = days_active;
//        this.all_bidders = all_bidders;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getHighest_bid() {
        return highest_bid;
    }

    public void setHighest_bid(double highest_bid) {
        this.highest_bid = highest_bid;
    }

    public int getStarting_price() {
        return starting_price;
    }

    public void setStarting_price(int starting_price) {
        this.starting_price = starting_price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getHighest_bidder() {
        return highest_bidder;
    }

    public void setHighest_bidder(String highest_bidder) {
        this.highest_bidder = highest_bidder;
    }

    public int getDays_active() {
        return days_active;
    }

    public void setDays_active(int days_active) {
        this.days_active = days_active;
    }


//    public String[] getAll_bidders() {
//        return all_bidders;
//    }
//
//    public void setAll_bidders(String[] all_bidders) {
//        this.all_bidders = all_bidders;
//    }


}
