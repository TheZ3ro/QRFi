package org.thezero.qrfi;

import android.os.Bundle;
import android.support.v4.app.NavUtils;

import com.google.zxing.Result;
import com.welcu.android.zxingfragmentlib.BarCodeScannerFragment;

public class ScanFragment extends BarCodeScannerFragment {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setmCallBack(new IResultCallback() {
            @Override
            public void result(Result lastResult) {
                //Toast.makeText(getActivity(), "Scan: " + lastResult.toString(), Toast.LENGTH_SHORT).show();
                NavUtils.navigateUpFromSameTask(ScanActivity.m);
                SuperAwesomeCardFragment.connectAP(lastResult);
            }
        });
    }

    public ScanFragment() {

    }
}