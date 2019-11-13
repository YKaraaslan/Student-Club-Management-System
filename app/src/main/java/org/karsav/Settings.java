package org.karsav;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class Settings extends AppCompatActivity {

    private static final String SETTINGS_PREF_NAME = "textSize";
    Toolbar toolbar;
    Switch notification;
    AppCompatSeekBar textSizeSeekBar;
    TextView version, notificationsText, textSizeText, textSizeTitle, versionControl;
    SharedPreferences sharedPref;
    SharedPreferences.Editor settings;
    int textSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();
    }

    @SuppressLint("CommitPrefEdits")
    private void init() {
        toolbar = findViewById(R.id.toolbar);
        notification = findViewById(R.id.notification);
        textSizeSeekBar = findViewById(R.id.textSize);
        version = findViewById(R.id.version);
        notificationsText = findViewById(R.id.notificationsText);
        textSizeText = findViewById(R.id.textSizeText);
        textSizeTitle = findViewById(R.id.textSizeTitle);
        versionControl = findViewById(R.id.versionControl);

        sharedPref = getSharedPreferences(SETTINGS_PREF_NAME, MODE_PRIVATE);
        settings = sharedPref.edit();
        setSupportActionBar(toolbar);
        View logoView = toolbar.getChildAt(1);
        logoView.setOnClickListener(v -> finish());

        notification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (notification.isChecked())
                Toast.makeText(getApplicationContext(), getString(R.string.notifications_enabled), Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(), getString(R.string.notifications_disabled), Toast.LENGTH_SHORT).show();
        });

        textSize = sharedPref.getInt(getString(R.string.text_size_for_settings), 15);

        textSizeText.setTextSize(textSize);
        notificationsText.setTextSize(textSize);
        notification.setTextSize(textSize + 2);
        textSizeTitle.setTextSize(textSize + 2);
        version.setTextSize(textSize);
        versionControl.setTextSize(textSize + 2);

        textSizeSeekBar.setMax(2);

        if (textSize == 15) {
            textSizeSeekBar.setProgress(0);
            textSizeText.setText(getString(R.string.text_size_small));
        } else if (textSize == 17) {
            textSizeSeekBar.setProgress(1);
            textSizeText.setText(getString(R.string.text_size_medium));
        } else {
            textSizeSeekBar.setProgress(2);
            textSizeText.setText(getString(R.string.text_size_large));
        }
        textSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (progress == 0) {
                    textSizeText.setText(getString(R.string.text_size_small));
                    textSize = 15;
                } else if (progress == 1) {
                    textSizeText.setText(getString(R.string.text_size_medium));
                    textSize = 17;
                } else {
                    textSizeText.setText(getString(R.string.text_size_large));
                    textSize = 19;
                }
                settings.putInt(SETTINGS_PREF_NAME, textSize);
                textSizeText.setTextSize(textSize);
                notificationsText.setTextSize(textSize);
                notification.setTextSize(textSize + 2);
                textSizeTitle.setTextSize(textSize + 2);
                version.setTextSize(textSize);
                versionControl.setTextSize(textSize + 2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        String versionName = BuildConfig.VERSION_NAME;
        version.setText(getString(R.string.this_version, versionName));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.done) {
            settings.apply();
            if (notification.isChecked()) {
                notificationEnable();
            } else
                notificationDisable();
            Toast.makeText(getApplicationContext(), getString(R.string.setting_applied), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.this, HomePage.class));
            finish();
        }
        return true;
    }

    private void notificationEnable() {
        try {
            FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.posts_for_subscription))
                    .addOnCompleteListener(task -> {
                        String msg = "compeleted";
                        if (!task.isSuccessful()) {
                            msg = "failed";
                        }
                        Log.d("Subscription", msg);
                    });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void notificationDisable() {
        try {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(getString(R.string.posts_for_subscription)).addOnCompleteListener(task -> Log.d("Unsubscription: ", "Done"));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
