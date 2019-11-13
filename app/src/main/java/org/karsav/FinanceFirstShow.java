package org.karsav;

import android.annotation.SuppressLint;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mysql.cj.jdbc.Blob;
import com.r0adkll.slidr.Slidr;

import java.sql.SQLException;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class FinanceFirstShow extends AppCompatActivity {

    private static final String SETTINGS_PREF_NAME = "textSize";
    TextView finance_name, finance_responsible, finance_description;
    FinanceItem financeItem;
    String name, responsible, description, id;
    LinearLayout linear_responsible, linear_description;
    Toolbar toolbar;
    DataBase karsav = new DataBase();
    ImageView financeImage;
    ProgressBar progressBar;
    ProgressDialog progressDialog;
    Bitmap btm;
    TextView responsibleTitle, descriptionTitle, receiptTitle;
    SharedPreferences settings;
    int textSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_first_show);
        Slidr.attach(this);
        init();
        getImage();
    }

    private void init() {
        finance_name = findViewById(R.id.finance_name);
        finance_responsible = findViewById(R.id.finance_responsible);
        finance_description = findViewById(R.id.finance_description);
        financeImage = findViewById(R.id.financeImage);
        progressBar = findViewById(R.id.progress_bar);
        toolbar = findViewById(R.id.toolbar);
        responsibleTitle = findViewById(R.id.responsibleTitle);
        descriptionTitle = findViewById(R.id.descriptionTitle);
        receiptTitle = findViewById(R.id.receiptTitle);

        setSupportActionBar(toolbar);
        View logoView = toolbar.getChildAt(1);
        logoView.setOnClickListener(v -> finish());

        settings = getSharedPreferences(SETTINGS_PREF_NAME, MODE_PRIVATE);
        textSize = settings.getInt(SETTINGS_PREF_NAME, 15);

        finance_name.setTextSize(textSize + 10);
        finance_responsible.setTextSize(textSize);
        finance_description.setTextSize(textSize);
        responsibleTitle.setTextSize(textSize + 5);
        descriptionTitle.setTextSize(textSize + 5);
        receiptTitle.setTextSize(textSize + 5);

        financeItem = (FinanceItem) getIntent().getSerializableExtra("Finance");

        name = financeItem.getName().trim();
        responsible = financeItem.getResponsible().trim();
        description = financeItem.getDescription().trim();
        id = financeItem.getId();

        if (responsible.isEmpty() || responsible.equalsIgnoreCase("null")) {
            linear_responsible.setVisibility(View.GONE);
        }
        if (description.isEmpty() || description.equalsIgnoreCase("null")) {
            linear_description.setVisibility(View.GONE);
        }

        finance_name.setText(name);
        finance_responsible.setText(responsible);
        finance_description.setText(description);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update_delete, menu);
        if (UserAdapter.getMaliyeKay() != null && UserAdapter.getMaliyeKay().equalsIgnoreCase("VAR")) {
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
                Intent intent = new Intent(FinanceFirstShow.this, FinanceUpdate.class);
                intent.putExtra("UpdateFinance", financeItem);
                startActivity(intent);
                finish();
                break;

            case R.id.delete:
                String title = getString(R.string.finance_delete_dialog_title);
                String text = getString(R.string.assignment_delete_dialog_message);
                String yes = getString(R.string.assignment_delete_yes);
                String no = getString(R.string.assignment_delete_no);
                AlertDialog.Builder builder = new AlertDialog.Builder(FinanceFirstShow.this);
                builder.setTitle(title).setMessage(text).setPositiveButton(yes, (dialog, which) -> foo_delete()).setNegativeButton(no, (dialog, which) -> {

                }).show();
                break;
        }
        return super.onOptionsItemSelected(item);
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
                    image = karsav.financeImage(id);
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
                    Toast.makeText(FinanceFirstShow.this, connection, Toast.LENGTH_LONG).show();
                }
                super.onPostExecute(aBoolean);
            }
        }.execute(true);
    }

    @SuppressLint("ResourceAsColor")
    private void setImage(Blob image) throws SQLException {
        int blobLength = (int) image.length();
        byte[] blobAsBytes = image.getBytes(1, blobLength);
        btm = BitmapFactory.decodeByteArray(blobAsBytes, 0, blobAsBytes.length);
        financeImage.setImageBitmap(btm);
        financeImage.setBackgroundColor(Color.WHITE);
    }


    @SuppressLint("StaticFieldLeak")
    private void foo_delete() {
        new AsyncTask<String, String, String>() {

            final String progress_title = getString(R.string.assignment_delete_progress_title);
            final String progress_message = getString(R.string.assignment_progress_message);
            final String connection = getString(R.string.mainactivity_connection);
            final String deleted = getString(R.string.finance_deleted);

            protected void onPreExecute() {
                progressDialog = new ProgressDialog(FinanceFirstShow.this);
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
                    karsav.FinanceLogDeleteFromDatabase(id);
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
                        Toast.makeText(FinanceFirstShow.this, connection, Toast.LENGTH_SHORT).show();
                        break;

                    case "done":
                        startActivity(new Intent(FinanceFirstShow.this, HomePage.class));
                        Toast.makeText(FinanceFirstShow.this, deleted, Toast.LENGTH_LONG).show();
                        finish();
                        break;
                }
            }
        }.execute("delete");
    }
}
