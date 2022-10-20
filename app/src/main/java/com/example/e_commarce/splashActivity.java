package com.example.e_commarce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;


import com.example.e_commarce.databinding.ActivitySplashBinding;

public class splashActivity extends AppCompatActivity {


    private ActivitySplashBinding binding;
    private SharedPreferences spf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        binding = ActivitySplashBinding.inflate(getLayoutInflater());

        spf = getSharedPreferences(constsp.PREF,MODE_PRIVATE);


        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());


        AlphaAnimation animation = new AlphaAnimation(0,1);
        animation.setDuration(3000);
        animation.setRepeatCount(1);
        binding.img.setAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                if(spf.getString(constsp.email,"").toString().equalsIgnoreCase(""))
                {
                        new common_method(splashActivity.this,SingupActivity.class);
                }
                else {
                    new common_method(splashActivity.this, homepage.class);
                }
                finish();
            }
        },3000);

    }
}