package me.mtechnic.coolrunnings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WifiActivity extends AppCompatActivity {
    private final static String TAG = "WiFiActivity";
    @BindView(R.id.rescan_wifi)
    AppCompatButton rescan;
    @BindView(R.id.wifi_list)
    ListView wifilist;
    private WifiManager wifi;
    private int size = 0;
    private List<ScanResult> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
        ButterKnife.bind(this);
        wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        Log.d(TAG, "WiFiActivity Initialized");
        this.rescan.setGravity(Gravity.CENTER);
    }
    @OnClick(R.id.rescan_wifi)
    public void rescanWiFi(View view){
        Log.d(TAG, "Rescan WiFi Clicked");
        wifi.startScan();
        Log.d(TAG, "Starting scan");

        registerReceiver(new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context c, Intent intent)
            {
                Log.d(TAG, "Recieved data");
                results = wifi.getScanResults();
                updateAdapter();
            }
        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }
    private void checkWifi(){
        Log.d(TAG, "Checking WiFI");

        if (!wifi.isWifiEnabled())
        {
            Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled", Toast.LENGTH_LONG).show();
            wifi.setWifiEnabled(true);
        }
    }

    private void updateAdapter() {
        this.wifilist.setAdapter(new WiFiListAdapter(this, this.results));
    }
}
