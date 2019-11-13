package org.karsav;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.r0adkll.slidr.Slidr;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class RegisterOfficialShow extends AppCompatActivity {

    private static final String SETTINGS_PREF_NAME = "textSize";
    CircleImageView karsavImage;
    TextView name, department, student_no, phone, mail, explanation, aviation, ground, marine, cyber;
    ImageView aviation_check, ground_check, marine_check, cyber_check;
    RegisterOfficialItem registerUserItem;
    ProgressDialog progressDialog;
    String names, surnames, departments, student_nos, phones, mails, explanations;
    String aviation_checks, ground_checks, marine_checks, cyber_checks;
    DataBase karsav = new DataBase();
    Toolbar toolbar;
    int id;
    SharedPreferences settings;
    int textSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_show);
        Slidr.attach(this);
        init();
    }

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    private void init() {
        karsavImage = findViewById(R.id.karsav);
        name = findViewById(R.id.nameSurname);
        department = findViewById(R.id.department);
        student_no = findViewById(R.id.student_no);
        phone = findViewById(R.id.phone);
        mail = findViewById(R.id.mail);
        explanation = findViewById(R.id.explanation);
        aviation_check = findViewById(R.id.aviation_check);
        ground_check = findViewById(R.id.ground_check);
        marine_check = findViewById(R.id.marine_check);
        cyber_check = findViewById(R.id.cyber_check);
        toolbar = findViewById(R.id.toolbar);
        aviation = findViewById(R.id.aviation);
        ground = findViewById(R.id.ground);
        marine = findViewById(R.id.marine);
        cyber = findViewById(R.id.cyber);

        setSupportActionBar(toolbar);
        View logoView = toolbar.getChildAt(1);
        logoView.setOnClickListener(v -> finish());

        registerUserItem = (RegisterOfficialItem) getIntent().getSerializableExtra("RegisterOfficial");

        try {
            names = registerUserItem.getName().trim();
            surnames = registerUserItem.getSurname().trim();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        settings = getSharedPreferences(SETTINGS_PREF_NAME, MODE_PRIVATE);
        textSize = settings.getInt(SETTINGS_PREF_NAME, 15);

        name.setTextSize(textSize);
        department.setTextSize(textSize);
        student_no.setTextSize(textSize);
        phone.setTextSize(textSize);
        mail.setTextSize(textSize);
        explanation.setTextSize(textSize);
        aviation.setTextSize(textSize);
        ground.setTextSize(textSize);
        marine.setTextSize(textSize);
        cyber.setTextSize(textSize);

        id = registerUserItem.getId();

        departments = (String) RegisterOfficialGet.getDepartment().get(id);
        student_nos = (String) RegisterOfficialGet.getStudentNo().get(id);
        phones = (String) RegisterOfficialGet.getPhone().get(id);
        mails = (String) RegisterOfficialGet.getMail().get(id);
        explanations = (String) RegisterOfficialGet.getPosition().get(id);
        aviation_checks = (String) RegisterOfficialGet.getAviation().get(id);
        ground_checks = (String) RegisterOfficialGet.getGround().get(id);
        marine_checks = (String) RegisterOfficialGet.getMarine().get(id);
        cyber_checks = (String) RegisterOfficialGet.getCyber().get(id);

        try {
            name.setText(names + " " + surnames);
            department.setText(explanations.trim());
            student_no.setText(student_nos.trim());
            phone.setText(phones.trim());
            mail.setText(mails.trim());
            explanation.setText(departments.trim());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            if (aviation_checks != null && aviation_checks.equalsIgnoreCase("VAR")) {
                aviation_check.setImageResource(R.drawable.check);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            if (ground_checks != null && ground_checks.equalsIgnoreCase("VAR"))
                ground_check.setImageResource(R.drawable.check);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            if (marine_checks != null && marine_checks.equalsIgnoreCase("VAR"))
                marine_check.setImageResource(R.drawable.check);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            if (cyber_checks != null && cyber_checks.equalsIgnoreCase("VAR"))
                cyber_check.setImageResource(R.drawable.check);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update_delete, menu);
        if (UserAdapter.getUyeKay() != null && UserAdapter.getUyeKay().equalsIgnoreCase("VAR")) {
            menu.findItem(R.id.update).setVisible(true);
            menu.findItem(R.id.delete).setVisible(true);
        } else {
            menu.findItem(R.id.update).setVisible(false);
            menu.findItem(R.id.delete).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.update:
                Intent intent = new Intent(RegisterOfficialShow.this, RegisterOfficialUpdate.class);
                intent.putExtra("UpdateOfficial", registerUserItem);
                startActivity(intent);
                finish();
                break;

            case R.id.delete:
                String title = getString(R.string.register_member_delete_title);
                String text = getString(R.string.register_member_delete_text_second);
                String yes = getString(R.string.reference_yes);
                String no = getString(R.string.reference_no);
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterOfficialShow.this);
                builder.setTitle(title).setMessage(text).setPositiveButton(yes, (dialog, which) -> foo_delete()).setNegativeButton(no, (dialog, which) -> {

                }).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("StaticFieldLeak")
    private void foo_delete() {
        new AsyncTask<String, String, String>() {

            final String progress_title = getString(R.string.register_reference_delete_progress_title);
            final String progress_message = getString(R.string.register_reference_progress_message);
            final String connection = getString(R.string.mainactivity_connection);
            final String deleted = getString(R.string.official_deleted);

            protected void onPreExecute() {
                progressDialog = new ProgressDialog(RegisterOfficialShow.this);
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
                    karsav.RegisterOfficialDeleteFromDatabase(String.valueOf(id));
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
                        Toast.makeText(RegisterOfficialShow.this, connection, Toast.LENGTH_SHORT).show();
                        break;

                    case "done":
                        startActivity(new Intent(RegisterOfficialShow.this, HomePage.class));
                        Toast.makeText(RegisterOfficialShow.this, deleted, Toast.LENGTH_LONG).show();
                        finish();
                        break;
                }
            }
        }.execute("delete");
    }
}
