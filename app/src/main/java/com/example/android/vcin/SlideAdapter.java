package com.example.android.vcin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SlideAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    public SlideAdapter(Context context){
        this.context=context;
    }
    public int[] slide_images={
            R.drawable.welcome,
            R.drawable.ready,
            R.drawable.open_cam,
            R.drawable.wall1
    };

    public String[] slide_headings={
            "Thanks for Coming Here",
            "What are you waiting for??",
           "Click a picture or Upload",
        " HOW TO CLICK A PICTURE?"
    };
    public String[] description={
            "This app helps you to fetch the vehicle information",
            "One click is all you need to do..choose your own way to add a picture",
            "If you are having pictures just upload from the gallery else just click a picture on your own!",
      "All you need to do open camera through the App \n Find the vehicle you want to capture and then CLICK!!!"
    };


    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.slide_layout,container,false);

        ImageView slideImageview=(ImageView)view.findViewById(R.id.cameraslide);
        TextView slideText= (TextView)view.findViewById(R.id.TitleCam);
        TextView slideDesc= (TextView)view.findViewById(R.id.desc);

        slideImageview.setImageResource(slide_images[position]);
        slideText.setText(slide_headings[position]);
        slideDesc.setText(description[position]);
//
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
