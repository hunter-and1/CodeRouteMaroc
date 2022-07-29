package com.hunterand1.coderoutemaroc;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class ListActivity extends AppCompatActivity {

    Context c;
    LinearLayout listLinearLayout;

    InterstitialAd mInterstitialAd;
    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice("4EDD9589AE7EDDBEC95AE8EB5E55CED3")
                .build();
        mInterstitialAd.loadAd(adRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        c = this;
        listLinearLayout = (LinearLayout)findViewById(R.id.listLinearLayout);

        MobileAds.initialize(getApplicationContext(),getString(R.string.app_id));

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice("4EDD9589AE7EDDBEC95AE8EB5E55CED3")
                .build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.inter_ad_id));
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });
        requestNewInterstitial();

        Button btRandom = new Button(this);
        btRandom.setText(getString(R.string.ha_list_random));
        btRandom.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorClick), PorterDuff.Mode.MULTIPLY);
        btRandom.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        btRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
                Intent t = new Intent(c,StartExameActivity.class);
                Bundle b = new Bundle();
                b.putInt("Index",-1);
                t.putExtras(b);
                startActivity(t);
            }
        });

        listLinearLayout.addView(btRandom);

        for (int x = 0;x< VariableGlobal.getSeries().size();x++)
        {
            Button bt = new Button(this);
            bt.setTag(x);
            bt.setText(VariableGlobal.getSeries().get(x).Label);
            bt.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    }
                    Intent t = new Intent(c,StartExameActivity.class);
                    Bundle b = new Bundle();
                    b.putInt("Index",Integer.parseInt(view.getTag().toString()));
                    t.putExtras(b);
                    startActivity(t);
                }
            });

            listLinearLayout.addView(bt);
        }


    }

}
