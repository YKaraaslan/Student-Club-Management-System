package org.karsav;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.r0adkll.slidr.Slidr;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import static org.karsav.FinanceAdd.PICK_IMAGE;
import static org.karsav.FinanceAdd.TAKE_PICTURE;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class RegisterReferenceAdd extends AppCompatActivity {

    private static final String SETTINGS_PREF_NAME = "textSize";
    EditText name, surname, phone, mail, institution, profession, explanation;
    AutoCompleteTextView reference_person;
    String nameString, surnameString, phoneString, mailString, institutionString, professionString, reference_personString, explanationString;
    DataBase karsav = new DataBase();
    ProgressDialog progressDialog;
    Toolbar toolbar;
    ImageView imageView;
    Button openCamera, openGallery;
    Bitmap imageBitmap, imageBitmapTwo;
    SharedPreferences settings;
    int textSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_reference);
        Slidr.attach(this);
        init();
    }

    private void init() {

        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        phone = findViewById(R.id.phone);
        mail = findViewById(R.id.mail);
        institution = findViewById(R.id.institution);
        profession = findViewById(R.id.profession);
        reference_person = findViewById(R.id.reference_person);
        explanation = findViewById(R.id.explanation);
        toolbar = findViewById(R.id.toolbar);
        imageView = findViewById(R.id.image);
        openCamera = findViewById(R.id.open_camera);
        openGallery = findViewById(R.id.open_gallery);

        setSupportActionBar(toolbar);
        View logoView = toolbar.getChildAt(1);
        logoView.setOnClickListener(v -> finish());

        settings = getSharedPreferences(SETTINGS_PREF_NAME, MODE_PRIVATE);
        textSize = settings.getInt(SETTINGS_PREF_NAME, 15);

        name.setTextSize(textSize);
        surname.setTextSize(textSize);
        phone.setTextSize(textSize);
        mail.setTextSize(textSize);
        institution.setTextSize(textSize);
        profession.setTextSize(textSize);
        reference_person.setTextSize(textSize);
        explanation.setTextSize(textSize);
        openCamera.setTextSize(textSize);
        openGallery.setTextSize(textSize);

        Map map = RegisterMemberGet.getFullName();
        Collection<String> values = map.values();
        ArrayList<String> listOfValues = new ArrayList<>(values);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.list_item_for_users, R.id.users_list_item, listOfValues);
        reference_person.setAdapter(adapter);

        openCamera.setOnClickListener(v -> openCamera());

        openGallery.setOnClickListener(v -> openGallery());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(Color.WHITE);
        }

        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
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

    private void openCamera() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, TAKE_PICTURE);
        }
    }

    @SuppressLint("IntentReset")
    private void openGallery() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, getString(R.string.select_image));
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PICTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) Objects.requireNonNull(extras).get("data");
            imageView.setImageBitmap(imageBitmap);
            imageView.setVisibility(View.VISIBLE);
            imageView.setBackgroundColor(Color.WHITE);
        }

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            if (data == null) {
                return;
            }
            InputStream inputStream = null;
            try {
                inputStream = getApplicationContext().getContentResolver().openInputStream(Objects.requireNonNull(data.getData()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            imageBitmap = BitmapFactory.decodeStream(bufferedInputStream);
            imageView.setImageBitmap(imageBitmap);
            imageView.setVisibility(View.VISIBLE);
        }
        try {
            imageBitmapTwo = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            return stream.toByteArray();
        }
        return null;
    }

    private void done() {
        nameString = name.getText().toString();
        surnameString = surname.getText().toString();
        phoneString = phone.getText().toString();
        mailString = mail.getText().toString();
        institutionString = institution.getText().toString();
        professionString = profession.getText().toString();
        reference_personString = reference_person.getText().toString();
        explanationString = explanation.getText().toString();

        if (nameString.equalsIgnoreCase("null") || nameString.isEmpty() || surnameString.equalsIgnoreCase("null") || surnameString.isEmpty()) {
            Toast.makeText(RegisterReferenceAdd.this, getString(R.string.name_surname_empty), Toast.LENGTH_SHORT).show();
        } else {
            String text = getString(R.string.reference_add_dialog_text);
            String yes = getString(R.string.reference_add_yes);
            String no = getString(R.string.reference_add_no);
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterReferenceAdd.this);
            builder.setMessage(text).setPositiveButton(yes, (dialog, which) -> register()).setNegativeButton(no, (dialog, which) -> {

            }).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void register() {
        new AsyncTask<String, String, String>() {

            final String progress_title = getString(R.string.register_reference_progress_title);
            final String progress_message = getString(R.string.register_reference_progress_message);
            final String connection = getString(R.string.mainactivity_connection);
            final String added = getString(R.string.reference_added);

            protected void onPreExecute() {
                progressDialog = new ProgressDialog(RegisterReferenceAdd.this);
                progressDialog.setTitle(progress_title);
                progressDialog.setMessage(progress_message);
                progressDialog.show();
            }

            @Override
            protected String doInBackground(String... strings) {
                try {
                    if (imageBitmapTwo == null) {
                        return "noPhoto";
                    } else if (karsav.Begin()) {
                        return "wrong";
                    }
                    byte[] blob = getBytesFromBitmap(imageBitmapTwo);
                    karsav.RegisterReferenceAddToDatabase(nameString, surnameString, phoneString, mailString, institutionString, professionString, reference_personString, explanationString, blob);
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
                        Toast.makeText(RegisterReferenceAdd.this, connection, Toast.LENGTH_SHORT).show();
                        break;

                    case "done":
                        startActivity(new Intent(RegisterReferenceAdd.this, HomePage.class));
                        Toast.makeText(getApplicationContext(), added, Toast.LENGTH_LONG).show();
                        finish();
                        break;
                }
            }
        }.execute("register");
    }
}