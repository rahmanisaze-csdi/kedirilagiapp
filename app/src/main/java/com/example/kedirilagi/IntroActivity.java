package com.example.kedirilagi;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter;
    TabLayout tabIndicator;
    Button btNext;
    Button btGetStarted;
    int position=0;
    Animation btAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCompat.requestPermissions(IntroActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},0);

        if (restorePrefData()){

            Intent intent = new Intent(getApplicationContext(),LoginRegister.class);
            startActivity(intent);
            finish();

        }

        setContentView(R.layout.activity_intro);

        //ini views
        btNext = findViewById(R.id.bt_next);
        btGetStarted = findViewById(R.id.bt_get_started);
        btAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation);
        tabIndicator = findViewById(R.id.tab_indicator);

        //fill list screen
        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("Alam & Buatan", "Dapatkan informasi objek wisata mulai dari alamat, harga tiket masuk, dan jam buka",R.drawable.i1));
        mList.add(new ScreenItem("Sejarah", "Dapatkan informasi objek wisata mulai dari alamat, harga tiket masuk, dan jam buka",R.drawable.i2));
        mList.add(new ScreenItem("Religi", "Dapatkan informasi objek wisata mulai dari alamat dan jam buka",R.drawable.i3));
        mList.add(new ScreenItem("Kuliner", "Dapatkan informasi objek wisata mulai dari alamat, harga menu, dan jam buka",R.drawable.i4));
        mList.add(new ScreenItem("Pasar & Mall", "Dapatkan informasi objek wisata mulai dari alamat dan jam buka",R.drawable.i5));
        mList.add(new ScreenItem("Cafe", "Dapatkan informasi objek wisata mulai dari alamat, harga menu, jam buka, dan instagram",R.drawable.i6));
        mList.add(new ScreenItem("Directions", "Lihat rute perjalanan menuju objek wisata",R.drawable.i7));

        //setup viewpager
        screenPager = findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this,mList);
        screenPager.setAdapter(introViewPagerAdapter);


        //setup tablayout with viewpager
        tabIndicator.setupWithViewPager(screenPager);

        //next button click listener
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                position = screenPager.getCurrentItem();
                if (position < mList.size()){

                    position++;
                    screenPager.setCurrentItem(position);

                }
                if (position == mList.size()-1){
                    loaddLastScreen();
                }


            }
        });

        //tablayout add change listener

        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == mList.size()-1){

                    loaddLastScreen();

                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //get started button clicklistener
        btGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //open main activity
                Intent intent = new Intent(getApplicationContext(),LoginRegister.class);
                startActivity(intent);
                //need to save a boolean value to storage so next time when the user run the app you could
                //know that it is already checked the intro screen activity
                savePrefsData();
                finish();


            }
        });


    }

    private boolean restorePrefData() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        Boolean isIntroActivityOpenBefore = pref.getBoolean("isIntroOpen", false);
        return isIntroActivityOpenBefore;

    }

    private void savePrefsData() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpen",true);
        editor.commit();

    }

    private void loaddLastScreen() {

        btNext.setVisibility(View.INVISIBLE);
        btGetStarted.setVisibility(View.VISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);
        //setup animation
        btGetStarted.setAnimation(btAnim);

    }
}
