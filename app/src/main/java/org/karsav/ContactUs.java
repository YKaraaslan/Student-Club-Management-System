package org.karsav;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.r0adkll.slidr.Slidr;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class ContactUs extends AppCompatActivity {

    private static final String SETTINGS_PREF_NAME = "textSize";
    ImageView facebook;
    ImageView instagram;
    ImageView karsav;
    ImageView linkedin;
    ImageView twitter;
    Toolbar toolbar;
    TextView head, location, mail_first, mail_second;
    SharedPreferences settings;
    int textSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        Slidr.attach(this);
        init();
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        facebook = findViewById(R.id.facebook);
        instagram = findViewById(R.id.instagram);
        twitter = findViewById(R.id.twitter);
        linkedin = findViewById(R.id.linkedin);
        karsav = findViewById(R.id.karsav);

        head = findViewById(R.id.head);
        location = findViewById(R.id.location);
        mail_first = findViewById(R.id.mail_first);
        mail_second = findViewById(R.id.mail_second);

        facebook.setOnClickListener(v -> foo_facebook());
        instagram.setOnClickListener(v -> foo_instagram());
        karsav.setOnClickListener(v -> foo_karsav());
        twitter.setOnClickListener(v -> foo_twitter());
        linkedin.setOnClickListener(v -> foo_linkedin());

        setSupportActionBar(toolbar);
        View logoView = toolbar.getChildAt(1);
        logoView.setOnClickListener(v -> finish());

        settings = getSharedPreferences(SETTINGS_PREF_NAME, MODE_PRIVATE);
        textSize = settings.getInt(SETTINGS_PREF_NAME, 15);

        head.setTextSize(textSize + 15);
        location.setTextSize(textSize);
        mail_first.setTextSize(textSize);
        mail_second.setTextSize(textSize);
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
