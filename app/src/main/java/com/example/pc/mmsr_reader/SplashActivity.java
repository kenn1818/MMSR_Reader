package com.example.pc.mmsr_reader;

import android.content.Intent;
import android.view.WindowManager;

import com.daimajia.androidanimations.library.Techniques;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

/**
 * Created by pc on 11/19/2017.
 */

public class SplashActivity extends AwesomeSplash{
    @Override
    public void initSplash(ConfigSplash configSplash) {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Background Animation
        configSplash.setBackgroundColor(R.color.bg_splash);
        configSplash.setAnimCircularRevealDuration(3000);
        configSplash.setRevealFlagX(Flags.REVEAL_LEFT);
        configSplash.setRevealFlagX(Flags.REVEAL_BOTTOM);

        //Logo
        configSplash.setLogoSplash(R.drawable.storybook);
        configSplash.setAnimLogoSplashDuration(5000);
        configSplash.setAnimLogoSplashTechnique(Techniques.Bounce);

        //Title
        configSplash.setTitleSplash("MMSR Reader");
        configSplash.setTitleTextColor(R.color.colorAccent);
        configSplash.setTitleTextSize(30f);
        configSplash.setAnimTitleDuration(3000);
        configSplash.setAnimTitleTechnique(Techniques.FlipInX);
    }

    @Override
    public void animationsFinished() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
    }
}