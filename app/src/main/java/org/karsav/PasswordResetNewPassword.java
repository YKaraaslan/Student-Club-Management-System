package org.karsav;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.r0adkll.slidr.Slidr;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class PasswordResetNewPassword extends AppCompatActivity {

    private static final String SETTINGS_PREF_NAME = "textSize";
    Toolbar toolbar;
    TextInputEditText userId, password, passwordCheck;
    Button checkButton;
    String userIdString, passwordString, passwordCheckString, name;
    ProgressDialog progressDialog;
    String userIdReceived;
    DataBase karsav = new DataBase();
    FirebaseAuth firebaseAuth;
    TextView karsavTextView;
    SharedPreferences settings;
    int textSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset_new_password);
        Slidr.attach(this);
        init();
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        userId = findViewById(R.id.userId);
        password = findViewById(R.id.password);
        passwordCheck = findViewById(R.id.passwordCheck);
        checkButton = findViewById(R.id.change);
        karsavTextView = findViewById(R.id.karsav);

        setSupportActionBar(toolbar);
        View logoView = toolbar.getChildAt(1);
        logoView.setOnClickListener(v -> finish());

        settings = getSharedPreferences(SETTINGS_PREF_NAME, MODE_PRIVATE);
        textSize = settings.getInt(SETTINGS_PREF_NAME, 15);

        password.setTextSize(textSize);
        passwordCheck.setTextSize(textSize);
        userId.setTextSize(textSize);
        checkButton.setTextSize(textSize);
        karsavTextView.setTextSize(textSize + 25);

        firebaseAuth = FirebaseAuth.getInstance();

        checkButton.setOnClickListener(v -> {
            passwordCheckString = String.valueOf(passwordCheck.getText());
            passwordString = String.valueOf(password.getText());
            userIdString = String.valueOf(userId.getText());

            if (passwordCheckString.isEmpty() || passwordString.isEmpty() || userIdString.isEmpty()) {
                Toast.makeText(getApplicationContext(), getString(R.string.sign_up_blank), Toast.LENGTH_SHORT).show();
            } else if (!passwordString.equalsIgnoreCase(passwordCheckString)) {
                Toast.makeText(getApplicationContext(), getString(R.string.passwords_are_different), Toast.LENGTH_SHORT).show();
            } else {
                String text = getString(R.string.password_reset_dialog_text);
                String yes = getString(R.string.yes);
                String no = getString(R.string.cancel);

                AlertDialog.Builder builder = new AlertDialog.Builder(PasswordResetNewPassword.this);
                builder.setMessage(text).setPositiveButton(yes, (dialog, which) -> passwordRenewal()).setNegativeButton(no, (dialog, which) -> {
                }).show();
            }
        });
        userIdReceived = (String) getIntent().getSerializableExtra("id");
        userId.setText(userIdReceived);
    }

    @SuppressLint("StaticFieldLeak")
    private void passwordRenewal() {
        new AsyncTask<String, String, String>() {

            final String progress_title = getString(R.string.checking);
            final String progress_message = getString(R.string.wait);
            final String connection = getString(R.string.mainactivity_connection);
            final String successful = getString(R.string.password_successfully_changed);

            protected void onPreExecute() {
                progressDialog = new ProgressDialog(PasswordResetNewPassword.this);
                progressDialog.setTitle(progress_title);
                progressDialog.setMessage(progress_message);
                progressDialog.show();
            }

            @Override
            protected String doInBackground(String... strings) {
                try {
                    if (karsav.Begin()) {
                        return "false";
                    }
                    int id = UserAdapterAnonymous.getId();
                    boolean checked = karsav.changePassword(id, userIdString, passwordString);
                    if (checked) {
                        karsav.Disconnect();
                        return "done";
                    }
                    return "false";
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return "false";
                }
            }

            @Override
            protected void onPostExecute(String s) {
                progressDialog.dismiss();
                super.onPostExecute(s);
                switch (s) {
                    case "false":
                        Toast.makeText(PasswordResetNewPassword.this, connection, Toast.LENGTH_SHORT).show();
                        break;

                    case "done":
                        Toast.makeText(getApplicationContext(), successful, Toast.LENGTH_LONG).show();
                        finish();
                        break;
                }
            }
        }.execute("passwordResetNewPassword");
    }
}
