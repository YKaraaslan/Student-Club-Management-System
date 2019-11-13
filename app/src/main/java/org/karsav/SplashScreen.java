package org.karsav;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class SplashScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        try {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        init();
    }

    private void init() {

        getWindow().setBackgroundDrawableResource(R.drawable.splash);

        int delay = 1000;
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashScreen.this, LoggedDataBase.class));
            finish();
        }, delay);
    }
}
