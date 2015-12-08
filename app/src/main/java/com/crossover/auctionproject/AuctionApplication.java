package com.crossover.auctionproject;

import android.app.Application;
import android.content.Context;


/**
 * Created by stevyhacker on 27.7.14..
 */
public class AuctionApplication extends Application {

    private static AuctionApplication app;
    public static PreferencesHelper preferencesHelper;

    public AuctionApplication(){

    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        init(this);
    }

    public static void init(Context context) {
        if (app == null)
            app = new AuctionApplication(context);
    }

    public AuctionApplication(Context context) {
        preferencesHelper = new PreferencesHelper(context);
    }

}
