package com.jooik.tabbeddialog.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.thezero.qrfi
        .R;

/**
 * A simple counterpart for tab1 layout...
 */
public class Fragment_Tab_1 extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_tab_1, container, false);
        CardView c1 = (CardView)view.findViewById(R.id.card_website);
        CardView c2 = (CardView)view.findViewById(R.id.card_source);
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://thezero.org")));
            }
        });
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/TheZ3ro/QRFi")));
            }
        });
        return view;
    }
}