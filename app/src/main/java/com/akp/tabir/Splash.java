package com.akp.tabir;

import com.akp.tabir.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends Activity{
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
                public void run() {
                        startActivity(new Intent(Splash.this, ActivityMain.class));
                        finish();
                }
        }, secondsDelayed * 10);
    }
}
