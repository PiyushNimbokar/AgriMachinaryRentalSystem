package com.example.minor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SelectionActivity extends AppCompatActivity {

    ViewPager mSlideViewPager;
    LinearLayout mDotLayout;

    TextView[] dots;
    ViewPagerAdapter viewPagerAdapter;

    private String category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        mSlideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        mDotLayout = (LinearLayout) findViewById(R.id.indicator_layout);

        viewPagerAdapter = new ViewPagerAdapter(this);

        mSlideViewPager.setAdapter(viewPagerAdapter);

        setUpindicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);

        Intent intent = getIntent();
        category = intent.getStringExtra("category");

        Button rentButton = findViewById(R.id.rentButton);
        rentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRentActivity();
            }
        });

        Button hireButton = findViewById(R.id.hireButton);
        hireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startItemsAvailableActivity();
            }
        });

    }
    public void setUpindicator(int position){

        dots=new TextView[3];
        mDotLayout.removeAllViews();

        for(int i=0; i< dots.length; i++){

            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.inactive,getApplicationContext().getTheme()));
            mDotLayout.addView(dots[i]);
        }

        dots[position].setTextColor(getResources().getColor(R.color.lightgreen,getApplicationContext().getTheme()));
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setUpindicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private int getitem(int i){

        return mSlideViewPager.getCurrentItem() + i;
    }

    private void startRentActivity() {
        Intent intent = new Intent(this, RentActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }

    private void startItemsAvailableActivity() {
        Intent intent = new Intent(this, ItemsAvailableActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }

}

