package me.mtechnic.coolrunnings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * The main fragment for WiFi activities
 */
public class WiFiFragment extends Fragment {
    private static final String TAG = "WiFiFragment";
    @BindView(R.id.ImageCanvas)
    ImageView ImageCanvas;
    private WifiManager wifi;
    private int size = 0;
    private List<ScanResult> results;
    private WiFiResolver resolver;
    private double[] coordinates = new double[]{0, 0, 0};

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedinstancestate) {
        View view = inflater.inflate(R.layout.fragment_wifi, container, false);
        ButterKnife.bind(this, view);
        wifi = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        Log.d(TAG, "WiFiActivity Initialized");
        this.resolver = new WiFiResolver();
        return view;
    }

    private void updateResults() {
        for (ScanResult sr : this.results) {
            resolver.setDistances(sr);
        }
        double[] coordinates = resolver.getCoordinate(true);
        String debug = String.format("New coordinate: %s", Arrays.toString(coordinates));
        Log.d(TAG, debug);
        //debugcoords.setText(Arrays.toString(coordinates) + "\n" + Arrays.toString(resolver.getDistances()));
        Toast.makeText(this.getActivity(), "ScanResults updated" + Arrays.toString(resolver.getDistances()), Toast.LENGTH_SHORT).show();
    }

    private void render() {
        double[] coordinates = resolver.getCoordinate(true);
//        debugcoords.setText(Arrays.toString(coordinates));
        Bitmap b = Bitmap.createBitmap(400, 800, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        Paint p = new Paint();
        p.setStrokeWidth(0);
        p.setColor(Color.parseColor("#FFDEAD"));
        c.drawRect(0.0f, 0.0f, 400.0f, 800.0f, p);
        p.setColor(Color.RED);
        c.drawCircle((float) coordinates[0] * 3, (float) coordinates[1] * 3, 2, p);
//        this.scalarSeekBar.setProgress(this.scalarSeekBar.getProgress());
        ImageCanvas.setImageBitmap(b);
    }

    public void scan() {
        this.wifi.startScan();
        getActivity().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent intent) {
                Log.d(TAG, "Received data");
                results = wifi.getScanResults();
                updateResults();
                render();
            }
        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }
}
