package me.mtechnic.coolrunnings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NavActivity extends AppCompatActivity {
    private static final String TAG = "NavActivity";
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;
    private WiFiFragment wiFiFragment;
    private FragmentManager fragMan;
    private Fragment currentFragment;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_scan:
                    currentFragment = wiFiFragment;
                    break;
                case R.id.navigation_settings:
                    //currentFragment = aboutFragment;
                    break;
            }
            final FragmentTransaction ft = fragMan.beginTransaction();
            ft.replace(R.id.fragmentContainer, currentFragment).commit();
            return true;
        }
    };
    private android.support.v7.widget.Toolbar.OnMenuItemClickListener mOnMenuItemClickListListener =
            item -> {
                switch (item.getItemId()) {
                    case R.id.action_refresh:
                        wiFiFragment.scan();
                        return true;
                }
                return false;
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
        this.wiFiFragment = new WiFiFragment();
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.navigation);
        toolbar.setOnMenuItemClickListener(mOnMenuItemClickListListener);
        this.fragMan = getSupportFragmentManager();
        this.currentFragment = this.wiFiFragment;
        final FragmentTransaction ft = fragMan.beginTransaction();
        ft.add(R.id.fragmentContainer, this.currentFragment).commit();

    }
}
