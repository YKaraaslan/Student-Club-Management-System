package org.karsav;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.r0adkll.slidr.Slidr;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

import static org.karsav.FinanceAdd.PICK_IMAGE;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class PostUpdate extends AppCompatActivity implements TextWatcher {

    private static final String SETTINGS_PREF_NAME = "textSize";
    TextInputEditText text;
    FirebaseFirestore db;
    long id;
    PostItems postItems;
    Toolbar toolbar;
    TextView number;
    int counter, maxCharacter = 1000, sumCharacter;
    SharedPreferences settings;
    int textSize;
    TextView addPhoto;
    LinearLayout addPhotoLinear;
    ImageView photo, postedImageBackground;
    Bitmap imageBitmap;
    Uri filePath;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_create);
        Slidr.attach(this);
        db = FirebaseFirestore.getInstance();
        setImage();
        init();
    }

    private void setImage() {
        photo = findViewById(R.id.photo);
        postedImageBackground = findViewById(R.id.postedImageBackground);

        postItems = (PostItems) getIntent().getSerializableExtra("post");
        StorageReference referenceSecond = storageReference.child(getString(R.string.firestore_database_for_post_photos) + "/" + postItems.getId() + ".jpg");
        referenceSecond.getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            photo.setImageBitmap(bmp);
            photo.setVisibility(View.VISIBLE);
            postedImageBackground.setVisibility(View.GONE);
        });
    }

    public void init() {
        text = findViewById(R.id.post_create_text);
        toolbar = findViewById(R.id.toolbar);
        number = findViewById(R.id.number);
        addPhoto = findViewById(R.id.addPhoto);
        addPhotoLinear = findViewById(R.id.addPhotoLinear);

        setSupportActionBar(toolbar);
        View logoView = toolbar.getChildAt(1);
        logoView.setOnClickListener(v -> finish());
        toolbar.setTitle(getString(R.string.update_post));

        text.addTextChangedListener(this);

        settings = getSharedPreferences(SETTINGS_PREF_NAME, MODE_PRIVATE);
        textSize = settings.getInt(SETTINGS_PREF_NAME, 15);

        text.setText(postItems.getText());
        id = postItems.getId();

        number.setTextSize(textSize - 2);
        text.setTextSize(textSize);
        addPhoto.setTextSize(textSize);
        if (postItems.isPhoto()) {
            addPhoto.setText(getString(R.string.change_photo));
            postedImageBackground.setVisibility(View.VISIBLE);
        }
        addPhotoLinear.setOnClickListener(v -> openGallery());
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
            filePath = data.getData();
            if (filePath != null) {
                photo.setImageURI(filePath);
                photo.setVisibility(View.VISIBLE);
                addPhoto.setText(R.string.change_photo);
            }
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
            foo_update();
        }
        return super.onOptionsItemSelected(item);
    }

    private void foo_update() {
        String getTitle = (String) RegisterOfficialGet.getExplanation().get(UserAdapter.getUserId());
        String getText = Objects.requireNonNull(text.getText()).toString().trim();
        String name = postItems.getName();
        String time = postItems.getTime();
        String date = postItems.getDate();
        int userId = UserAdapter.getUserId();
        boolean photoExists = false;
        if (filePath != null)
            photoExists = true;
        if (!Objects.requireNonNull(getTitle).isEmpty() && !getText.isEmpty()) {
            this.db.collection(getString(R.string.firestore_database_for_posts)).document(String.valueOf(id)).set(new PostItems(getTitle, getText, name, time, userId, date, id, photoExists)).addOnSuccessListener(aVoid -> {

            });

            StorageReference reference = storageReference.child(getString(R.string.firestore_database_for_post_photos) + "/" + id + ".jpg");
            reference.putFile(filePath).addOnCompleteListener(task -> {
                Toast.makeText(PostUpdate.this, getString(R.string.post_updated_succesfully), Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(PostUpdate.this, HomePage.class));
            });
        }
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
