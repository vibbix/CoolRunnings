package me.mtechnic.coolrunnings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WifiActivity extends AppCompatActivity {
    private WifiManager wifi;
    private int size = 0;
    private List<ScanResult> results;
    private final static String TAG = "WiFiActivity";

    @BindView(R.id.rescan_wifi)
    AppCompatButton rescan;
    @BindView(R.id.wifi_name)
    TextView wifiname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
        ButterKnife.bind(this);
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        Log.d(TAG, "WiFiActivity Initialized");

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
                size = results.size();
                setWifiname();
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
    private void setWifiname(){
        Log.d(TAG, "Building Wifiname");

        StringBuilder sb = new StringBuilder();
        sb.append("SSID\tMAC ADDRESS\tSIGNAL")
        for (ScanResult sr : results){
            sb.append(sr.SSID);
            sb.append('\t');
            sb.append(sr.BSSID);
            sb.append('\t');

            sb.append('\n');
        }
        this.wifiname.setText(sb.toString());
        Log.d(TAG, "WiFI data set");

    }

}
