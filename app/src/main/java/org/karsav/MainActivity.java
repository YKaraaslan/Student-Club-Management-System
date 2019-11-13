package org.karsav;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.BuildConfig;
import androidx.multidex.MultiDex;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class MainActivity extends AppCompatActivity implements TextWatcher, CompoundButton.OnCheckedChangeListener {

    private static final String KEY_PASS = "password";
    private static final String KEY_REMEMBER = "remember";
    private static final String KEY_USERNAME = "username";
    private static final String PREF_NAME = "prefs";
    private static final String TAG = "MainActivity";
    private static final String KEY_UPDATE_ENABLE = "update";
    private static final String KEY_VERSION = "version";
    private static final String KEY_UPDATE_URL = "updateUrl";
    private static final String KEY_UPDATE_TITLE = "updateTitle";
    private static final String KEY_UPDATE_TEXT = "updateText";
    private static final String SETTINGS_PREF_NAME = "textSize";
    boolean doubleBackToExitPressedOnce = false;
    ImageView instagram, karsavorg, linkedin, facebook, twitter, karsav_image;
    DataBase karsav = new DataBase();
    Button log;
    ProgressDialog progressDialog;
    TextInputEditText user, pwd;
    CheckBox rem;
    TextView version, forgot, sign, learn;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ScrollView scroll_view;
    SharedPreferences settings;
    int textSize;
    private FirebaseAuth mAuth;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @SuppressLint({"CommitPrefEdits", "ClickableViewAccessibility"})
    protected void init() {
        learn = findViewById(R.id.learn_more);
        sign = findViewById(R.id.sign_up);
        user = findViewById(R.id.user);
        pwd = findViewById(R.id.password);
        forgot = findViewById(R.id.pwd_fg);
        rem = findViewById(R.id.checkbox);
        log = findViewById(R.id.log_in);
        version = findViewById(R.id.version);
        facebook = findViewById(R.id.facebook);
        instagram = findViewById(R.id.instagram);
        twitter = findViewById(R.id.twitter);
        linkedin = findViewById(R.id.linkedin);
        karsavorg = findViewById(R.id.karsav);
        karsav_image = findViewById(R.id.karsav_image);
        scroll_view = findViewById(R.id.scroll_view);
        mAuth = FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        settings = getSharedPreferences(SETTINGS_PREF_NAME, MODE_PRIVATE);
        textSize = settings.getInt(SETTINGS_PREF_NAME, 15);

        if (sharedPreferences.getBoolean(KEY_REMEMBER, false)) {
            rem.setChecked(true);
        } else {
            rem.setChecked(false);
        }

        learn.setTextSize(textSize + 25);
        sign.setTextSize(textSize);
        user.setTextSize(textSize);
        pwd.setTextSize(textSize);
        forgot.setTextSize(textSize);
        rem.setTextSize(textSize);
        log.setTextSize(textSize);
        version.setTextSize(textSize + 2);

        String versionName = BuildConfig.VERSION_NAME;
        version.setText(versionName);

        karsav_image.setOnClickListener(v -> learn_more());
        user.setOnTouchListener((v, event) -> {
            v.setClickable(true);
            v.setFocusable(true);
            v.setFocusableInTouchMode(true);
            return false;
        });

        pwd.setOnTouchListener((v, event) -> {
            v.setClickable(true);
            v.setFocusable(true);
            v.setFocusableInTouchMode(true);
            return false;
        });

        user.setText(sharedPreferences.getString(KEY_USERNAME, ""));
        pwd.setText(sharedPreferences.getString(KEY_PASS, ""));

        user.addTextChangedListener(this);
        pwd.addTextChangedListener(this);
        rem.setOnCheckedChangeListener(this);

        facebook.setOnClickListener(v -> foo_facebook());

        instagram.setOnClickListener(v -> foo_instagram());

        karsavorg.setOnClickListener(v -> foo_karsavorg());

        twitter.setOnClickListener(v -> foo_twitter());

        linkedin.setOnClickListener(v -> foo_linkedin());

        learn.setOnClickListener(v -> learn_more());

        sign.setOnClickListener(v -> signUp());

        forgot.setOnClickListener(v -> forgotPassword());

        log.setOnClickListener(v -> sign_in());

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(MainActivity.this, instanceIdResult -> {
            String newToken = instanceIdResult.getToken();
            Log.e("newToken", newToken);
        });
        checkUpdate();
    }

    private void signUp() {
        startActivity(new Intent(MainActivity.this, SignUp.class));
    }

    private void forgotPassword() {
        startActivity(new Intent(MainActivity.this, PasswordReset.class));
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    @SuppressLint("StaticFieldLeak")
    public void sign_in() {
        final String blank = getString(R.string.mainactivity_blank);
        final String progress_title = getString(R.string.mainactivity_progress_title);
        final String progress_message = getString(R.string.mainactivity_progress_message);
        final String welcome = getString(R.string.mainactivity_welcome);
        final String wrong = getString(R.string.mainactivity_wrong);
        final String connection = getString(R.string.mainactivity_connection);
        String mail = Objects.requireNonNull(this.user.getText()).toString();
        String pass = Objects.requireNonNull(this.pwd.getText()).toString();

        if (mail.trim().isEmpty() || pass.trim().isEmpty()) {
            Toast.makeText(MainActivity.this, blank, Toast.LENGTH_SHORT).show();
        } else {
            new AsyncTask<String, String, String>() {

                protected void onPreExecute() {
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setTitle(progress_title);
                    progressDialog.setMessage(progress_message);
                    progressDialog.show();
                }

                @Override
                protected String doInBackground(String... strings) {
                    String smail = user.getText().toString().trim();
                    String spass = pwd.getText().toString().trim();
                    if (karsav.Begin()) {
                        return "false";
                    }
                    try {
                        if (!karsav.Query(smail, spass).equals("true")) {
                            return "wrong";
                        }
                        karsav.Disconnect();
                        return "done";
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        return "false";
                    }
                }

                @Override
                protected void onPostExecute(String s) {
                    progressDialog.dismiss();
                    switch (s) {
                        case "false":
                            Toast.makeText(MainActivity.this, connection, Toast.LENGTH_SHORT).show();
                            break;

                        case "wrong":
                            Toast.makeText(MainActivity.this, wrong, Toast.LENGTH_SHORT).show();
                            break;

                        case "done":
                            signed();
                            startActivity(new Intent(MainActivity.this, HomePage.class));
                            Toast.makeText(MainActivity.this, welcome, Toast.LENGTH_SHORT).show();
                            finish();
                            break;
                    }
                }
            }.execute("sign_in");
        }
    }

    private void signed() {
        String mail = Objects.requireNonNull(UserAdapter.getMail()).trim();
        String pass = Objects.requireNonNull(pwd.getText()).toString().trim();
        mAuth.createUserWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "createUserWithEmail:success");
                    }
                });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        managePrefs();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        managePrefs();
    }

    @Override
    public void afterTextChanged(Editable s) {
        managePrefs();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        managePrefs();
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

    private void managePrefs() {
        if (rem.isChecked()) {
            editor.putString(KEY_USERNAME, Objects.requireNonNull(user.getText()).toString().trim());
            editor.putString(KEY_PASS, Objects.requireNonNull(pwd.getText()).toString().trim());
            editor.putBoolean(KEY_REMEMBER, true);
            editor.apply();
        } else {
            editor.putBoolean(KEY_REMEMBER, false);
            editor.remove(KEY_PASS);//editor.putString(KEY_PASS,"");
            editor.remove(KEY_USERNAME);//editor.putString(KEY_USERNAME, "");
            editor.apply();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void checkUpdate() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        String versionName = getVersionName(getApplicationContext());
        Map<String, Object> defaultValue = new HashMap<>();
        defaultValue.put(KEY_UPDATE_ENABLE, false);
        defaultValue.put(KEY_VERSION, versionName);
        defaultValue.put(KEY_UPDATE_URL, getString(R.string.apk_download));
        defaultValue.put(KEY_UPDATE_TITLE, getString(R.string.new_version_available));
        defaultValue.put(KEY_UPDATE_TEXT, getString(R.string.new_version_text));
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(defaultValue);
        long cacheExpiration = 3600;
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }
        mFirebaseRemoteConfig.fetch(cacheExpiration).addOnCompleteListener(this, task -> mFirebaseRemoteConfig.fetchAndActivate());
        if (mFirebaseRemoteConfig.getBoolean(KEY_UPDATE_ENABLE)) {
            if (!mFirebaseRemoteConfig.getString(KEY_VERSION).equalsIgnoreCase(versionName)) {
                startActivity(new Intent(MainActivity.this, Update.class));
                finish();
            }
        }
    }

    public void learn_more() {
        startActivity(new Intent(this, Learn.class));
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

    public void foo_karsavorg() {
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

    public String getVersionName(Context ctx) {
        try {
            return ctx.getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }
}
