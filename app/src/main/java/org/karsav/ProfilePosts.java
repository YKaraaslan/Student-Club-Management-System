package org.karsav;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.r0adkll.slidr.Slidr;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class ProfilePosts extends AppCompatActivity {

    Toolbar toolbar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    PostAdapter adapter;
    RecyclerView recyclerView;
    PostItems post;
    FloatingActionButton button;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_posts);
        Slidr.attach(this);
        init();
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        button = findViewById(R.id.add);
        button.setOnClickListener(v -> button_function());
        setSupportActionBar(toolbar);
        View logoView = toolbar.getChildAt(1);
        logoView.setOnClickListener(v -> finish());

        id = getIntent().getIntExtra("user", 0);
        if (id != UserAdapter.getUserId()) {
            toolbar.setTitle(getString(R.string.detail));
        }
        setUpPosts();
    }

    private void button_function() {
        startActivity(new Intent(ProfilePosts.this, PostCreate.class));
    }


    private void setUpPosts() {
        CollectionReference likeRef = db.collection(getString(R.string.firestore_database_for_posts));
        Query query = likeRef.orderBy("id", Query.Direction.DESCENDING).whereEqualTo("userId", id);
        FirestoreRecyclerOptions<PostItems> options = new FirestoreRecyclerOptions.Builder<PostItems>()
                .setQuery(query, PostItems.class)
                .build();
        adapter = new PostAdapter(options, getApplicationContext());
        RecyclerView recyclerView = findViewById(R.id.my_post_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemViewCacheSize(5);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy < 0 && !button.isShown())
                    button.show();
                else if (dy > 0 && button.isShown())
                    button.hide();
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });


        adapter.setOnItemLongClickListener(documentSnapshot -> {
            ClipboardManager clipboardManager = (ClipboardManager) Objects.requireNonNull(getApplicationContext()).getSystemService(CLIPBOARD_SERVICE);
            PostItems post = documentSnapshot.toObject(PostItems.class);
            ClipData clipData = ClipData.newPlainText(getString(R.string.check_text), Objects.requireNonNull(post).getText());
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(getApplicationContext(), getString(R.string.copied), Toast.LENGTH_SHORT).show();
        });
        adapter.setOnItemClickListener(new PostAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot) {
                PostItems post = documentSnapshot.toObject(PostItems.class);
                Intent intent = new Intent(getApplicationContext(), PostShow.class);
                intent.putExtra("post", post);
                startActivity(intent);
            }

            @SuppressLint("SimpleDateFormat")
            @Override
            public void onLikeClick(int position, final ImageButton likeButton, TextView likeText, final String id, final long userId) {
                try {
                    final String name = UserAdapter.getUserName() + " " + UserAdapter.getUserSurName();
                    final int ourId = UserAdapter.getUserId();
                    String titleString = UserAdapter.getPozisyon();
                    if (titleString.isEmpty()) {
                        titleString = "Üye";
                    }
                    final String finalTitleString = titleString;
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    final String date = df.format(c.getTime());
                    final String time = new SimpleDateFormat("EEE, HH:mm:ss").format(Calendar.getInstance().getTime());
                    DocumentReference documentReference = db.collection(getString(R.string.firestore_database_for_posts)).document(String.valueOf(id)).collection(getString(R.string.firestore_database_for_likes)).document(String.valueOf(ourId));
                    documentReference.get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (Objects.requireNonNull(document).exists()) {
                                db.collection(getString(R.string.firestore_database_for_posts)).document(String.valueOf(id)).collection(getString(R.string.firestore_database_for_likes)).document(String.valueOf(ourId)).delete();
                                likeButton.setBackgroundResource(R.drawable.like_default);
                            } else {
                                db.collection(getString(R.string.firestore_database_for_posts)).document(String.valueOf(id)).collection(getString(R.string.firestore_database_for_likes)).document(String.valueOf(ourId)).set(new LikeItems(name, finalTitleString, ourId, date, time));
                                likeButton.setBackgroundResource(R.drawable.like_pressed);
                            }
                        }
                    });

                    db.collection(getString(R.string.firestore_database_for_users)).document(String.valueOf(userId)).collection(getString(R.string.firestore_database_for_likedby))
                            .document(id + " - " + ourId).get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (Objects.requireNonNull(document).exists()) {
                                db.collection(getString(R.string.firestore_database_for_users)).document(String.valueOf(userId)).collection(getString(R.string.firestore_database_for_likedby))
                                        .document(id + " - " + ourId).delete();
                                likeButton.setBackgroundResource(R.drawable.like_default);
                            } else {
                                db.collection(getString(R.string.firestore_database_for_users)).document(String.valueOf(userId)).collection(getString(R.string.firestore_database_for_likedby))
                                        .document(id + " - " + ourId).set(new LikeItems(name, finalTitleString, ourId, date, time));
                                likeButton.setBackgroundResource(R.drawable.like_pressed);
                            }
                        }
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onShareClick(DocumentSnapshot snapshot) {
                post = snapshot.toObject(PostItems.class);
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                String text = post.getText();
                share.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(share);
            }

            @Override
            public void onDeleteClick(DocumentSnapshot documentSnapshot) {
                final PostItems post = documentSnapshot.toObject(PostItems.class);
                String title = getString(R.string.post_delete_dialog_title);
                String text = getString(R.string.post_delete_dialog_message);
                String exit_yes = getString(R.string.homepage_exit_yes);
                String exit_no = getString(R.string.homepage_exit_no);
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setTitle(title).setMessage(text).setPositiveButton(exit_yes, (dialog, which) -> db.collection(getString(R.string.firestore_database_for_posts)).document(String.valueOf(Objects.requireNonNull(post).getId()))
                        .delete()
                        .addOnSuccessListener(aVoid -> Toast.makeText(getApplicationContext(), getString(R.string.post_deleted_succesfully), Toast.LENGTH_LONG).show())
                        .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), getString(R.string.post_not_deleted), Toast.LENGTH_LONG).show())).setNegativeButton(exit_no, (dialog, which) -> {

                }).show();
            }

            @Override
            public void onUpdateClick(DocumentSnapshot documentSnapshot) {
                PostItems post = documentSnapshot.toObject(PostItems.class);
                Intent intent = new Intent(getApplicationContext(), PostUpdate.class);
                intent.putExtra("post", post);
                startActivity(intent);
            }

            @Override
            public void onCommentClick(DocumentSnapshot documentSnapshot) {
                PostItems post = documentSnapshot.toObject(PostItems.class);
                Intent intent = new Intent(getApplicationContext(), PostShow.class);
                intent.putExtra("post", post);
                startActivity(intent);
            }

            @Override
            public void onImageClick(DocumentSnapshot snapshot) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
