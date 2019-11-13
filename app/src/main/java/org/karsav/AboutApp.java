package org.karsav;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.r0adkll.slidr.Slidr;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class AboutApp extends AppCompatActivity {

    private static final String SETTINGS_PREF_NAME = "textSize";
    Button button;
    Toolbar toolbar;
    TextView head, text;
    SharedPreferences settings;
    int textSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        Slidr.attach(this);
        init();
    }

    public void init() {
        button = findViewById(R.id.back);
        head = findViewById(R.id.head);
        text = findViewById(R.id.text);

        settings = getSharedPreferences(SETTINGS_PREF_NAME, MODE_PRIVATE);
        textSize = settings.getInt(SETTINGS_PREF_NAME, 15);

        head.setTextSize(textSize + 25);
        text.setTextSize(textSize);
        button.setTextSize(textSize);

        button.setOnClickListener(v -> finish());

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        View logoView = toolbar.getChildAt(1);
        logoView.setOnClickListener(v -> finish());
    }
}
