package me.mtechnic.coolrunnings;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NavActivity extends AppCompatActivity {
    private static final String TAG = "NavActivity";
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_LOCATION = 1;
    @BindView(R.id.message)
    TextView mTextMessage;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;
    @BindView(R.id.scalarSeekBar)
    SeekBar scalarSeekBar;
    @BindView(R.id.ImageCanvas)
    ImageView ImageCanvas;
    @BindView(R.id.DebugCoords)
    TextView debugcoords;
    @BindView(R.id.DebugScalar)
    TextView debugscalar;
    private WifiManager wifi;
    private int size = 0;
    private List<ScanResult> results;
    private WiFiResolver resolver;
    private double[] coordinates = new double[]{0, 0, 0};
    private Random rand;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_scan:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_map:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_settings:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };
    private android.support.v7.widget.Toolbar.OnMenuItemClickListener mOnMenuItemClickListListener =
            new android.support.v7.widget.Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.action_refresh:
                            wifi.startScan();
                            registerReceiver(new BroadcastReceiver() {
                                @Override
                                public void onReceive(Context c, Intent intent) {

                                    Log.d(TAG, "Received data");
                                    results = wifi.getScanResults();
                                    updateResults();
                                    render();
                                }
                            }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                            return true;
                    }
                    return false;
                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbartop, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.rand = new Random();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        ButterKnife.bind(this);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.navigation);
        toolbar.setOnMenuItemClickListener(mOnMenuItemClickListListener);
        if (ContextCompat.checkSelfPermission(NavActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(NavActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(NavActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);
            }
        }
        wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        Log.d(TAG, "WiFiActivity Initialized");
        this.resolver = new WiFiResolver();
        this.scalarSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double scalar = ((double) progress) / 10;
                resolver.setScalar(scalar);
                debugscalar.setText(String.valueOf(scalar) + "x");
                render();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void updateResults() {
        for (ScanResult sr : this.results) {
            resolver.setDistances(sr);
        }
        double[] coordinates = resolver.getCoordinate(true);
        String debug = String.format("New coordinate: %s", Arrays.toString(coordinates));
        Log.d(TAG, debug);
        debugcoords.setText(Arrays.toString(coordinates) + "\n" + Arrays.toString(resolver.getDistances()));
        Toast.makeText(this, "ScanResults updated" + Arrays.toString(resolver.getDistances()), Toast.LENGTH_SHORT).show();

    }

    private void render() {
        double[] coordinates = resolver.getCoordinate(true);
        debugcoords.setText(Arrays.toString(coordinates));
        Bitmap b = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        Paint p = new Paint();
        p.setStrokeWidth(0);
        p.setColor(Color.BLUE);
        c.drawRect(0.0f, 0.0f, 100.0f, 66.0f, p);
        p.setColor(Color.YELLOW);
        c.drawCircle((float) coordinates[0] * 3, (float) coordinates[1] * 3, 3, p);
        this.scalarSeekBar.setProgress(this.scalarSeekBar.getProgress());
        ImageCanvas.setImageBitmap(b);
    }

}
