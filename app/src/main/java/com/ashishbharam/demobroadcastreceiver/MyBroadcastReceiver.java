package com.ashishbharam.demobroadcastreceiver;
/* 
Created by Ashish Bharam on 13-Nov-20 at 06:58 PM.
Copyright (c) 2020 Ashish Bharam. All rights reserved.
*/

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private final String TAG = "mytag";

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "SMS received. BroadcastReceiver triggered", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onReceive: SMS received. BroadcastReceiver triggered" );
    }
}

