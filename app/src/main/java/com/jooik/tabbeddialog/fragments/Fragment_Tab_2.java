package com.jooik.tabbeddialog.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.thezero.qrfi.R;

/**
 * A simple counterpart for tab1 layout...
 */
public class Fragment_Tab_2 extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_tab_2, container, false);
        CardView c2 = (CardView)view.findViewById(R.id.card_licon);
        CardView c3 = (CardView)view.findViewById(R.id.card_lfab);
        CardView c4 = (CardView)view.findViewById(R.id.card_ltab1);
        CardView c5 = (CardView)view.findViewById(R.id.card_ltab2);
        CardView c6 = (CardView)view.findViewById(R.id.card_ltab3);
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://martz90.deviantart.com/")));
            }
        });
        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/zxing/zxing")));
            }
        });
        c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/kiteflo/Android_Tabbed_Dialog")));
            }
        });
        c5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/jpardogo/PagerSlidingTabStrip")));
            }
        });
        c6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/mitoyarzun/zxingfragmentlib")));
            }
        });
        return view;
    }
}