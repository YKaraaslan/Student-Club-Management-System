package org.karsav;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.r0adkll.slidr.Slidr;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class PasswordReset extends AppCompatActivity {

    private static final String SETTINGS_PREF_NAME = "textSize";
    Toolbar toolbar;
    AutoCompleteTextView nameSurname;
    EditText mail, phone, userId, studentNo;
    Button checkButton;
    String nameString, mailString, userIdString, studentNoString, phoneString;
    ProgressDialog progressDialog;
    DataBase karsav = new DataBase();
    TextView contact, karsavTextView;
    SharedPreferences settings;
    int textSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        Slidr.attach(this);
        init();
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        nameSurname = findViewById(R.id.nameSurname);
        mail = findViewById(R.id.mail);
        phone = findViewById(R.id.phone_number);
        userId = findViewById(R.id.userId);
        studentNo = findViewById(R.id.student_no);
        checkButton = findViewById(R.id.check);
        contact = findViewById(R.id.contact_us);
        karsavTextView = findViewById(R.id.karsav);

        setSupportActionBar(toolbar);
        View logoView = toolbar.getChildAt(1);
        logoView.setOnClickListener(v -> finish());
        // TODO: 09/11/2019 registermemberget icin veritabanindan cekim yap.
        /*Map map = RegisterMemberGet.getFullName();
        Collection<String> values = map.values();
        ArrayList<String> listOfValues = new ArrayList<>(values);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.list_item_for_users, R.id.users_list_item, listOfValues);
        nameSurname.setAdapter(adapter);*/

        settings = getSharedPreferences(SETTINGS_PREF_NAME, MODE_PRIVATE);
        textSize = settings.getInt(SETTINGS_PREF_NAME, 15);

        nameSurname.setTextSize(textSize);
        mail.setTextSize(textSize);
        phone.setTextSize(textSize);
        userId.setTextSize(textSize);
        studentNo.setTextSize(textSize);
        checkButton.setTextSize(textSize);
        contact.setTextSize(textSize);
        karsavTextView.setTextSize(textSize + 25);

        checkButton.setOnClickListener(v -> {
            nameString = String.valueOf(nameSurname.getText());
            mailString = String.valueOf(mail.getText());
            phoneString = String.valueOf(phone.getText());
            studentNoString = String.valueOf(studentNo.getText());
            userIdString = String.valueOf(userId.getText());

            if (nameString.isEmpty() || mailString.isEmpty() || phoneString.isEmpty() ||
                    studentNoString.isEmpty() || userIdString.isEmpty()) {
                Toast.makeText(getApplicationContext(), getString(R.string.sign_up_blank), Toast.LENGTH_SHORT).show();
            } else {
                passwordRenewal();
            }
        });
        contact.setOnClickListener(v -> contactUs());
    }

    private void contactUs() {
        String mailText = getString(R.string.mail_text, nameString, mailString, phoneString, studentNoString, userIdString);
        Intent intent = new Intent(Intent.ACTION_SEND);
        String[] developers = getString(R.string.developer_mail).split(" ");
        intent.putExtra(Intent.EXTRA_EMAIL, developers);
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_subject));
        intent.putExtra(Intent.EXTRA_TEXT, mailText);
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, getString(R.string.choose_email)));
    }

    @SuppressLint("StaticFieldLeak")
    private void passwordRenewal() {
        new AsyncTask<String, String, String>() {

            final String progress_title = getString(R.string.checking);
            final String progress_message = getString(R.string.wait);
            final String connection = getString(R.string.mainactivity_connection);
            final String succesfull = getString(R.string.successful);
            final String wrong = getString(R.string.wrong_information);

            protected void onPreExecute() {
                progressDialog = new ProgressDialog(PasswordReset.this);
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
                    String[] nameSurnameList = nameString.split((" "), 3);
                    String name = null, surname = null;
                    if (nameSurnameList.length == 2) {
                        name = nameSurnameList[0].trim();
                        surname = nameSurnameList[1].trim();
                    } else if (nameSurnameList.length == 3) {
                        name = nameSurnameList[0].trim() + " " + nameSurnameList[1].trim();
                        surname = nameSurnameList[2].trim();
                    }
                    boolean checked = karsav.checkPersonFromDatabase(name, surname, mailString, phoneString, studentNoString, userIdString);
                    if (checked) {
                        karsav.Disconnect();
                        return "done";
                    } else {
                        return "wrong";
                    }
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
                        Toast.makeText(PasswordReset.this, connection, Toast.LENGTH_SHORT).show();
                        break;

                    case "wrong":
                        Toast.makeText(PasswordReset.this, wrong, Toast.LENGTH_SHORT).show();
                        contact.setVisibility(View.VISIBLE);
                        break;

                    case "done":
                        Intent intent = new Intent(PasswordReset.this, PasswordResetNewPassword.class);
                        intent.putExtra("id", userIdString);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), succesfull, Toast.LENGTH_LONG).show();
                        finish();
                        break;
                }
            }
        }.execute("passwordReset");
    }
}