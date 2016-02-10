package com.midsummer.mynews;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by nienb on 10/2/16.
 */
public class MyNewsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
    }

}
