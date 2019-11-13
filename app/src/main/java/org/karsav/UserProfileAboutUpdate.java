package org.karsav;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.r0adkll.slidr.Slidr;

import java.util.HashMap;
import java.util.Objects;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class UserProfileAboutUpdate extends AppCompatActivity implements TextWatcher {
    private static final String SETTINGS_PREF_NAME = "textSize";
    Toolbar toolbar;
    String aboutString;
    int counter, maxCharacter = 500, sumCharacter;
    TextInputEditText text;
    TextView number;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    int userId;
    SharedPreferences settings;
    int textSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_about_update);
        Slidr.attach(this);
        init();
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        text = findViewById(R.id.text);
        number = findViewById(R.id.number);

        text.addTextChangedListener(this);
        number.setText(String.valueOf(maxCharacter));

        setSupportActionBar(toolbar);
        View logoView = toolbar.getChildAt(1);
        logoView.setOnClickListener(v -> finish());

        settings = getSharedPreferences(SETTINGS_PREF_NAME, MODE_PRIVATE);
        textSize = settings.getInt(SETTINGS_PREF_NAME, 15);

        aboutString = getIntent().getStringExtra("about");
        userId = getIntent().getIntExtra("id", 0);

        if (aboutString.equalsIgnoreCase(getString(R.string.status_empty))) {
            aboutString = "";
            text.setHint(getString(R.string.about));
        } else {
            text.setText(aboutString);
            text.setTextSize(textSize);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.done) {
            foo_update();
        }
        return super.onOptionsItemSelected(item);
    }

    private void foo_update() {
        HashMap<String, String> status = new HashMap<>();
        String getText = Objects.requireNonNull(text.getText()).toString().trim();

        status.put("status", getText);

        if (!getText.isEmpty()) {
            db.collection(getString(R.string.firestore_database_for_status)).document(String.valueOf(userId)).set(status);
            UserProfileAdapter.setStatus(getText);
            finish();
            Toast.makeText(getApplicationContext(), getString(R.string.updated), Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getApplicationContext(), getString(R.string.status_left_empty), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        counter = Objects.requireNonNull(text.getText()).length();
        sumCharacter = maxCharacter - counter;
        number.setText(String.valueOf(sumCharacter));
        if (sumCharacter < 51) {
            number.setTextColor(Color.RED);
        } else
            number.setTextColor(Color.GRAY);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
