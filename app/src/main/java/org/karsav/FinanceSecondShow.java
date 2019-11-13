package org.karsav;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mysql.cj.jdbc.Blob;
import com.r0adkll.slidr.Slidr;

import java.sql.SQLException;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class FinanceSecondShow extends AppCompatActivity {

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
    TextView responsibleTitle, descriptionTitle, receiptTitle;
    SharedPreferences settings;
    int textSize;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
                    image = karsav.financeLogImage(id);
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
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(FinanceSecondShow.this, connection, Toast.LENGTH_LONG).show();
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
        financeImage.setImageBitmap(btm);
        financeImage.setBackgroundColor(Color.WHITE);
    }
}
