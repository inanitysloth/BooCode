package com.example.runtimepermissiontest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int RequestPermissionCode_CALLPHONE=1;
    private static final int RequestPermissionCode_SENDSMS=2;
    private Button mBtnMakeCall;
    private Button mBtnSendSms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        int currentapiVersion = Build.VERSION.SDK_INT;
        Log.d("MainActivity", Integer.toString(currentapiVersion));
        /*Button makeCall = (Button) findViewById(R.id.make_call);
        makeCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                } else {
                    call();
                }
               *//* try {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:10086"));
                    startActivity(intent);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }*//*
            }
        });*/
    }

    private void call() {
        //Android 6.0 phone must check if the app have the permission,app must requestPermissions if not;

        //get permission by running Application
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CALL_PHONE},RequestPermissionCode_CALLPHONE);
        }

        else {
            try {
                //create a new intent to use the system Call function
                Intent intent = new Intent(Intent.ACTION_CALL);
                // use the system Call function by the data
                //Uri is used to parse the string,get the string data from editText
                intent.setData(Uri.parse("tel:10010"));
                //Call system function (call phone function)
                startActivity(intent);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Callback for the result from requesting permissions. This method
     * is invoked for every call on {@link #requestPermissions(String[], int)}.
     * <p>
     * <strong>Note:</strong> It is possible that the permissions request interaction
     * with the user is interrupted. In this case you will receive empty permissions
     * and results arrays which should be treated as a cancellation.
     * </p>
     *
     * @param requestCode  The request code passed in {@link #requestPermissions(String[], int)}.
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either {@link PackageManager#PERMISSION_GRANTED}
     *                     or {@link PackageManager#PERMISSION_DENIED}. Never null.
     * @see #requestPermissions(String[], int)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            /*requestCode==1 means call phone permission*/
            case RequestPermissionCode_CALLPHONE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            /*requestCode==2 means send sms  permission*/
            default:
        }
    }
/*@Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }*/

    private void initView() {
        mBtnMakeCall = (Button) findViewById(R.id.btn_make_call);
        mBtnSendSms = (Button) findViewById(R.id.btn_send_sms);

        mBtnMakeCall.setOnClickListener(this);
        mBtnSendSms.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_make_call:
                call();
                break;
            case R.id.btn_send_sms:
                sms();
                break;
        }
    }

    private void sms() {
        try {
            Uri smsToUri = Uri.parse("smsto:10010");
            Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
            intent.putExtra("sms_body", "测试发送短信");
            startActivity(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}
