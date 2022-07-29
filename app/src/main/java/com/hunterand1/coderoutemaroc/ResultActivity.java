package com.hunterand1.coderoutemaroc;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {
    int IdSerie;
    TextView note;
    ListView listView;
    ImageView imageViewPartage;
    int InterstitialResultCount = 0;
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
        setContentView(R.layout.activity_result);

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
        IdSerie = b.getInt("IdSerie");

        imageViewPartage = (ImageView) findViewById(R.id.imageViewPartage);
        note = (TextView) findViewById(R.id.Note);
        listView = (ListView) findViewById(R.id.listView);

        int i = 0;
        for (int x = 0;x<VariableGlobal.Results.size();x++)
        {
            if(VariableGlobal.Results.get(x).Status)
            i++;
        }

        note.setText(i+" / "+VariableGlobal.Results.size());

        final MyCustomAdapter mca = new MyCustomAdapter(this, VariableGlobal.Results);
        listView.setAdapter(mca);

        imageViewPartage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, " لقد حصلت على نتيجة\n" +
                        "\n" +note.getText().toString()+
                        " حمل التطبيق خاص بالتعليم سياقة تعليم \n" +
                        "\n" +
                        "http://play.google.com/store/apps/details?id=" + getApplication().getPackageName());
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "نشر"));
            }
        });
    }

    class MyCustomAdapter extends BaseAdapter {
        ArrayList<Result> lic = new ArrayList<Result>();
        Context c;

        MyCustomAdapter(Context c, ArrayList<Result> lic) {
            this.c = c;
            this.lic = lic;
        }

        @Override
        public int getCount() {
            return lic.size();
        }

        @Override
        public Object getItem(int position) {
            return lic.get(position);
        }

        @Override
        public long getItemId(int position) {
            return lic.indexOf(getItem(position));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater li = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null)
                convertView = li.inflate(R.layout.row_result, null);

            TextView tv = (TextView) convertView.findViewById(R.id.textView2);
            ImageView iv = (ImageView) convertView.findViewById(R.id.imageView2);

            final int IdQuest = position;
            tv.setText(getApplication().getResources().getString(R.string.ha_number_q)+" "+(position+1));
            iv.setImageResource((lic.get(position).Status)?R.drawable.checkmark:R.drawable.close);

            View.OnClickListener OCL = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InterstitialResultCount++;
                    int CountAds = Integer.parseInt(getApplication().getResources().getString(R.string.interstitial_count));
                    if(InterstitialResultCount > CountAds && mInterstitialAd.isLoaded())
                    {
                        mInterstitialAd.show();
                        InterstitialResultCount = 0;
                    }
                    Intent startMain = new Intent(c,QActivity.class);
                    Bundle b = new Bundle();
                    b.putInt("IdSerie",IdSerie);
                    b.putInt("IdQuest",IdQuest);
                    startMain.putExtras(b);
                    startActivity(startMain);
                }
            };

            iv.setOnClickListener(OCL);
            tv.setOnClickListener(OCL);

            return convertView;
        }

    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(this,ListActivity.class);
        startActivity(startMain);
        finish();
    }

}
