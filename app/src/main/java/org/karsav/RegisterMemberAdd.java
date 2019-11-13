package org.karsav;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.r0adkll.slidr.Slidr;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class RegisterMemberAdd extends AppCompatActivity {

    private static final String SETTINGS_PREF_NAME = "textSize";
    EditText name, surname, department, phone, mail, studentNo, explanation;
    CheckBox aviation_check, ground_check, marine_check, cyber_check;
    String nameString, surnameString, phoneString, mailString, departmentString, studentNoString, explanationString;
    String aviationString, groundString, marineString, cyberString;
    DataBase karsav = new DataBase();
    ProgressDialog progressDialog;
    Switch official, kayit_go, proje_go, top_go, gorev_go, uye_go, uye_kay, grv_go, grv_kay, ref_go, ref_kay, tum_proje_go, proje_kay;
    Switch tum_top_go, top_kay, tum_gorev_go, gorev_kay, maliye_go, maliye_kay;
    String kayit_goString, proje_goString, top_goString, gorev_goString, uye_goString, user_idString, passwordString;
    String uye_kayString, grv_goString, grv_kayString, ref_goString, ref_kayString, tum_proje_goString, proje_kayString;
    String tum_top_goString, top_kayString, tum_gorev_goString, gorev_kayString, maliye_goString, maliye_kayString, positionString;
    EditText position, userId, password;
    LinearLayout linearLayout;
    Toolbar toolbar;
    TextView interests, aviation, ground, marine, cyber, information, registers, displays;
    SharedPreferences settings;
    int textSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_member_add);
        Slidr.attach(this);
        init();
    }

    private void init() {
        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        department = findViewById(R.id.department);
        studentNo = findViewById(R.id.student_no);
        phone = findViewById(R.id.phone);
        mail = findViewById(R.id.mail);
        explanation = findViewById(R.id.explanation);

        official = findViewById(R.id.official);
        kayit_go = findViewById(R.id.kayit_go);
        proje_go = findViewById(R.id.proje_go);
        top_go = findViewById(R.id.top_go);
        gorev_go = findViewById(R.id.gorev_go);
        uye_go = findViewById(R.id.uye_go);
        uye_kay = findViewById(R.id.uye_kay);
        grv_go = findViewById(R.id.grv_go);
        grv_kay = findViewById(R.id.grv_kay);
        ref_go = findViewById(R.id.ref_go);
        ref_kay = findViewById(R.id.ref_kay);
        tum_proje_go = findViewById(R.id.tum_proje_go);
        proje_kay = findViewById(R.id.proje_kay);
        tum_top_go = findViewById(R.id.tum_top_go);
        top_kay = findViewById(R.id.top_kay);
        tum_gorev_go = findViewById(R.id.tum_gorev_go);
        gorev_kay = findViewById(R.id.gorev_kay);
        maliye_go = findViewById(R.id.maliye_go);
        maliye_kay = findViewById(R.id.maliye_kay);
        displays = findViewById(R.id.displays);

        position = findViewById(R.id.position);
        userId = findViewById(R.id.userid);
        password = findViewById(R.id.password);

        interests = findViewById(R.id.interests);
        aviation = findViewById(R.id.aviation);
        ground = findViewById(R.id.ground);
        marine = findViewById(R.id.marine);
        cyber = findViewById(R.id.cyber);
        information = findViewById(R.id.information);
        registers = findViewById(R.id.registers);

        aviation_check = findViewById(R.id.aviation_check);
        ground_check = findViewById(R.id.ground_check);
        marine_check = findViewById(R.id.marine_check);
        cyber_check = findViewById(R.id.cyber_check);

        linearLayout = findViewById(R.id.linear_official);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        View logoView = toolbar.getChildAt(1);
        logoView.setOnClickListener(v -> finish());

        settings = getSharedPreferences(SETTINGS_PREF_NAME, MODE_PRIVATE);
        textSize = settings.getInt(SETTINGS_PREF_NAME, 15);

        name.setTextSize(textSize);
        surname.setTextSize(textSize);
        department.setTextSize(textSize);
        studentNo.setTextSize(textSize);
        phone.setTextSize(textSize);
        mail.setTextSize(textSize);
        explanation.setTextSize(textSize);
        official.setTextSize(textSize);
        kayit_go.setTextSize(textSize);
        proje_go.setTextSize(textSize);
        gorev_go.setTextSize(textSize);
        uye_go.setTextSize(textSize);
        uye_kay.setTextSize(textSize);
        grv_go.setTextSize(textSize);
        grv_kay.setTextSize(textSize);
        ref_go.setTextSize(textSize);
        ref_kay.setTextSize(textSize);
        tum_proje_go.setTextSize(textSize);
        proje_kay.setTextSize(textSize);
        tum_top_go.setTextSize(textSize);
        top_go.setTextSize(textSize);
        top_kay.setTextSize(textSize);
        tum_gorev_go.setTextSize(textSize);
        gorev_kay.setTextSize(textSize);
        maliye_go.setTextSize(textSize);
        maliye_kay.setTextSize(textSize);
        position.setTextSize(textSize);
        userId.setTextSize(textSize);
        password.setTextSize(textSize);
        interests.setTextSize(textSize + 5);
        aviation.setTextSize(textSize);
        ground.setTextSize(textSize);
        marine.setTextSize(textSize);
        cyber.setTextSize(textSize);
        information.setTextSize(textSize + 5);
        registers.setTextSize(textSize + 5);
        aviation_check.setTextSize(textSize);
        ground_check.setTextSize(textSize);
        marine_check.setTextSize(textSize);
        cyber_check.setTextSize(textSize);
        displays.setTextSize(textSize + 5);

        official.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (official.isChecked()) {
                linearLayout.setVisibility(View.VISIBLE);
                position.setText(getString(R.string.member));
                proje_go.setChecked(true);
                top_go.setChecked(true);
                gorev_go.setChecked(true);
                uye_go.setChecked(true);
                grv_go.setChecked(true);
                tum_proje_go.setChecked(true);
                tum_top_go.setChecked(true);
                tum_gorev_go.setChecked(true);
            } else
                linearLayout.setVisibility(View.GONE);
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(Color.WHITE);
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
            done();
        }
        return super.onOptionsItemSelected(item);
    }

    private void done() {
        nameString = name.getText().toString();
        surnameString = surname.getText().toString();
        departmentString = department.getText().toString();
        phoneString = phone.getText().toString();
        mailString = mail.getText().toString();
        studentNoString = studentNo.getText().toString();
        explanationString = explanation.getText().toString();

        maliye_kayString = "";
        maliye_goString = "";
        gorev_kayString = "";
        tum_gorev_goString = "";
        top_kayString = "";
        tum_top_goString = "";
        proje_kayString = "";
        tum_proje_goString = "";
        ref_kayString = "";
        ref_goString = "";
        grv_kayString = "";
        grv_goString = "";
        uye_kayString = "";
        uye_goString = "";
        gorev_goString = "";
        top_goString = "";
        proje_goString = "";
        kayit_goString = "";
        aviationString = "";
        groundString = "";
        marineString = "";
        cyberString = "";

        if (aviation_check.isChecked())
            aviationString = "var";

        if (ground_check.isChecked())
            groundString = "var";

        if (marine_check.isChecked())
            marineString = "var";

        if (cyber_check.isChecked())
            cyberString = "var";

        positionString = position.getText().toString();
        user_idString = userId.getText().toString();
        passwordString = password.getText().toString();

        if (kayit_go.isChecked())
            kayit_goString = "var";

        if (proje_go.isChecked())
            proje_goString = "var";

        if (top_go.isChecked())
            top_goString = "var";

        if (gorev_go.isChecked())
            gorev_goString = "var";

        if (uye_go.isChecked())
            uye_goString = "var";

        if (uye_kay.isChecked())
            uye_kayString = "var";

        if (grv_go.isChecked())
            grv_goString = "var";

        if (grv_kay.isChecked())
            grv_kayString = "var";

        if (ref_go.isChecked())
            ref_goString = "var";

        if (ref_kay.isChecked())
            ref_kayString = "var";

        if (tum_proje_go.isChecked())
            tum_proje_goString = "var";

        if (proje_kay.isChecked())
            proje_kayString = "var";

        if (tum_top_go.isChecked())
            tum_top_goString = "var";

        if (top_kay.isChecked())
            top_kayString = "var";

        if (tum_gorev_go.isChecked())
            tum_gorev_goString = "var";

        if (gorev_kay.isChecked())
            gorev_kayString = "var";

        if (maliye_go.isChecked())
            maliye_goString = "var";

        if (maliye_kay.isChecked())
            maliye_kayString = "var";

        if (nameString.equalsIgnoreCase("null") || nameString.isEmpty() || surnameString.equalsIgnoreCase("null") || surnameString.isEmpty()) {
            Toast.makeText(RegisterMemberAdd.this, getString(R.string.name_surname_empty), Toast.LENGTH_SHORT).show();
        } else {
            String text = getString(R.string.register_member_add_text);
            String yes = getString(R.string.register_member_add_yes);
            String no = getString(R.string.register_member_add_no);
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterMemberAdd.this);
            builder.setMessage(text).setPositiveButton(yes, (dialog, which) -> addUser()).setNegativeButton(no, (dialog, which) -> {

            }).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void addUser() {
        new AsyncTask<String, String, String>() {

            final String progress_title = getString(R.string.register_reference_progress_title);
            final String progress_message = getString(R.string.register_reference_progress_message);
            final String connection = getString(R.string.mainactivity_connection);
            final String added = getString(R.string.member_added);

            protected void onPreExecute() {
                progressDialog = new ProgressDialog(RegisterMemberAdd.this);
                progressDialog.setTitle(progress_title);
                progressDialog.setMessage(progress_message);
                progressDialog.show();
            }

            @Override
            protected String doInBackground(String... strings) {
                try {
                    if (karsav.Begin()) {
                        return "wrong";
                    }
                    karsav.RegisterMemberAddToDatabase(nameString, surnameString, phoneString, mailString, departmentString,
                            studentNoString, explanationString, aviationString, groundString, marineString, cyberString);
                    if (official.isChecked()) {
                        karsav.RegisterOfficialAddToDatabase(nameString, surnameString, kayit_goString, proje_goString, top_goString, gorev_goString, uye_goString, user_idString, passwordString,
                                uye_kayString, grv_goString, grv_kayString, ref_goString, ref_kayString, tum_proje_goString, proje_kayString,
                                tum_top_goString, top_kayString, tum_gorev_goString, gorev_kayString, maliye_goString, maliye_kayString, positionString);
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
                        Toast.makeText(RegisterMemberAdd.this, connection, Toast.LENGTH_SHORT).show();
                        break;

                    case "done":
                        startActivity(new Intent(RegisterMemberAdd.this, HomePage.class));
                        Toast.makeText(getApplicationContext(), added, Toast.LENGTH_LONG).show();
                        finish();
                        break;
                }
            }
        }.execute("addMember");
    }
}
