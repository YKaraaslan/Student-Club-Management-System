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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.r0adkll.slidr.Slidr;
import com.yalantis.ucrop.UCrop;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

import static org.karsav.FinanceAdd.PICK_IMAGE;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class SignUp extends AppCompatActivity {

    private static final String SETTINGS_PREF_NAME = "textSize";
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    Uri filePath;
    EditText name, surname, studentNo, department, mail, phone;
    CheckBox aviation, ground, marine, cyber;
    Button signUp;
    ProgressDialog progressDialog;
    TextInputEditText user, password;
    Toolbar toolbar;
    TextView information, interests;
    DataBase karsav = new DataBase();
    String nameString, surnameString, studentNoString, departmentString, mailString, phoneString;
    String aviationString, groundString, marineString, cyberString;
    String userString, passwordString, explanationString;
    ImageView imageView;
    Button openGallery;
    Bitmap imageBitmap;
    Uri imageUriResultCrop;
    SharedPreferences settings;
    int textSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Slidr.attach(this);
        init();
    }

    private void init() {
        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        studentNo = findViewById(R.id.student_no);
        department = findViewById(R.id.department);
        mail = findViewById(R.id.mail);
        phone = findViewById(R.id.phone_number);
        user = findViewById(R.id.user);
        password = findViewById(R.id.password);
        information = findViewById(R.id.information);
        interests = findViewById(R.id.interests);

        imageView = findViewById(R.id.image);
        openGallery = findViewById(R.id.open_gallery);

        aviation = findViewById(R.id.aviation_check);
        ground = findViewById(R.id.ground_check);
        marine = findViewById(R.id.marine_check);
        cyber = findViewById(R.id.cyber_check);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        View logoView = toolbar.getChildAt(1);
        logoView.setOnClickListener(v -> finish());

        signUp = findViewById(R.id.sign_up);

        settings = getSharedPreferences(SETTINGS_PREF_NAME, MODE_PRIVATE);
        textSize = settings.getInt(SETTINGS_PREF_NAME, 15);

        name.setTextSize(textSize);
        phone.setTextSize(textSize);
        mail.setTextSize(textSize);
        studentNo.setTextSize(textSize);
        department.setTextSize(textSize);
        user.setTextSize(textSize);
        password.setTextSize(textSize);
        information.setTextSize(textSize);
        interests.setTextSize(textSize);
        openGallery.setTextSize(textSize);
        aviation.setTextSize(textSize);
        ground.setTextSize(textSize);
        marine.setTextSize(textSize);
        cyber.setTextSize(textSize);
        signUp.setTextSize(textSize);

        signUp.setOnClickListener(v -> {
            nameString = String.valueOf(name.getText());
            surnameString = String.valueOf(surname.getText());
            mailString = String.valueOf(mail.getText());
            phoneString = String.valueOf(phone.getText());
            studentNoString = String.valueOf(studentNo.getText());
            departmentString = String.valueOf(department.getText());

            userString = String.valueOf(user.getText());
            passwordString = String.valueOf(password.getText());

            aviationString = "";
            groundString = "";
            marineString = "";
            cyberString = "";

            if (aviation.isChecked())
                aviationString = "var";

            if (ground.isChecked())
                groundString = "var";

            if (marine.isChecked())
                marineString = "var";

            if (cyber.isChecked())
                cyberString = "var";

            if (nameString.isEmpty() || surnameString.isEmpty() || mailString.isEmpty() || phoneString.isEmpty() ||
                    studentNoString.isEmpty() || departmentString.isEmpty() || userString.isEmpty() || passwordString.isEmpty()) {
                Toast.makeText(getApplicationContext(), getString(R.string.sign_up_blank), Toast.LENGTH_SHORT).show();
            } else if (!aviation.isChecked() && !ground.isChecked() && !marine.isChecked() && !cyber.isChecked()) {
                Toast.makeText(getApplicationContext(), getString(R.string.choose_at_least_one), Toast.LENGTH_SHORT).show();
            } else {
                String text = getString(R.string.sign_up_dialog_text);
                String yes = getString(R.string.yes);
                String no = getString(R.string.cancel);
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                builder.setMessage(text).setPositiveButton(yes, (dialog, which) -> foo_signUp()).setNegativeButton(no, (dialog, which) -> {

                }).show();
            }
        });

        openGallery.setOnClickListener(v -> openGallery());
    }

    @SuppressLint("IntentReset")
    private void openGallery() {
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
        }
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
            Uri filePath = data.getData();
            if (filePath != null) {
                startCropping(filePath);
            }
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            imageUriResultCrop = UCrop.getOutput(data);
            if (imageUriResultCrop != null) {
                imageView.setImageURI(imageUriResultCrop);
                imageView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void startCropping(@NonNull Uri uri) {
        String destinationFileName = "CropImage";
        destinationFileName += ".jpg";

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));
        uCrop.withAspectRatio(1, 1);
        //uCrop.withAspectRatio(3, 4);
        //uCrop.useSourceImageAspectRatio();
        //uCrop.withAspectRatio(2, 3);
        //uCrop.withAspectRatio(16, 9);

        uCrop.withMaxResultSize(450, 450);
        uCrop.withOptions(getCropOptions());

        uCrop.start(SignUp.this);
    }

    private UCrop.Options getCropOptions() {
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(70);
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(false);
        options.setStatusBarColor(Color.BLACK);
        options.setToolbarColor(Color.BLACK);
        options.setToolbarTitle(getString(R.string.crop_image));
        return options;
    }

    @SuppressLint("StaticFieldLeak")
    private void foo_signUp() {
        new AsyncTask<String, String, String>() {

            final String progress_title = getString(R.string.signing_up);
            final String progress_message = getString(R.string.wait);
            final String connection = getString(R.string.mainactivity_connection);
            final String successful = getString(R.string.signed_up_successfully);

            protected void onPreExecute() {
                progressDialog = new ProgressDialog(SignUp.this);
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
                    explanationString = "Üye";
                    karsav.signUp(nameString, surnameString, studentNoString, departmentString, mailString, phoneString, aviationString,
                            groundString, marineString, cyberString, explanationString, userString, passwordString);

                    StorageReference reference = storageReference.child(getString(R.string.firestore_database_for_profile_photos) + "/" + nameString + surnameString);
                    reference.putFile(filePath).addOnCompleteListener(task -> {

                    });
                    return "done";
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
                        Toast.makeText(SignUp.this, connection, Toast.LENGTH_SHORT).show();
                        break;

                    case "done":
                        Toast.makeText(getApplicationContext(), successful, Toast.LENGTH_LONG).show();
                        finish();
                        break;
                }
            }
        }.execute("SignUp");
    }
}
