package org.karsav;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.r0adkll.slidr.Slidr;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.karsav.FinanceAdd.PICK_IMAGE;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class PostCreate extends AppCompatActivity implements TextWatcher {
    private static final String SETTINGS_PREF_NAME = "textSize";
    TextInputEditText text;
    FirebaseFirestore db;
    long id;
    Toolbar toolbar;
    TextView number;
    SharedPreferences settings;
    int textSize;
    int counter, maxCharacter = 1000, sumCharacter;
    LinearLayout addPhotoLinear;
    ImageView photo;
    TextView addPhoto;
    Bitmap imageBitmap;
    Uri filePath;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_create);
        Slidr.attach(this);
        db = FirebaseFirestore.getInstance();
        init();
    }

    public void init() {
        text = findViewById(R.id.post_create_text);
        number = findViewById(R.id.number);
        addPhotoLinear = findViewById(R.id.addPhotoLinear);
        photo = findViewById(R.id.photo);
        addPhoto = findViewById(R.id.addPhoto);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        View logoView = toolbar.getChildAt(1);
        logoView.setOnClickListener(v -> finish());

        text.addTextChangedListener(this);
        number.setText(String.valueOf(maxCharacter));
        number.setTextColor(Color.GRAY);

        settings = getSharedPreferences(SETTINGS_PREF_NAME, MODE_PRIVATE);
        textSize = settings.getInt(SETTINGS_PREF_NAME, 15);

        number.setTextSize(textSize - 2);
        text.setTextSize(textSize);
        addPhoto.setTextSize(textSize);

        addPhotoLinear.setOnClickListener(v -> openGallery());

        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_a_post_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.post) {
            foo_send();
        }
        return super.onOptionsItemSelected(item);
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
            filePath = data.getData();
            if (filePath != null) {
                photo.setImageURI(filePath);
                photo.setVisibility(View.VISIBLE);
                addPhoto.setText(R.string.change_photo);
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    public void foo_send() {
        @SuppressLint("UseSparseArrays") final Map<Integer, Object> post = new HashMap<>();
        if (isNetworkAvailable()) {
            final int userId = UserAdapter.getUserId();
            final String getTitles = (String) RegisterOfficialGet.getExplanation().get(userId);
            final String getTexts = Objects.requireNonNull(text.getText()).toString().trim();
            if (getTexts.isEmpty()) {
                Toast.makeText(getApplicationContext(), getString(R.string.post_create_blank), Toast.LENGTH_LONG).show();
            } else {
                String name = UserAdapter.getUserName();
                String surname = UserAdapter.getUserSurName();
                final String full_name = name + " " + surname;
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                final String formattedDate = df.format(c.getTime());
                final String time = new SimpleDateFormat("EEE, HH:mm:ss").format(Calendar.getInstance().getTime());
                Query query = db.collection(getString(R.string.firestore_database_for_posts)).orderBy("id", Query.Direction.DESCENDING);
                query.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int count = 0;
                        for (QueryDocumentSnapshot queryDocumentSnapshot : (Objects.requireNonNull(task.getResult()))) {
                            try {
                                post.put(count, queryDocumentSnapshot);
                            } catch (Exception e) {
                                post.put(count, "");
                            }
                            count++;
                        }

                        if (post.get(0) == null) {
                            id = 1;
                        } else {
                            String last_post_id = ((DocumentSnapshot) Objects.requireNonNull(post.get(0))).getId().trim();
                            id = Long.valueOf(last_post_id) + 1;
                        }
                        boolean photo = false;
                        if (filePath != null) {
                            photo = true;
                            StorageReference reference = storageReference.child(getString(R.string.firestore_database_for_post_photos) + "/" + id + ".jpg");
                            reference.putFile(filePath).addOnCompleteListener(task1 -> {

                            });
                        }
                        addPost(new PostItems(getTitles, getTexts, full_name, time, userId, formattedDate, id, photo), id);
                    }
                });
            }
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.mainactivity_connection), Toast.LENGTH_SHORT).show();
        }
    }

    public void addPost(PostItems post, long id) {
        db.collection(getString(R.string.firestore_database_for_posts)).document(String.valueOf(id)).set(post);
        Toast.makeText(getApplicationContext(), getString(R.string.post_shared), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, HomePage.class));
        finish();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        counter = Objects.requireNonNull(text.getText()).length();
        sumCharacter = maxCharacter - counter;
        number.setText(String.valueOf(sumCharacter));
        if (sumCharacter < 51) {
            number.setTextColor(Color.RED);
        } else
            number.setTextColor(Color.GRAY);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}