/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.thezero.qrfi;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.android_my.wifi.WifiConfigManager;
import com.google.zxing.client.result.WifiParsedResult;
import com.google.zxing.client.result.WifiResultParser;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SuperAwesomeCardFragment extends Fragment {

	private static final String ARG_POSITION = "position";

    private static String TAG = "QRFi::Card";

	private int position;
    private static Context c;

    public static int GALLERY_INTENT_CALLED = 1;
    public static int GALLERY_KITKAT_INTENT_CALLED = 2;

	public static SuperAwesomeCardFragment newInstance(int position, Context con) {
		SuperAwesomeCardFragment f = new SuperAwesomeCardFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
        c=con;
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		position = getArguments().getInt(ARG_POSITION);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView;
        if(position==0){
            rootView = inflater.inflate(R.layout.fragment_generate, container, false);
            final CardView cardQr = (CardView) rootView.findViewById(R.id.card_qr);
            cardQr.setVisibility(View.GONE);

            String[] items = new String[] {"Open", "WEP", "WPA/WPA2"};
            final EditText ssid = (EditText)rootView.findViewById(R.id.edit_ssid);
            final EditText pass = (EditText)rootView.findViewById(R.id.edit_pass);
            final Spinner spinner = (Spinner) rootView.findViewById(R.id.wifi_type);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(c,android.R.layout.simple_spinner_item, items);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (parent.getSelectedItem().toString().equals("Open")) {
                        rootView.findViewById(R.id.second).setVisibility(View.GONE);
                    } else {
                        rootView.findViewById(R.id.second).setVisibility(View.VISIBLE);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });

            final boolean[] cardHide = {true};
            Button delete = (Button)rootView.findViewById(R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cardHide[0] = true;
                    cardQr.animate()
                            .translationY(0)
                            .alpha(0.0f)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    if(cardHide[0])
                                        cardQr.setVisibility(View.GONE);
                                }
                            });
                    ssid.setText("");
                    pass.setText("");
                    spinner.setSelection(0);
                }
            });

            Button gen = (Button)rootView.findViewById(R.id.generate);
            gen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // WIFI:S:Pistol;T:WEP;P:pistolawifi2;;
                    if(pass.getText().length()<8 && !spinner.getSelectedItem().toString().equals("Open")){
                        Toast.makeText(c, c.getString(R.string.error_pass), Toast.LENGTH_LONG).show();
                        return;
                    }
                    String ap = "WIFI:S:"+ssid.getText()+";T:"+spinner.getSelectedItem().toString()+";P:"+pass.getText()+";;";
                    Log.d(TAG,ap);
                    QRCodeWriter writer = new QRCodeWriter();
                    try {
                        BitMatrix matrix = writer.encode(ap, BarcodeFormat.QR_CODE, 700, 700, null);
                        int height = matrix.getHeight();
                        int width = matrix.getWidth();
                        final Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                        for (int x = 0; x < width; x++) {
                            for (int y = 0; y < height; y++) {
                                bmp.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : Color.WHITE);
                            }
                        }
                        ImageView qr_image = (ImageView)rootView.findViewById(R.id.qrimage);
                        qr_image.setImageBitmap(bmp);
                        cardHide[0]=false;
                        cardQr.setVisibility(View.VISIBLE);
                        cardQr.setAlpha(1.0f);
                        cardQr.animate()
                                .translationY(10)
                                .alpha(1.0f);

                        Button dialogButton = (Button)rootView.findViewById(R.id.ok);
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                File sdcard = Environment.getExternalStorageDirectory();
                                FileOutputStream out = null;
                                File check = new File(sdcard, "/wifipv/");
                                if (!(check.exists())) {
                                    boolean resu = check.mkdir();
                                    if(!resu){return;}
                                }

                                try {
                                    File myFile = new File(sdcard, "/wifipv/" + ssid.getText() + ".png");
                                    out = new FileOutputStream(myFile);
                                    boolean success = bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
                                    if (success) {
                                        MediaScannerConnection.scanFile(c, new String[]{myFile.getAbsolutePath()}, null,
                                                new MediaScannerConnection.OnScanCompletedListener() {
                                                    @Override
                                                    public void onScanCompleted(String path, Uri uri) {
                                                        Log.d(TAG, "file " + path + " was scanned seccessfully: " + uri);
                                                        Intent shareIntent = new Intent();
                                                        shareIntent.setAction(Intent.ACTION_SEND);
                                                        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                                                        shareIntent.setType("image/png");
                                                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                        Intent new_intent = Intent.createChooser(shareIntent, c.getText(R.string.send_to));
                                                        new_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        c.startActivity(new_intent);

                                                    }
                                                });
                                    } else {
                                        Toast.makeText(c, c.getString(R.string.error_share), Toast.LENGTH_LONG).show();
                                    }
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } finally {
                                    if (out != null) try {
                                        out.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
            });
        }else {
            rootView = inflater.inflate(R.layout.fragment_scan, container, false);
            Button scan = (Button)rootView.findViewById(R.id.scan);
            scan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(c, ScanActivity.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    c.startActivity(myIntent);
                }
            });
            Button gallery = (Button)rootView.findViewById(R.id.gallery);
            gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectFromGallery();
                }
            });
            /*final boolean[] tutorialHide = {false};
            final CardView tutorial = (CardView)rootView.findViewById(R.id.card_tutorial);
            tutorial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(tutorialHide[0]){
                        tutorialHide[0]=false;
                        Animation anidelta = new ResizeAnimation(tutorial, tutorial.getHeight());
                        anidelta.setDuration(500);
                        anidelta.setFillAfter(true);
                        tutorial.startAnimation(anidelta);
                    }else{
                        tutorialHide[0]=true;
                        Animation anidelta = new ResizeAnimation(tutorial, 120);
                        anidelta.setDuration(500);
                        anidelta.setFillAfter(true);
                        tutorial.startAnimation(anidelta);
                    }
                }
            });*/
        }
        ViewCompat.setElevation(rootView,50);
		return rootView;
	}

    @SuppressLint("NewApi")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        if (null == data) return;
        Uri originalUri = null;
        if (requestCode == GALLERY_INTENT_CALLED) {
            originalUri = data.getData();
        } else if (requestCode == GALLERY_KITKAT_INTENT_CALLED) {
            originalUri = data.getData();
        }
        MainActivity.handleDecodeImage(originalUri);

    }

    public void selectFromGallery() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, GALLERY_INTENT_CALLED);
        } else {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, GALLERY_KITKAT_INTENT_CALLED);
        }
    }

    public static void connectAP(Result a){
        WifiParsedResult wifiResult = (new WifiResultParser()).parse(a);
        if (wifiResult != null) {
            WifiManager wifiManager = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
            if (wifiManager == null) {
                Toast.makeText(c.getApplicationContext(), c.getString(R.string.error_wifimanager), Toast.LENGTH_LONG).show();
                return;
            }
            Toast.makeText(c.getApplicationContext(), c.getString(R.string.connecting)+wifiResult.getSsid()+"...", Toast.LENGTH_LONG).show();
            new WifiConfigManager(wifiManager).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, wifiResult);
        }else{
            Toast.makeText(c.getApplicationContext(), c.getString(R.string.error_wifiqr), Toast.LENGTH_LONG).show();
        }
    }

}