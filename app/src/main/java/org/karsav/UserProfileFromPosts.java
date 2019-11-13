package org.karsav;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.r0adkll.slidr.Slidr;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class UserProfileFromPosts extends AppCompatActivity {

    private static final String SETTINGS_PREF_NAME = "textSize";
    Toolbar toolbar;
    CircleImageView userPhoto;
    ReadMoreTextView about;
    TextView name, position, last_login, aboutTitle, posts;
    TextView assignmentsAmount, projectsAmount, meetupsAmount, postAmount, likeAmount;
    LinearLayout assignmentsLinear, projectsLinear, meetupsLinear, postsLinear, likesLinear, userPostsLinear;
    ImageView onlineImage;
    String postAmountString, likeAmountString, meetupsAmountString, projectsAmountString, assignmentsAmountString;
    DataBase karsav = new DataBase();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences settings;
    int textSize;
    int userId;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Slidr.attach(this);
        init();
    }

    private void init() {
        userPhoto = findViewById(R.id.userPhoto);
        about = findViewById(R.id.about);
        name = findViewById(R.id.name);
        position = findViewById(R.id.position);
        last_login = findViewById(R.id.last_login);
        aboutTitle = findViewById(R.id.aboutTitle);
        assignmentsAmount = findViewById(R.id.assignmentsAmount);
        projectsAmount = findViewById(R.id.projectsAmount);
        meetupsAmount = findViewById(R.id.meetupsAmount);
        postAmount = findViewById(R.id.postAmount);
        likeAmount = findViewById(R.id.likeAmount);
        assignmentsLinear = findViewById(R.id.assignmentsLinear);
        projectsLinear = findViewById(R.id.projectsLinear);
        meetupsLinear = findViewById(R.id.meetupsLinear);
        projectsLinear = findViewById(R.id.projectsLinear);
        postsLinear = findViewById(R.id.postsLinear);
        likesLinear = findViewById(R.id.likesLinear);
        onlineImage = findViewById(R.id.onlineImage);
        userPostsLinear = findViewById(R.id.userPostsLinear);
        posts = findViewById(R.id.posts);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        View logoView = toolbar.getChildAt(1);
        logoView.setOnClickListener(v -> finish());

        settings = Objects.requireNonNull(getApplicationContext()).getSharedPreferences(SETTINGS_PREF_NAME, MODE_PRIVATE);
        textSize = settings.getInt(SETTINGS_PREF_NAME, 15);

        name.setTextSize(textSize + 5);
        position.setTextSize(textSize - 1);
        postAmount.setTextSize(textSize);
        likeAmount.setTextSize(textSize);
        last_login.setTextSize(textSize - 1);
        meetupsAmount.setTextSize(textSize);
        projectsAmount.setTextSize(textSize);
        assignmentsAmount.setTextSize(textSize);
        aboutTitle.setTextSize(textSize + 2);
        about.setTextSize(textSize);
        posts.setTextSize(textSize);

        PostItems postItems = (PostItems) getIntent().getSerializableExtra("user");
        userId = postItems.getUserId();

        name.setText(postItems.getName());
        position.setText(postItems.getTitle());
        setLikeAndPostAmount();
        setProfile(userId);

        assignmentsLinear.setOnClickListener(v -> alertDialog(getString(R.string.assignmentsLinear, String.valueOf(ProfileAdapter.getAssignmentsAmount()))));

        projectsLinear.setOnClickListener(v -> alertDialog(getString(R.string.projectsLinear, String.valueOf(ProfileAdapter.getProjectsAmount()))));

        meetupsLinear.setOnClickListener(v -> alertDialog(getString(R.string.meetupsLinear, String.valueOf(ProfileAdapter.getMeetupsAmount()))));

        postsLinear.setOnClickListener(v -> alertDialog(getString(R.string.postsLinear, String.valueOf(ProfileAdapter.getPostAmount()))));

        likesLinear.setOnClickListener(v -> alertDialog(getString(R.string.likesLinear, String.valueOf(ProfileAdapter.getLikeAmount()))));

        userPostsLinear.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ProfilePosts.class);
            intent.putExtra("user", userId);
            startActivity(intent);
        });

        StorageReference reference = storageReference.child(getString(R.string.firestore_database_for_profile_photos) + "/" + userId + ".jpg");
        reference.getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            userPhoto.setImageBitmap(bmp);
        });

        db.collection(getString(R.string.firestore_database_for_status)).document(String.valueOf(userId)).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.get("status") != null) {
                about.setText((String) documentSnapshot.get("status"));
            } else {
                about.setText(getString(R.string.status_empty));
            }
        });
    }

    private void setLikeAndPostAmount() {
        final int[] postAmountInt = {0};
        final int[] likeAmountInt = {0};
        db.collection(getString(R.string.firestore_database_for_posts)).addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (!Objects.requireNonNull(queryDocumentSnapshots).isEmpty()) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    long id = (long) documentSnapshot.get("userId");
                    if (id == userId) {
                        postAmountInt[0]++;
                    }
                }
                postAmount.setText(String.valueOf(postAmountInt[0]));
                UserProfileAdapter.setPostAmount(postAmountInt[0]);
            }
        });

        db.collection(getString(R.string.firestore_database_for_users)).document(String.valueOf(userId))
                .collection(getString(R.string.firestore_database_for_likedby)).get().addOnSuccessListener(queryDocumentSnapshots -> {
            likeAmountInt[0] = queryDocumentSnapshots.size();
            likeAmount.setText(String.valueOf(likeAmountInt[0]));
            UserProfileAdapter.setLikeAmount(likeAmountInt[0]);
        });

        final DocumentReference documentReference = db.collection(getString(R.string.firestore_database_for_users))
                .document(String.valueOf(userId));

        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            try {
                String date = (String) documentSnapshot.get("status");
                if (!Objects.requireNonNull(date).equalsIgnoreCase("Online")) {
                    DateFormatter dateFormatter = new DateFormatter();
                    String status = dateFormatter.format(date);
                    last_login.setText(getString(R.string.last_login, status));
                    onlineImage.setVisibility(View.GONE);
                } else {
                    last_login.setText(getString(R.string.online));
                    onlineImage.setVisibility(View.VISIBLE);
                }
            } catch (Exception ex) {
                last_login.setText(getString(R.string.offline));
                onlineImage.setVisibility(View.GONE);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void setProfile(final int userId) {
        new AsyncTask<String, String, String>() {
            final String connection = getString(R.string.mainactivity_connection);

            @Override
            protected String doInBackground(String... strings) {
                try {
                    if (karsav.Begin()) {
                        return "wrong";
                    }
                    karsav.setUserProfileFromDatabase(userId);
                    return "done";
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return "false";
                }
            }

            @Override
            protected void onPostExecute(String s) {
                switch (s) {
                    case "false":
                        Toast.makeText(UserProfileFromPosts.this, connection, Toast.LENGTH_SHORT).show();
                        break;

                    case "done":
                        postAmountString = String.valueOf(UserProfileAdapter.getPostAmount());
                        likeAmountString = String.valueOf(UserProfileAdapter.getLikeAmount());
                        meetupsAmountString = String.valueOf(UserProfileAdapter.getMeetupsAmount());
                        projectsAmountString = String.valueOf(UserProfileAdapter.getProjectsAmount());
                        assignmentsAmountString = String.valueOf(UserProfileAdapter.getAssignmentsAmount());

                        postAmount.setText(postAmountString);
                        likeAmount.setText(likeAmountString);
                        meetupsAmount.setText(meetupsAmountString);
                        projectsAmount.setText(projectsAmountString);
                        assignmentsAmount.setText(assignmentsAmountString);

                        break;
                }
            }
        }.execute("showProfile");
    }

    private void alertDialog(String text) {
        AlertDialog dialog = new AlertDialog.Builder(Objects.requireNonNull(getApplicationContext())).setMessage(text)
                .setPositiveButton(getString(R.string.ok), (dialog1, which) -> {

                }).show();
        TextView textView = dialog.findViewById(android.R.id.message);
        Objects.requireNonNull(textView).setTextSize(textSize);
    }
}
