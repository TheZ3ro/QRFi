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
public class Fragment_Tab_3 extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_tab_3, container, false);
        CardView github = (CardView)view.findViewById(R.id.card_github);
        CardView twitter = (CardView)view.findViewById(R.id.card_twitter);
        CardView plus = (CardView)view.findViewById(R.id.card_plus);
        github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/TheZ3ro")));
            }
        });
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/Th3Zer0")));
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/+DavideTheZero")));
            }
        });
        return view;
    }
}