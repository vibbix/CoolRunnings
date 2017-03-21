package me.mtechnic.coolrunnings;

import android.app.Activity;
import android.content.res.Resources;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple adapter to enumerate all the available wifi networks
 */
public class WiFiListAdapter extends BaseAdapter {
    private static final String TAG = "WiFiListAdapter";
    private List<ScanResult> results;
    private Activity activity;

    public WiFiListAdapter(Activity activity, List<ScanResult> results) {
        this.activity = activity;
        this.results = results;
    }

    @Override
    public int getCount() {
        return this.results.size();
    }

    @Override
    public Object getItem(int position) {
        return this.results.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WiFiViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (WiFiViewHolder) convertView.getTag();
        } else {
            LayoutInflater li = activity.getLayoutInflater();
            convertView = li.inflate(R.layout.list_wifi, parent, false);
            viewHolder = new WiFiViewHolder(convertView);
        }
        try {
            viewHolder.ssid.setText(this.results.get(position).SSID);
            viewHolder.macaddress.setText(this.results.get(position).BSSID);
            int signal = WifiManager.calculateSignalLevel(
                    this.results.get(position).level, 100);
            viewHolder.signal.setText(String.valueOf(signal));
        } catch (Resources.NotFoundException nfe) {
            Log.d(TAG, "NotFoundException", nfe);
        } catch (NullPointerException npe) {
            Log.d(TAG, "NullPointerException", npe);
            //if (viewHolder.signal != null && viewHolder.signal.getText().equals(""))
            //    viewHolder.signal.setText("N/A");
        }

        return convertView;
    }
}

class WiFiViewHolder {
    @BindView(R.id.ssid)
    TextView ssid;
    @BindView(R.id.mac_adress)
    TextView macaddress;
    @BindView(R.id.signal)
    TextView signal;

    WiFiViewHolder(View view) {
        ButterKnife.bind(this, view);
    }
}
