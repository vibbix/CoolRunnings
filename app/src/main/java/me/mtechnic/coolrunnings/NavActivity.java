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
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NavActivity extends AppCompatActivity {
    private static final String TAG = "NavActivity";
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
                                    Log.d(TAG, "Recieved data");
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        ButterKnife.bind(this);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.navigation);
        toolbar.setOnMenuItemClickListener(mOnMenuItemClickListListener);
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
        double[] coordinates = resolver.getCoordinate();
        String debug = String.format("New coordinate: %s", Arrays.toString(coordinates));
        Log.d(TAG, debug);
        debugcoords.setText(Arrays.toString(coordinates));

    }

    private void render() {
        double[] coordinates = resolver.getCoordinate();
        debugcoords.setText(Arrays.toString(coordinates));
        Bitmap b = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        Paint p = new Paint();
        p.setStrokeWidth(0);
        p.setColor(Color.BLUE);
        c.drawRect(0.0f, 0.0f, 100.0f, 66.0f, p);
        p.setColor(Color.YELLOW);
        c.drawCircle((float) coordinates[0], (float) coordinates[1], 5, p);
        ImageCanvas.setImageBitmap(b);
    }

}
