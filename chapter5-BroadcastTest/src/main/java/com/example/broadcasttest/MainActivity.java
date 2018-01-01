package com.example.broadcasttest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {



    private LocalReceiver localReceiver;

    private LocalBroadcastManager localBroadcastManager;
    private MyBroadcastReceiver myBroadcastReceiver;

   private IntentFilter intentFilter;
   private NetworkChangeReceiver networkChangeReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //网络信息改变
       /*
       intentFilter = new IntentFilter();
       intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
       networkChangeReceiver = new NetworkChangeReceiver();
       registerReceiver(networkChangeReceiver, intentFilter);*/
       /*localBroadcastManager = LocalBroadcastManager.getInstance(this); // 获取实例
         Button button = (Button) findViewById(R.id.button);
       button.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example.broadcasttest.LOCAL_BROADCAST");
                localBroadcastManager.sendBroadcast(intent); // 发送本地广播
                          }
        });
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.broadcasttest.LOCAL_BROADCAST");
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver, intentFilter); // 注册本地广播监听器
        */

        intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.broadcasttest.MY_BROADCAST");
        myBroadcastReceiver=new MyBroadcastReceiver();
        registerReceiver(myBroadcastReceiver,intentFilter);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example.broadcasttest.MY_BROADCAST");
                sendBroadcast(intent); // 发送本地广播
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
      //unregisterReceiver(networkChangeReceiver);
      //  localBroadcastManager.unregisterReceiver(localReceiver);
        unregisterReceiver(myBroadcastReceiver);
    }

    class LocalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "received local broadcast", Toast.LENGTH_SHORT).show();
        }

    }

    class NetworkChangeReceiver extends BroadcastReceiver {
       @Override
       public void onReceive(Context context, Intent intent) {
           ConnectivityManager connectionManager = (ConnectivityManager)
                   getSystemService(Context.CONNECTIVITY_SERVICE);
           NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
           if (networkInfo != null && networkInfo.isAvailable()) {
               Toast.makeText(context, "network is available",
                       Toast.LENGTH_SHORT).show();
           } else {
               Toast.makeText(context, "network is unavailable",
                       Toast.LENGTH_SHORT).show();
           }
       }

   }

}
