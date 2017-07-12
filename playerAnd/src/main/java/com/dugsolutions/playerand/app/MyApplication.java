package com.dugsolutions.playerand.app;

import android.app.Application;

import com.dugsolutions.playerand.BuildConfig;
import com.dugsolutions.playerand.db.LoadXml;
import com.dugsolutions.playerand.db.DatabaseManager;
import com.dugsolutions.playerand.util.Roll;
import com.dugsolutions.playerand.util.Values;

import timber.log.Timber;

/**
 * Created by dug on 7/11/17.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        DatabaseManager.Init(this);
        Roll.Init();
        LoadXml.Init(this);
        Values.Init();
    }
}