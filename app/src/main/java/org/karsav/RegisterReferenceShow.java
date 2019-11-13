package org.karsav;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mysql.cj.jdbc.Blob;
import com.r0adkll.slidr.Slidr;

import java.sql.SQLException;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class RegisterReferenceShow extends AppCompatActivity {

    private static final String SETTINGS_PREF_NAME = "textSize";
    TextView name, phone, mail, institution, profession, reference_person, explanation;
    String idString;
    RegisterReferenceItem registerUserItem;
    DataBase karsav = new DataBase();
    ProgressDialog progressDialog;
    Toolbar toolbar;
    ImageView refImage;
    ProgressBar progressBar;
    TextView explanationTitle, institutionTitle, professionTitle, phoneNumberTitle, mailTitle, referenceTitle, photoTitle;
    SharedPreferences settings;
    int textSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reference_show);
        Slidr.attach(this);
        init();
        getImage();
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        explanationTitle = findViewById(R.id.explanationTitle);
        institutionTitle = findViewById(R.id.institutionTitle);
        professionTitle = findViewById(R.id.professionTitle);
        phoneNumberTitle = findViewById(R.id.phoneNumberTitle);
        mailTitle = findViewById(R.id.mailTitle);
        referenceTitle = findViewById(R.id.referenceTitle);
        photoTitle = findViewById(R.id.photoTitle);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        mail = findViewById(R.id.mail);
        institution = findViewById(R.id.institution);
        profession = findViewById(R.id.profession);
        reference_person = findViewById(R.id.reference_person);
        explanation = findViewById(R.id.explanation);
        toolbar = findViewById(R.id.toolbar);
        refImage = findViewById(R.id.refImage);
        progressBar = findViewById(R.id.progress_bar);

        setSupportActionBar(toolbar);
        View logoView = toolbar.getChildAt(1);
        logoView.setOnClickListener(v -> finish());

        registerUserItem = (RegisterReferenceItem) getIntent().getSerializableExtra("RegisterReferenceAddToDatabase");

        try {
            name.setText(registerUserItem.getName() + " " + registerUserItem.getSurname());
            phone.setText(registerUserItem.getPhone());
            mail.setText(registerUserItem.getMail());
            institution.setText(registerUserItem.getInstitution());
            profession.setText(registerUserItem.getProfession());
            reference_person.setText(registerUserItem.getReference());
            explanation.setText(registerUserItem.getExplanation());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        idString = registerUserItem.getId();

        settings = getSharedPreferences(SETTINGS_PREF_NAME, MODE_PRIVATE);
        textSize = settings.getInt(SETTINGS_PREF_NAME, 15);

        name.setTextSize(textSize + 15);
        phone.setTextSize(textSize);
        mail.setTextSize(textSize);
        institution.setTextSize(textSize);
        profession.setTextSize(textSize);
        reference_person.setTextSize(textSize);
        explanation.setTextSize(textSize);
        explanationTitle.setTextSize(textSize + 5);
        institutionTitle.setTextSize(textSize + 5);
        professionTitle.setTextSize(textSize + 5);
        phoneNumberTitle.setTextSize(textSize + 5);
        mailTitle.setTextSize(textSize + 5);
        referenceTitle.setTextSize(textSize + 5);
        photoTitle.setTextSize(textSize + 5);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update_delete, menu);
        if (UserAdapter.getRefKay() == null || !UserAdapter.getRefKay().equalsIgnoreCase("VAR")) {
            menu.findItem(R.id.update).setVisible(false);
            menu.findItem(R.id.delete).setVisible(false);
        } else {
            menu.findItem(R.id.update).setVisible(true);
            menu.findItem(R.id.delete).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.update:
                foo_update();
                break;

            case R.id.delete:
                String title = getString(R.string.reference_delete_dialog_title);
                String text = getString(R.string.reference_delete_dialog_message);
                String yes = getString(R.string.reference_yes);
                String no = getString(R.string.reference_no);
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterReferenceShow.this);
                builder.setTitle(title).setMessage(text).setPositiveButton(yes, (dialog, which) -> foo_delete()).setNegativeButton(no, (dialog, which) -> {

                }).show();
                break;
        }
        return true;
    }

    private void foo_update() {
        Intent intent = new Intent(RegisterReferenceShow.this, RegisterReferenceUpdate.class);
        intent.putExtra("UpdateReference", registerUserItem);
        startActivity(intent);
        finish();
    }

    @SuppressLint("StaticFieldLeak")
    private void foo_delete() {
        new AsyncTask<String, String, String>() {

            final String progress_title = getString(R.string.register_reference_delete_progress_title);
            final String progress_message = getString(R.string.register_reference_progress_message);
            final String connection = getString(R.string.mainactivity_connection);
            final String deleted = getString(R.string.reference_deleted);

            protected void onPreExecute() {
                progressDialog = new ProgressDialog(RegisterReferenceShow.this);
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
                    karsav.RegisterReferenceDeleteFromDatabase(idString);
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
                        Toast.makeText(RegisterReferenceShow.this, connection, Toast.LENGTH_SHORT).show();
                        break;

                    case "done":
                        startActivity(new Intent(RegisterReferenceShow.this, HomePage.class));
                        Toast.makeText(RegisterReferenceShow.this, deleted, Toast.LENGTH_LONG).show();
                        finish();
                        break;
                }
            }
        }.execute("delete");
    }

    @SuppressLint("StaticFieldLeak")
    private void getImage() {
        new AsyncTask<Boolean, Boolean, Boolean>() {

            final String connection = getString(R.string.mainactivity_connection);
            Blob image;

            @Override
            protected Boolean doInBackground(Boolean... booleans) {
                try {
                    if (karsav.Begin()) {
                        return false;
                    }
                    image = karsav.referenceImage(idString);
                    karsav.Disconnect();
                    return true;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                progressBar.setVisibility(View.GONE);
                if (aBoolean) {
                    try {
                        setImage(image);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(RegisterReferenceShow.this, connection, Toast.LENGTH_LONG).show();
                }
                super.onPostExecute(aBoolean);
            }
        }.execute(true);
    }

    @SuppressLint("ResourceAsColor")
    private void setImage(Blob image) throws SQLException {
        int blobLength = (int) image.length();
        byte[] blobAsBytes = image.getBytes(1, blobLength);
        Bitmap btm = BitmapFactory.decodeByteArray(blobAsBytes, 0, blobAsBytes.length);
        refImage.setImageBitmap(btm);
        refImage.setBackgroundColor(Color.WHITE);
    }
}
