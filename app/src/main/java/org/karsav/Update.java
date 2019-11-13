package org.karsav;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.Locale;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class Update extends AppCompatActivity {

    private static final String KEY_VERSION = "version";
    private static final String KEY_UPDATE_URL = "updateUrl";
    private static final String KEY_UPDATE_TEXT_TR = "updateTextTr";
    private static final String KEY_UPDATE_TEXT_EN = "updateTextEn";
    private static final String KEY_UPDATE_VISIT_APK = "updateVisitApk";
    private static final String SETTINGS_PREF_NAME = "textSize";
    boolean doubleBackToExitPressedOnce = false;
    String text, title;
    Button download, visit;
    TextView updateIsFor, currentVersion, updateText;
    SharedPreferences settings;
    int textSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        init();
    }

    private void init() {
        download = findViewById(R.id.download);
        visit = findViewById(R.id.visit);
        updateIsFor = findViewById(R.id.update_is_for);
        currentVersion = findViewById(R.id.current_version);
        updateText = findViewById(R.id.update_text);
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        final Uri uri = Uri.parse(mFirebaseRemoteConfig.getString(KEY_UPDATE_URL));
        final Uri visitApk = Uri.parse(mFirebaseRemoteConfig.getString(KEY_UPDATE_VISIT_APK));

        download.setOnClickListener(v -> downloadApk(uri));

        visit.setOnClickListener(v -> visitApk(visitApk));

        String versionName = BuildConfig.VERSION_NAME;
        String language = Locale.getDefault().getLanguage();

        if (language.equalsIgnoreCase("tr"))
            text = mFirebaseRemoteConfig.getString(KEY_UPDATE_TEXT_TR);
        else
            text = mFirebaseRemoteConfig.getString(KEY_UPDATE_TEXT_EN);

        String versionString = getString(R.string.update_for, mFirebaseRemoteConfig.getString(KEY_VERSION));
        String currentString = getString(R.string.this_version, versionName);
        updateIsFor.setText(versionString);
        currentVersion.setText(currentString);
        updateText.setText(text);

        settings = getSharedPreferences(SETTINGS_PREF_NAME, MODE_PRIVATE);
        textSize = settings.getInt(SETTINGS_PREF_NAME, 15);

        download.setTextSize(textSize);
        visit.setTextSize(textSize);
        updateIsFor.setTextSize(textSize);
        currentVersion.setTextSize(textSize);
        updateText.setTextSize(textSize);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.clickOneMoreTimeToExit), Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    private void downloadApk(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void visitApk(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
