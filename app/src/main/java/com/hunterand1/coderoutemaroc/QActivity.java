package com.hunterand1.coderoutemaroc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class QActivity extends AppCompatActivity {

    TextView headerSerie;
    ImageView imageQuestion;
    Button btnBack,btn1,btn2,btn3,btn4;

    Question q;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice("4EDD9589AE7EDDBEC95AE8EB5E55CED3")
                .build();
        mAdView.loadAd(adRequest);

        Bundle b = getIntent().getExtras();
        if(b.getInt("IdSerie") == -1)
            q = VariableGlobal.RandomSerie.Questions.get(b.getInt("IdQuest"));
        else
            q = VariableGlobal.getSeries().get(b.getInt("IdSerie")).Questions.get(b.getInt("IdQuest"));

        headerSerie = (TextView) findViewById(R.id.headerSerie);
        imageQuestion = (ImageView)findViewById(R.id.imageQuestion);

        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);
        btn3 = (Button)findViewById(R.id.btn3);
        btn4 = (Button)findViewById(R.id.btn4);
        btnBack = (Button)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        headerSerie.setText(getApplication().getResources().getString(R.string.ha_number_q)+" "+q.ID);
        imageQuestion.setBackgroundResource(q.Image);

        btn1.setVisibility(View.INVISIBLE);
        btn2.setVisibility(View.INVISIBLE);
        btn3.setVisibility(View.INVISIBLE);
        btn4.setVisibility(View.INVISIBLE);

        if(q.CodeResponse.contains("1"))
            btn1.setVisibility(View.VISIBLE);
        if(q.CodeResponse.contains("2"))
            btn2.setVisibility(View.VISIBLE);
        if(q.CodeResponse.contains("3"))
            btn3.setVisibility(View.VISIBLE);
        if(q.CodeResponse.contains("4"))
            btn4.setVisibility(View.VISIBLE);
    }
}
