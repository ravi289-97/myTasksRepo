package com.example.android.vcin;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ViewPager mviewpage;
    private LinearLayout mdots;
    private SlideAdapter slideAdapter;
    private TextView[] dot;
    private Button nxtbtn;
    private Button bckbtn;
    private int currpage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mviewpage= (ViewPager)findViewById(R.id.slideviewer);
        mdots=(LinearLayout)findViewById(R.id.dots);
        bckbtn=(Button)findViewById(R.id.bck);
        nxtbtn=(Button)findViewById(R.id.Nxt);

        slideAdapter=new SlideAdapter(this);
        mviewpage.setAdapter(slideAdapter);

        addDots(0);
        mviewpage.addOnPageChangeListener(vlisten);


        nxtbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currpage == dot.length-1){
                    Intent intent;

                    intent=new Intent(getApplicationContext(),Front.class);
                    startActivity(intent);

                }
                mviewpage.setCurrentItem(currpage+1);
            }
        });
        bckbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mviewpage.setCurrentItem(currpage-1);
            }
        });
    }
    public void addDots(int pos)
    {
        dot=new TextView[4];
        mdots.removeAllViews();
        for(int i=0;i<dot.length;i++)
        {
            dot[i]=new TextView(this);
            dot[i].setText(Html.fromHtml("&#8226;"));
            dot[i].setTextSize(35);
            dot[i].setTextColor(getResources().getColor(R.color.transwhite));
            mdots.addView(dot[i]);
        }
        if(dot.length>0){
            dot[pos].setTextColor(getResources().getColor(R.color.white));
        }
    }
    ViewPager.OnPageChangeListener vlisten = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int i) {
            addDots(i);
            currpage=i;

            if(i == 0)
            {
                nxtbtn.setEnabled(true);
                bckbtn.setEnabled(false);
                bckbtn.setVisibility(View.INVISIBLE);
                nxtbtn.setText("Next");
                bckbtn.setText("");
            }else if(i == dot.length-1)
            {
                nxtbtn.setVisibility(View.VISIBLE);
                bckbtn.setVisibility(View.VISIBLE);
                nxtbtn.setEnabled(true);
                nxtbtn.setText("FINISH");
                bckbtn.setEnabled(true);
                bckbtn.setText("BACK");
            }else{
                nxtbtn.setVisibility(View.VISIBLE);
                bckbtn.setVisibility(View.VISIBLE);
                nxtbtn.setEnabled(true);
                nxtbtn.setText("NEXT");
                bckbtn.setEnabled(true);
                bckbtn.setText("BACK");
            }


        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
