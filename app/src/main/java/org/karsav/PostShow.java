package org.karsav;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.r0adkll.slidr.Slidr;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class PostShow extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private static final String SETTINGS_PREF_NAME = "textSize";
    final String name = UserAdapter.getUserName() + " " + UserAdapter.getUserSurName();
    long id;
    int userId;
    ImageView pen, bin, back, share, postedImage, postedImageBackground;
    PostItems postItems;
    TextView sender, time, title;
    TextInputEditText comment;
    ImageButton send;
    ImageButton likeButton, commentButton;
    TextView likeText, toolbarText, likesText, commentText, comment_text;
    ReadMoreTextView text;
    String commentString;
    Toolbar toolbar;
    LinearLayout linearLayout;
    CommentAdapter adapter;
    RecyclerView recyclerView;
    ListView list;
    android.content.ClipboardManager clipboardManager;
    PostItems post;
    NestedScrollView scrollView;
    CircleImageView postImage;
    boolean photo;
    SharedPreferences settings;
    int textSize;
    private FirebaseFirestore db;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_show);
        Slidr.attach(this);
        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        init();
        setUpComments();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void init() {
        sender = findViewById(R.id.sender);
        time = findViewById(R.id.postedTime);
        title = findViewById(R.id.postshow_title);
        text = findViewById(R.id.postshow_text);
        pen = findViewById(R.id.pen);
        bin = findViewById(R.id.bin);
        back = findViewById(R.id.back_arrow);
        share = findViewById(R.id.share);
        send = findViewById(R.id.imageButton);
        comment = findViewById(R.id.comment);
        likeButton = findViewById(R.id.likeButton);
        commentButton = findViewById(R.id.commentButton);
        likeText = findViewById(R.id.likeText);
        toolbar = findViewById(R.id.toolbar);
        toolbarText = findViewById(R.id.toolbar_text);
        linearLayout = findViewById(R.id.lienar_layout);
        likesText = findViewById(R.id.likesText);
        commentText = findViewById(R.id.commentText);
        comment_text = findViewById(R.id.comment_text);
        scrollView = findViewById(R.id.scrollView);
        postImage = findViewById(R.id.postImage);
        postedImage = findViewById(R.id.postedImage);
        postedImageBackground = findViewById(R.id.postedImageBackground);

        settings = getSharedPreferences(SETTINGS_PREF_NAME, MODE_PRIVATE);
        textSize = settings.getInt(SETTINGS_PREF_NAME, 15);

        sender.setTextSize(textSize + 2);
        time.setTextSize(textSize);
        title.setTextSize(textSize);
        text.setTextSize(textSize);
        comment.setTextSize(textSize);
        likeText.setTextSize(textSize);
        likesText.setTextSize(textSize - 2);
        commentText.setTextSize(textSize);
        comment_text.setTextSize(textSize - 2);

        likeButton.setOnClickListener(v -> foo_like());

        commentButton.setOnClickListener(v -> comment.requestFocus());

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                .build();
        db.setFirestoreSettings(settings);

        pen.setOnClickListener(v -> foo_pen());

        bin.setOnClickListener(v -> foo_bin());

        share.setOnClickListener(v -> foo_share());

        send.setOnClickListener(v -> {
            if (String.valueOf(comment.getText()).isEmpty())
                Toast.makeText(PostShow.this, getString(R.string.comment_blank), Toast.LENGTH_SHORT).show();
            else
                foo_comment();
        });

        linearLayout.setOnClickListener(v -> openLikes());

        back.setOnClickListener(v -> foo_back());

        text.setOnLongClickListener(v -> {
            ClipData clipData = ClipData.newPlainText(getString(R.string.check_text), postItems.getText());
            clipboardManager.setPrimaryClip(clipData);
            Snackbar snackbar = Snackbar.make(v, getString(R.string.copied), Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        });

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        toolbarText.setText(getString(R.string.posts));

        postItems = (PostItems) getIntent().getSerializableExtra("post");
        title.setText(postItems.getTitle());
        text.setText(postItems.getText());
        sender.setText(postItems.getName());
        String[] formattedDate = postItems.getDate().split(" ");
        time.setText(formattedDate[0]);
        id = postItems.getId();
        userId = postItems.getUserId();
        photo = postItems.isPhoto();

        String ourId = String.valueOf(UserAdapter.getUserId());
        if (ourId.equalsIgnoreCase(String.valueOf(userId))) {
            pen.setVisibility(View.VISIBLE);
            bin.setVisibility(View.VISIBLE);
        }

        db.collection(getString(R.string.firestore_database_for_posts)).document(String.valueOf(id)).collection(getString(R.string.firestore_database_for_likes)).document(ourId)
                .addSnapshotListener((documentSnapshot, e) -> {
                    if (Objects.requireNonNull(documentSnapshot).exists())
                        likeButton.setBackgroundResource(R.drawable.like_pressed);
                    else
                        likeButton.setBackgroundResource(R.drawable.like_default);
                });


        db.collection(getString(R.string.firestore_database_for_posts)).document(String.valueOf(id)).collection(getString(R.string.firestore_database_for_likes))
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (!Objects.requireNonNull(queryDocumentSnapshots).isEmpty()) {
                        int count = queryDocumentSnapshots.size();
                        String liked = getString(R.string.like_amount_text) + " " + count;
                        likeText.setText(String.valueOf(count));
                        likesText.setText(liked);
                        linearLayout.setClickable(true);
                    } else {
                        String liked = getString(R.string.like_amount_text) + " " + 0;
                        likeText.setText("0");
                        likesText.setText(liked);
                        linearLayout.setClickable(false);
                    }
                });

        db.collection(getString(R.string.firestore_database_for_posts)).document(String.valueOf(id)).collection(getString(R.string.firestore_database_for_comments))
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (!Objects.requireNonNull(queryDocumentSnapshots).isEmpty()) {
                        int count = queryDocumentSnapshots.size();
                        String commented = getString(R.string.comment_of_recyclerview) + " " + count;
                        commentText.setText(String.valueOf(count));
                        comment_text.setText(commented);
                    } else {
                        String commented = getString(R.string.comment_of_recyclerview) + " " + 0;
                        commentText.setText("0");
                        comment_text.setText(commented);
                    }
                });

        StorageReference reference = storageReference.child(getString(R.string.firestore_database_for_profile_photos) + "/" + postItems.getUserId() + ".jpg");
        reference.getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            postImage.setImageBitmap(bmp);
        });

        if (photo) {
            postedImageBackground.setVisibility(View.VISIBLE);
            StorageReference referenceSecond = storageReference.child(getString(R.string.firestore_database_for_post_photos) + "/" + postItems.getId() + ".jpg");
            referenceSecond.getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                postedImage.setImageBitmap(bmp);
                postedImage.setVisibility(View.VISIBLE);
                postedImageBackground.setVisibility(View.GONE);
            });
        }
    }

    private void setUpComments() {
        CollectionReference likeRef = db.collection(getString(R.string.firestore_database_for_posts));
        Query query = likeRef.document(String.valueOf(id)).collection(getString(R.string.firestore_database_for_comments)).orderBy("id", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<CommentItems> options = new FirestoreRecyclerOptions.Builder<CommentItems>()
                .setQuery(query, CommentItems.class)
                .build();
        adapter = new CommentAdapter(options, getApplicationContext());
        recyclerView = findViewById(R.id.comment_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setItemViewCacheSize(50);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.requestFocus();
        adapter.notifyDataSetChanged();
        Objects.requireNonNull(recyclerView.getLayoutManager()).scrollToPosition(0);
        adapter.setOnItemLongClickListener((documentSnapshot, view) -> {
            post = documentSnapshot.toObject(PostItems.class);
            int id = UserAdapter.getUserId();
            PopupMenu popup = new PopupMenu(getApplicationContext(), view);
            popup.setOnMenuItemClickListener(PostShow.this);
            if (id == postItems.getUserId()) {
                popup.inflate(R.menu.comment_long_click_menu);
            } else if (id == post.getUserId()) {
                popup.inflate(R.menu.comment_long_click_menu);
            } else {
                ClipData clipData = ClipData.newPlainText(getString(R.string.check_text), post.getText());
                clipboardManager.setPrimaryClip(clipData);
                Snackbar snackbar = Snackbar.make(view, getString(R.string.copied), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                popup.setGravity(Gravity.CENTER_HORIZONTAL);
            }
            popup.show();
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        View view = this.getCurrentFocus();
        switch (item.getItemId()) {
            case R.id.copy:
                ClipData clipData = ClipData.newPlainText(getString(R.string.check_text), post.getText());
                clipboardManager.setPrimaryClip(clipData);
                if (view != null) {
                    Snackbar snackbar = Snackbar.make(view, getString(R.string.copied), Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else
                    Toast.makeText(getApplicationContext(), getString(R.string.copied), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.delete:
                db.collection(getString(R.string.firestore_database_for_posts)).document(String.valueOf(id)).collection(getString(R.string.firestore_database_for_comments))
                        .document(String.valueOf(post.getId())).delete();
                if (view != null) {
                    Snackbar snackbar = Snackbar.make(view, getString(R.string.comment_deleted), Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else
                    Toast.makeText(getApplicationContext(), getString(R.string.comment_deleted), Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
                return true;
            default:
                return false;
        }
    }

    private void openLikes() {
        LikeShowItems likeShowItems = new LikeShowItems(postItems.getName(), postItems.getTitle(), postItems.getId());
        Intent intent = new Intent(this, LikeShow.class);
        intent.putExtra("likes", likeShowItems);
        startActivity(intent);
    }

    @SuppressLint("SimpleDateFormat")
    private void foo_like() {
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
                    } else {
                        db.collection(getString(R.string.firestore_database_for_users)).document(String.valueOf(userId)).collection(getString(R.string.firestore_database_for_likedby))
                                .document(id + " - " + ourId).set(new LikeItems(name, finalTitleString, ourId, date, time));
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void foo_share() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        String text = postItems.getText();
        share.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(share);
    }

    private void foo_back() {
        finish();
    }

    private void foo_pen() {
        Intent intent = new Intent(this, PostUpdate.class);
        intent.putExtra("post", postItems);
        startActivity(intent);
    }

    private void foo_bin() {
        String title = getString(R.string.post_delete_dialog_title);
        String exit_yes = getString(R.string.homepage_exit_yes);
        String exit_no = getString(R.string.homepage_exit_no);
        AlertDialog.Builder builder = new AlertDialog.Builder(PostShow.this);
        builder.setMessage(title).setPositiveButton(exit_yes, (dialog, which) -> {
            db.collection(getString(R.string.firestore_database_for_posts)).document(String.valueOf(Objects.requireNonNull(postItems).getId()))
                    .collection(getString(R.string.firestore_database_for_likes)).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot queryDocumentSnapshots = task.getResult();
                    for (DocumentSnapshot documentSnapshot1 : Objects.requireNonNull(queryDocumentSnapshots)) {
                        db.collection(getString(R.string.firestore_database_for_posts)).document(String.valueOf(Objects.requireNonNull(postItems).getId()))
                                .collection(getString(R.string.firestore_database_for_likes))
                                .document(documentSnapshot1.getId()).delete();
                    }
                }
            });

            db.collection(getString(R.string.firestore_database_for_posts)).document(String.valueOf(Objects.requireNonNull(postItems).getId()))
                    .collection(getString(R.string.firestore_database_for_comments)).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot queryDocumentSnapshots = task.getResult();
                    for (DocumentSnapshot documentSnapshot : Objects.requireNonNull(queryDocumentSnapshots)) {
                        db.collection(getString(R.string.firestore_database_for_posts)).document(String.valueOf(Objects.requireNonNull(postItems).getId()))
                                .collection(getString(R.string.firestore_database_for_comments))
                                .document(documentSnapshot.getId()).delete();
                    }
                }
            });

            db.collection(getString(R.string.firestore_database_for_users)).document(String.valueOf(Objects.requireNonNull(postItems).getUserId()))
                    .collection(getString(R.string.firestore_database_for_likedby)).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot queryDocumentSnapshots = task.getResult();
                    for (DocumentSnapshot documentSnapshot : Objects.requireNonNull(queryDocumentSnapshots)) {
                        String id = documentSnapshot.getId();
                        String[] likedId = id.split("-");
                        if (Integer.valueOf(likedId[0].trim()) == postItems.getId()) {
                            db.collection(getString(R.string.firestore_database_for_users)).document(String.valueOf(Objects.requireNonNull(postItems).getUserId()))
                                    .collection(getString(R.string.firestore_database_for_likedby)).document(documentSnapshot.getId()).delete();
                        }
                    }
                }
            });
            db.collection(getString(R.string.firestore_database_for_posts)).document(String.valueOf(id))
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getApplicationContext(), getString(R.string.post_deleted_succesfully), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(PostShow.this, HomePage.class));
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), getString(R.string.post_not_deleted), Toast.LENGTH_LONG).show());
            StorageReference referenceSecond = storageReference.child(getString(R.string.firestore_database_for_post_photos) + "/" + postItems.getId() + ".jpg");
            referenceSecond.delete().addOnSuccessListener(aVoid -> {

            });

        }).setNegativeButton(exit_no, (dialog, which) -> {

        }).show();
    }

    @SuppressLint("SimpleDateFormat")
    private void foo_comment() {
        commentString = String.valueOf(comment.getText());
        @SuppressLint("UseSparseArrays") final Map<Integer, Object> commentMap = new HashMap<>();
        try {
            final String name = UserAdapter.getUserName() + " " + UserAdapter.getUserSurName();
            final int userId = UserAdapter.getUserId();
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            final String date = df.format(c.getTime());
            final String time = new SimpleDateFormat("EEE, HH:mm:ss").format(Calendar.getInstance().getTime());

            Query query = db.collection(getString(R.string.firestore_database_for_posts)).document(String.valueOf(id)).collection("Comments").orderBy("id", Query.Direction.DESCENDING);
            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    int count = 0;
                    final long ourId;
                    for (QueryDocumentSnapshot queryDocumentSnapshot : (Objects.requireNonNull(task.getResult()))) {
                        try {
                            commentMap.put(count, queryDocumentSnapshot);
                        } catch (Exception e) {
                            commentMap.put(count, "");
                        }
                        count++;
                    }

                    if (commentMap.get(0) == null) {
                        ourId = 1;
                    } else {
                        String last_post_id = ((DocumentSnapshot) Objects.requireNonNull(commentMap.get(0))).getId().trim();
                        ourId = Long.valueOf(last_post_id) + 1;
                    }
                    db.collection(getString(R.string.firestore_database_for_posts)).document(String.valueOf(id)).collection(getString(R.string.firestore_database_for_comments))
                            .document(String.valueOf(ourId)).set(new CommentItems(name, commentString, date, time, userId, ourId));
                    Toast.makeText(getApplicationContext(), getString(R.string.comment_sent), Toast.LENGTH_SHORT).show();
                    comment.setText("");
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    closeKeyboard();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}