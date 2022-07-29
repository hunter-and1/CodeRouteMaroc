package com.hunterand1.coderoutemaroc;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    Context c;
    int indexQuesion;
    Serie serie;
    TextView headerSerie;
    ImageView imageQuestion;
    ProgressBar timeLine;
    Button btnNext,btnClear,btnPause;
    ToggleButton btn1,btn2,btn3,btn4;

    int MaxProgress;
    Timer myTimer;
    Boolean Pause = false;
    int time = 0;
    int index;

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
        setContentView(R.layout.activity_main);
        c = this;

        MaxProgress = Integer.parseInt(getApplication().getResources().getString(R.string.times_question));

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

        Bundle b = getIntent().getExtras();
        index = b.getInt("Index");
        if(index == -1)
        {
            VariableGlobal.setRandomQuestion();
            serie = VariableGlobal.RandomSerie;
        }
        else
            serie = VariableGlobal.getSeries().get(index);
        indexQuesion = 0;

        VariableGlobal.Results  = new ArrayList<Result>();

        headerSerie = (TextView) findViewById(R.id.headerSerie);
        imageQuestion = (ImageView)findViewById(R.id.imageQuestion);
        timeLine= (ProgressBar)findViewById(R.id.timeLine);
        timeLine.setMax(MaxProgress);
        btn1 = (ToggleButton)findViewById(R.id.btn1);
        btn2 = (ToggleButton)findViewById(R.id.btn2);
        btn3 = (ToggleButton)findViewById(R.id.btn3);
        btn4 = (ToggleButton)findViewById(R.id.btn4);

        btnNext = (Button)findViewById(R.id.btnNext);
        btnClear = (Button)findViewById(R.id.btnClear);
        btnPause = (Button)findViewById(R.id.btnPause);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextQuestion(false);
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {btn1.setChecked(false);btn2.setChecked(false);btn3.setChecked(false);btn4.setChecked(false);}
        });
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pause = !Pause;
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
                Intent t = new Intent(c,PauseActivity.class);
                startActivity(t);
            }
        });

        myTimer = new Timer();
        myTimer.schedule(aTask, 0, 1000);

        nextQuestion(true);
    }

    TimerTask aTask = new TimerTask() {
        @Override
        public void run() {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MainActivity.this.timeLine.setProgress(time);
                    if(time == MaxProgress)
                    {
                        MainActivity.this.nextQuestion(false);
                    }
                }
            });
            if(!Pause)
            time += 1;
        }
    };

    public void nextQuestion(boolean first)
    {
        time = 0;
        if(!first)
        {
            String r = ((btn1.isChecked())?"1":"")+""+((btn2.isChecked())?"2":"")+""+((btn3.isChecked())?"3":"")+""+((btn4.isChecked())?"4":"");
            VariableGlobal.Results.add(new Result(serie.Questions.get(indexQuesion).ID,serie.Questions.get(indexQuesion).CodeResponse.contentEquals(r),time));
            indexQuesion++;
        }

        if(serie.Questions.size() > indexQuesion)
        {
            btn1.setChecked(false);
            btn2.setChecked(false);
            btn3.setChecked(false);
            btn4.setChecked(false);

            headerSerie.setText(getApplication().getResources().getString(R.string.ha_number_q)+" "+serie.Questions.get(indexQuesion).ID);
            timeLine.setProgress(0);
            imageQuestion.setBackgroundResource(serie.Questions.get(indexQuesion).Image);
            if(serie.Questions.get(indexQuesion).Count == 2)
            {
                btn1.setVisibility(View.VISIBLE);
                btn2.setVisibility(View.VISIBLE);
                btn3.setVisibility(View.INVISIBLE);
                btn4.setVisibility(View.INVISIBLE);
            }
            else if(serie.Questions.get(indexQuesion).Count == 3)
            {
                btn1.setVisibility(View.VISIBLE);
                btn2.setVisibility(View.VISIBLE);
                btn3.setVisibility(View.VISIBLE);
                btn4.setVisibility(View.INVISIBLE);
            }
            else if(serie.Questions.get(indexQuesion).Count == 4)
            {
                btn1.setVisibility(View.VISIBLE);
                btn2.setVisibility(View.VISIBLE);
                btn3.setVisibility(View.VISIBLE);
                btn4.setVisibility(View.VISIBLE);
            }

        }
        else{
            myTimer.cancel();
            Intent t = new Intent(this,ResultActivity.class);
            Bundle b = new Bundle();
            b.putInt("IdSerie",index);
            t.putExtras(b);
            startActivity(t);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Pause = false;
    }
}

