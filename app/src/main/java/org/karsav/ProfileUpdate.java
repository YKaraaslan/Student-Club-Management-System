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
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mysql.cj.jdbc.Blob;
import com.r0adkll.slidr.Slidr;
import com.yalantis.ucrop.UCrop;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import static org.karsav.FinanceAdd.PICK_IMAGE;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class ProfileUpdate extends AppCompatActivity {

    private static final String SETTINGS_PREF_NAME = "textSize";
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    Toolbar toolbar;
    EditText name, surname, student_no, department, mail, phone_number;
    String nameString, surnameString, student_noString, departmentString, mailString, phone_numberString, userString, passwordString;
    CheckBox aviation_check, ground_check, marine_check, cyber_check;
    TextInputEditText user, password;
    Button sign_up;
    String aviationString, groundString, marineString, cyberString;
    ProgressDialog progressDialog;
    DataBase karsav = new DataBase();
    TextView interests, aviation, ground, marine, cyber, information;
    ImageView imageView;
    Blob image;
    ProgressBar progressBar;
    Bitmap imageBitmap;
    Button openGallery;
    Uri imageUriResultCrop;
    SharedPreferences settings;
    int textSize;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Slidr.attach(this);
        init();
    }

    public void init() {

        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        student_no = findViewById(R.id.student_no);
        department = findViewById(R.id.department);
        mail = findViewById(R.id.mail);
        phone_number = findViewById(R.id.phone_number);
        progressBar = findViewById(R.id.progress_bar);

        interests = findViewById(R.id.interests);
        aviation = findViewById(R.id.aviation);
        ground = findViewById(R.id.ground);
        marine = findViewById(R.id.marine);
        cyber = findViewById(R.id.cyber);
        information = findViewById(R.id.information);
        aviation_check = findViewById(R.id.aviation_check);
        ground_check = findViewById(R.id.ground_check);
        marine_check = findViewById(R.id.marine_check);
        cyber_check = findViewById(R.id.cyber_check);

        user = findViewById(R.id.user);
        password = findViewById(R.id.password);

        imageView = findViewById(R.id.image);
        openGallery = findViewById(R.id.open_gallery);

        imageView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        sign_up = findViewById(R.id.sign_up);
        sign_up.setText(getString(R.string.update_profile));
        sign_up.setVisibility(View.GONE);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.update_profile));
        View logoView = toolbar.getChildAt(1);
        logoView.setOnClickListener(v -> finish());

        settings = getSharedPreferences(SETTINGS_PREF_NAME, MODE_PRIVATE);
        textSize = settings.getInt(SETTINGS_PREF_NAME, 15);

        name.setTextSize(textSize);
        surname.setTextSize(textSize);
        student_no.setTextSize(textSize);
        department.setTextSize(textSize);
        mail.setTextSize(textSize);
        phone_number.setTextSize(textSize);
        interests.setTextSize(textSize + 5);
        aviation.setTextSize(textSize);
        ground.setTextSize(textSize);
        marine.setTextSize(textSize);
        cyber.setTextSize(textSize);
        information.setTextSize(textSize + 5);
        aviation_check.setTextSize(textSize);
        ground_check.setTextSize(textSize);
        marine_check.setTextSize(textSize);
        cyber_check.setTextSize(textSize);
        user.setTextSize(textSize);
        password.setTextSize(textSize);
        openGallery.setTextSize(textSize);

        name.setText(UserAdapter.getUserName());
        surname.setText(UserAdapter.getUserSurName());
        student_no.setText(UserAdapter.getStudentNo());
        department.setText(UserAdapter.getDepartment());
        mail.setText(UserAdapter.getMail());
        phone_number.setText(UserAdapter.getPhone());

        if (UserAdapter.getAviation().equalsIgnoreCase("VAR"))
            aviation_check.setChecked(true);

        if (UserAdapter.getGround().equalsIgnoreCase("VAR"))
            ground_check.setChecked(true);

        if (UserAdapter.getMarine().equalsIgnoreCase("VAR"))
            marine_check.setChecked(true);

        if (UserAdapter.getCyber().equalsIgnoreCase("VAR"))
            cyber_check.setChecked(true);

        user.setText(UserAdapter.getKullaniciAd());
        password.setText(UserAdapter.getSifre());

        sign_up.setOnClickListener(v -> update());

        openGallery.setOnClickListener(v -> openGallery());

        setImage();
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
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUriResultCrop);
                    UserAdapter.setUserImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

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

        uCrop.start(ProfileUpdate.this);
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

    @SuppressLint("ResourceAsColor")
    private void setImage() {
        imageView.setImageBitmap(UserAdapter.getUserImage());
        imageView.setBackgroundColor(Color.WHITE);
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
            update();
        }
        return true;
    }

    private void update() {
        nameString = name.getText().toString();
        surnameString = surname.getText().toString();
        departmentString = department.getText().toString();
        phone_numberString = phone_number.getText().toString();
        mailString = mail.getText().toString();
        student_noString = student_no.getText().toString();
        userString = Objects.requireNonNull(user.getText()).toString();
        passwordString = Objects.requireNonNull(password.getText()).toString();

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

        if (nameString.isEmpty() || surnameString.isEmpty() || departmentString.isEmpty() || phone_numberString.isEmpty() || mailString.isEmpty() || student_noString.isEmpty() ||
                userString.isEmpty() || passwordString.isEmpty()) {
            Toast.makeText(ProfileUpdate.this, getString(R.string.empty_blanks), Toast.LENGTH_SHORT).show();
        } else if (aviationString.isEmpty() && groundString.isEmpty() && marineString.isEmpty() && cyberString.isEmpty())
            Toast.makeText(ProfileUpdate.this, getString(R.string.choose_at_least_one), Toast.LENGTH_SHORT).show();
        else {
            String text = getString(R.string.update_profile_text);
            String yes = getString(R.string.update);
            String no = getString(R.string.cancel);
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileUpdate.this);
            builder.setMessage(text).setPositiveButton(yes, (dialog, which) -> updateProfile()).setNegativeButton(no, (dialog, which) -> {
            }).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void updateProfile() {
        new AsyncTask<String, String, String>() {

            final String progress_title = getString(R.string.updating);
            final String progress_message = getString(R.string.register_reference_progress_message);
            final String connection = getString(R.string.mainactivity_connection);
            final String added = getString(R.string.profile_updated);

            protected void onPreExecute() {
                progressDialog = new ProgressDialog(ProfileUpdate.this);
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
                    int userId = UserAdapter.getUserId();
                    karsav.updateUser(userId, nameString, surnameString, departmentString, phone_numberString, mailString, student_noString,
                            userString, passwordString, aviationString, groundString, marineString, cyberString);
                    UserAdapter.setUserName(nameString);
                    UserAdapter.setUserSurName(surnameString);
                    UserAdapter.setDepartment(departmentString);
                    UserAdapter.setPhone(phone_numberString);
                    UserAdapter.setMail(mailString);
                    UserAdapter.setStudentNo(student_noString);
                    UserAdapter.setKullaniciAd(userString);
                    UserAdapter.setSifre(passwordString);
                    UserAdapter.setAviation(aviationString);
                    UserAdapter.setGround(groundString);
                    UserAdapter.setMarine(marineString);
                    UserAdapter.setCyber(cyberString);

                    karsav.Disconnect();
                    StorageReference reference = storageReference.child(getString(R.string.firestore_database_for_profile_photos) + "/" + UserAdapter.getUserId() + ".jpg");
                    if (imageUriResultCrop != null) {
                        {
                            reference.putFile(imageUriResultCrop).addOnCompleteListener(task -> {

                            });
                        }
                    }
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
                        Toast.makeText(ProfileUpdate.this, connection, Toast.LENGTH_SHORT).show();
                        break;

                    case "done":
                        startActivity(new Intent(ProfileUpdate.this, HomePage.class));
                        Toast.makeText(getApplicationContext(), added, Toast.LENGTH_LONG).show();
                        finish();
                        break;
                }
            }
        }.execute("updateProfile");
    }
}
