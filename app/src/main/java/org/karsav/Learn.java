package org.karsav;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.r0adkll.slidr.Slidr;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class Learn extends AppCompatActivity {

    private static final String SETTINGS_PREF_NAME = "textSize";
    Button backs;
    ImageView facebook;
    ImageView instagram;
    ImageView karsav;
    ImageView linkedin;
    ImageView twitter;
    Toolbar toolbar;
    TextView whoAreWe, whoAreWeText, ourMission, ourMissionText, ourVision, ourVisionText, ourAims, ourAimsText, socialMedia, learnMore;
    SharedPreferences settings;
    int textSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        Slidr.attach(this);
        init();
    }

    private void init() {
        backs = findViewById(R.id.back);
        facebook = findViewById(R.id.facebook);
        instagram = findViewById(R.id.instagram);
        twitter = findViewById(R.id.twitter);
        linkedin = findViewById(R.id.linkedin);
        karsav = findViewById(R.id.karsav);

        whoAreWe = findViewById(R.id.whoAreWe);
        whoAreWeText = findViewById(R.id.whoAreWeText);
        ourMission = findViewById(R.id.ourMission);
        ourMissionText = findViewById(R.id.ourMissionText);
        ourVision = findViewById(R.id.ourVision);
        ourVisionText = findViewById(R.id.ourVisionText);
        ourAims = findViewById(R.id.ourAims);
        ourAimsText = findViewById(R.id.ourAimsText);
        socialMedia = findViewById(R.id.socialMedia);
        learnMore = findViewById(R.id.learn_more);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        View logoView = toolbar.getChildAt(1);
        logoView.setOnClickListener(v -> finish());

        settings = getSharedPreferences(SETTINGS_PREF_NAME, MODE_PRIVATE);
        textSize = settings.getInt(SETTINGS_PREF_NAME, 15);

        learnMore.setTextSize(textSize + 25);
        whoAreWe.setTextSize(textSize + 18);
        whoAreWeText.setTextSize(textSize);
        ourMission.setTextSize(textSize + 18);
        ourMissionText.setTextSize(textSize);
        ourVision.setTextSize(textSize + 18);
        ourVisionText.setTextSize(textSize);
        ourAims.setTextSize(textSize + 18);
        ourAimsText.setTextSize(textSize);
        socialMedia.setTextSize(textSize + 10);
        backs.setTextSize(textSize);

        backs.setOnClickListener(v -> foo_back());
        facebook.setOnClickListener(v -> foo_facebook());
        instagram.setOnClickListener(v -> foo_instagram());
        karsav.setOnClickListener(v -> foo_karsav());
        twitter.setOnClickListener(v -> foo_twitter());
        linkedin.setOnClickListener(v -> foo_linkedin());
    }

    public void foo_back() {
        finish();
    }

    public void foo_facebook() {
        try {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("fb://page/458119124571855")));
        } catch (Exception e) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://www.facebook.com/KarabukKarsav/")));
        }
    }

    public void foo_instagram() {
        try {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("instagram://5895117881")));
        } catch (Exception e) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://www.instagram.com/karabukkarsav/")));
        }
    }

    public void foo_karsav() {
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://www.karsavImage.org")));
    }

    public void foo_twitter() {
        try {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("twitter://899298139785637888")));
        } catch (Exception e) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://twitter.com/karabukkarsav")));
        }
    }

    public void foo_linkedin() {
        try {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("linkedin://ituEOp5ER260LZsShFgduw==")));
        } catch (Exception e) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://www.linkedin.com/in/karabukkarsav/")));
        }
    }
}
