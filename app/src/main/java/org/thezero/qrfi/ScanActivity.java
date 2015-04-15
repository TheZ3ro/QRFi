package org.thezero.qrfi;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.welcu.android.zxingfragmentlib.BarCodeScannerFragment;

public class ScanActivity extends ActionBarActivity {

    boolean torchState = false;

    Button mToggleButton;
    BarCodeScannerFragment mScannerFragment;
    public static Activity m;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        m=this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.primary_text));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.scan_activity));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fm = getSupportFragmentManager();
        mScannerFragment = (BarCodeScannerFragment) fm.findFragmentById(R.id.scanner_fragment);

        mToggleButton = (Button) findViewById(R.id.button_flash);
        mToggleButton.setOnClickListener(createToggleFlashListener());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
        }
        return false;
    }

    private View.OnClickListener createToggleFlashListener() {
        return new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            torchState = !torchState;
            mScannerFragment.setTorch(torchState);
          }
        };
    }
}