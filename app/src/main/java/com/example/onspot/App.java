package com.example.onspot;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.example.onspot.utilities.SignalGenerator;


public class App  extends Application {

    public static Resources res;

    @Override
    public void onCreate() {
        super.onCreate();

        res = getResources();

        SignalGenerator.init(this);
    }
}
