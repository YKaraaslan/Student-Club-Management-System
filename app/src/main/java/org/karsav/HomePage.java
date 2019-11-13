package org.karsav;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.multidex.MultiDex;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String LOGIN_STATU = "logged";
    private static final String KEY_REMEMBER = "remember";
    private static final String PREF_NAME = "prefs";
    private static final String KEY_UPDATE_ENABLE = "update";
    private static final String KEY_VERSION = "version";
    private static final String KEY_UPDATE_URL = "updateUrl";
    private static final String KEY_UPDATE_TEXT_TR = "updateTextTr";
    private static final String KEY_UPDATE_TEXT_EN = "updateTextEn";
    private static final String KEY_UPDATE_VISIT_APK = "updateVisitApk";
    private static final String SETTINGS_PREF_NAME = "textSize";
    String name, surname, position;
    String full_name;
    boolean doubleBackToExitPressedOnce = false;
    View headerView;
    TextView navPosition;
    SharedPreferences sharedPreferences, loginSp;
    SharedPreferences.Editor loginSpEditor;
    DrawerLayout drawer;
    ProgressDialog progressDialog;
    DataBase karsav = new DataBase();
    CircleImageView ourImage;
    NavigationView navigationView;
    TextView nameSurnameTextView;
    SharedPreferences settingsPreferences;
    int textSize;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settingsPreferences = getSharedPreferences(SETTINGS_PREF_NAME, MODE_PRIVATE);
        textSize = settingsPreferences.getInt(SETTINGS_PREF_NAME, 15);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        init();
        setAbout();
    }

    private void init() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        drawer.setScrimColor(Color.TRANSPARENT);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_posts);
        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_posts));
        navigationView.setItemIconTintList(null);

        Menu menu = navigationView.getMenu();
        authorizations(menu);

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        loginSp = getSharedPreferences(LOGIN_STATU, MODE_PRIVATE);
        loginSpEditor = loginSp.edit();
        if (sharedPreferences.getBoolean(KEY_REMEMBER, true)) {
            subscribeToTopic();
            loginSpEditor.putBoolean(LOGIN_STATU, true);
        }
        loginSpEditor.apply();

        if (textSize == 15) {
            navigationView.setItemTextAppearance(R.style.NavDrawerTextStyle1);
        } else if (textSize == 17) {
            navigationView.setItemTextAppearance(R.style.NavDrawerTextStyle2);
        } else {
            navigationView.setItemTextAppearance(R.style.NavDrawerTextStyle3);
        }
        position = UserAdapter.getPozisyon();
        name = UserAdapter.getUserName();
        surname = UserAdapter.getUserSurName();
        full_name = name + " " + surname;
        try {
            headerView = navigationView.getHeaderView(0);
            nameSurnameTextView = headerView.findViewById(R.id.nameSurnamed);
            nameSurnameTextView.setText(full_name);
            navPosition = headerView.findViewById(R.id.position);
            navPosition.setText(position);
            ourImage = headerView.findViewById(R.id.karsav);
            StorageReference reference = storageReference.child("ProfilePhotos/" + UserAdapter.getUserId() + ".jpg");
            reference.getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                ourImage.setImageBitmap(bmp);
            });

            nameSurnameTextView.setTextSize(textSize);
            navPosition.setTextSize(textSize);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        checkUpdate();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String formattedDate = "Online";
        String fullName = name + " " + surname;
        try {
            db.collection(getString(R.string.firestore_database_for_users)).document(String.valueOf(UserAdapter.getUserId()))
                    .set(new FirebaseUserAdapter(fullName, formattedDate, position, UserAdapter.getUserId())).addOnCompleteListener(task -> {
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        final String formattedDate = df.format(c.getTime());
        try {
            db.collection(getString(R.string.firestore_database_for_users)).document(String.valueOf(UserAdapter.getUserId()))
                    .update("status", formattedDate);
        } catch (Exception ex) {
            ex.printStackTrace();
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
        defaultValue.put(KEY_UPDATE_TEXT_TR, getString(R.string.new_version_text));
        defaultValue.put(KEY_UPDATE_TEXT_EN, getString(R.string.new_version_text));
        defaultValue.put(KEY_UPDATE_VISIT_APK, getString(R.string.apk_download));
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
        if (mFirebaseRemoteConfig.getBoolean(KEY_UPDATE_ENABLE) || mFirebaseRemoteConfig.getString(KEY_UPDATE_ENABLE).equalsIgnoreCase("True")) {
            if (!mFirebaseRemoteConfig.getString(KEY_VERSION).equalsIgnoreCase(versionName)) {
                startActivity(new Intent(HomePage.this, Update.class));
                finish();
            }
        }
    }

    private void subscribeToTopic() {
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

    private void authorizations(Menu menu) {
        if (UserAdapter.getUyeGo() == null && UserAdapter.getRefGo() == null
                && UserAdapter.getKayitGo() == null && UserAdapter.getGrvGo() == null) {
            menu.findItem(R.id.nav_registers).setVisible(false);
        }
        if (UserAdapter.getProjeGo() == null) {
            menu.findItem(R.id.nav_projects).setVisible(false);
        }
        if (UserAdapter.getTopGo() == null || UserAdapter.getTumTopGo() == null) {
            menu.findItem(R.id.nav_meetups).setVisible(false);
        }
        if (UserAdapter.getMaliyeGo() == null) {
            menu.findItem(R.id.nav_finance).setVisible(false);
        }
        if (UserAdapter.getUserName() == null) {
            menu.findItem(R.id.nav_assignments).setVisible(false);
            menu.findItem(R.id.nav_profile).setVisible(false);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, getString(R.string.clickOneMoreTimeToExit), Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    private void onInviteClicked() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/html");
        String title = getString(R.string.invitation_message) + "\n\n" + mFirebaseRemoteConfig.getString(KEY_UPDATE_URL);
        share.putExtra(Intent.EXTRA_TEXT, title);
        startActivity(share);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, Settings.class));
        }
        if (id == R.id.action_share) {
            onInviteClicked();
        }
        if (id == R.id.action_info) {
            startActivity(new Intent(this, Learn.class));
        }
        if (id == R.id.action_info_app) {
            startActivity(new Intent(this, AboutApp.class));
        }
        if (id == R.id.action_contact_us) {
            startActivity(new Intent(this, ContactUs.class));
        }
        if (id == R.id.action_logout) {
            String sure = getString(R.string.homepage_sure);
            String exit_yes = getString(R.string.homepage_exit_yes);
            String exit_no = getString(R.string.homepage_exit_no);
            AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
            builder.setMessage(sure).setPositiveButton(exit_yes, (dialog, which) -> {
                FirebaseAuth.getInstance().signOut();
                logOut();
            }).setNegativeButton(exit_no, (dialog, which) -> {

            }).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_posts) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Posts()).commit();
        } else if (id == R.id.nav_registers) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Registers()).commit();
        } else if (id == R.id.nav_projects) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Projects()).commit();
        } else if (id == R.id.nav_meetups) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MeetUps()).commit();
        } else if (id == R.id.nav_assignments) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Assignments()).commit();
        } else if (id == R.id.nav_finance) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Finance()).commit();
        } else if (id == R.id.nav_profile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Profile()).commit();
        } else if (id == R.id.nav_users) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Users()).commit();
        }
        navigationView.setCheckedItem(id);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logOut() {
        try {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(getString(R.string.posts_for_subscription)).addOnCompleteListener(task -> Log.d("Unsubscription: ", "Done"));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        loginSpEditor.putBoolean(LOGIN_STATU, false);
        loginSpEditor.apply();
        startActivity(new Intent(HomePage.this, MainActivity.class));
        finish();
    }

    private void setAbout() {
        db.collection(getString(R.string.firestore_database_for_status)).document(String.valueOf(UserAdapter.getUserId())).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.get("status") != null) {
                String status = String.valueOf(documentSnapshot.get("status"));
                UserProfileAdapter.setStatus(status);
            } else {
                UserProfileAdapter.setStatus(getString(R.string.status_empty));
            }
        });
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
