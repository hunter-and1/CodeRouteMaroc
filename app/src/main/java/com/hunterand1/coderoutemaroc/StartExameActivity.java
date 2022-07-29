package com.hunterand1.coderoutemaroc;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class StartExameActivity extends AppCompatActivity {
    Context c;
    TextView textSerie;
    Button startExame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_exame);
        c = this;
        Bundle b = getIntent().getExtras();
        final int Index = b.getInt("Index");

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice("4EDD9589AE7EDDBEC95AE8EB5E55CED3")
                .build();
        mAdView.loadAd(adRequest);

        textSerie = (TextView) findViewById(R.id.textSerie);
        startExame = (Button)findViewById(R.id.btnStartExame);
        if(Index != -1)
            textSerie.setText(VariableGlobal.getSeries().get(Index).Label);
        else {
            textSerie.setText(getString(R.string.ha_list_random));


        }

        startExame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent t = new Intent(c,MainActivity.class);
                Bundle b = new Bundle();
                b.putInt("Index",Index);
                t.putExtras(b);
                startActivity(t);
                finish();
            }
        });
    }


}
