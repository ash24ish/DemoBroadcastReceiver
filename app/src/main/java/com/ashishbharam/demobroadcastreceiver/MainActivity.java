package com.ashishbharam.demobroadcastreceiver;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static final String TAG = "mytag";
    public static final int PERMISSION_REQUEST_CAMERA = 10;
    private static final int RECEIVE_SMS_PERMISSION_REQ = 20;
    private static final int STORAGE_PERMISSION_REQ_CODE = 30;
    private Button button;
    private BroadcastReceiver broadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        broadcastReceiver = new MyBroadcastReceiver();

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CALENDAR);
        Log.d(TAG, "onCreate: " + permissionCheck);

        button.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED){
                Log.i(TAG, "CheckPermission: Permission Already Granted ");
                Toast.makeText(this, "Permission Already Granted", Toast.LENGTH_SHORT).show();
            }
            else{
                getUserPermission();
            }
        });
    }

    private void getUserPermission() {

        // Provide an additional rationale(Dialog) to the user if the permission was not granted
        // and the user would benefit from additional context for the use of the permission.

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)){
            new AlertDialog.Builder(this)
                    .setTitle("Permissions Needed")
                    .setMessage("To Register Broadcast Receiver")
                    .setPositiveButton("OK", (dialog, which) -> {
                        // Request the permission. The result will be received in onRequestPermissionResult().
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS},
                                RECEIVE_SMS_PERMISSION_REQ);
                    })
                    .setNegativeButton("CANCEL", (dialog, which) ->{ dialog.dismiss();})
                    .create()
                    .show();

        }
        else{
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, RECEIVE_SMS_PERMISSION_REQ);
        }
    }

    public void askManualPermission(){
        new AlertDialog.Builder(this)
                .setTitle("Grant Receive SMS request")
                .setMessage("As you have denied the permission earlier, you hav to " +
                        "Enable permissions from settings to register the Broadcast receiver")
                .setPositiveButton("Settings", (dialog, which) -> {

                    startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID)));
                    Log.d(TAG, "requestStoragePermissions: "+getPackageName());

                })
                .setNegativeButton("Cancel", (dialog, which) -> { dialog.dismiss();})
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case RECEIVE_SMS_PERMISSION_REQ:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.i(TAG, "getPermission: Permission Granted ");
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                }else {
                    Log.i(TAG, "getPermission: Permission NOT Granted ");
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
                break;

            case PERMISSION_REQUEST_CAMERA:
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + requestCode);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(broadcastReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }
}