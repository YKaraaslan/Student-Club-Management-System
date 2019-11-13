package org.karsav;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class Posts extends Fragment {

    @SuppressLint("StaticFieldLeak")
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    FloatingActionButton button;
    boolean created = false;
    RecyclerView recyclerView;
    FirestoreRecyclerOptions<PostItems> options;
    private PostItems post;
    private CollectionReference postRef;
    private PostAdapter adapter;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReference();

    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.posts, container, false);
        ProgressBar progressbar = view.findViewById(R.id.progressBar);
        button = view.findViewById(R.id.add);
        button.setOnClickListener(v -> button_function());
        recyclerView = view.findViewById(R.id.post_recycler_view);
        postRef = db.collection(getString(R.string.firestore_database_for_posts));
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                .build();
        db.setFirestoreSettings(settings);
        setUpRecyclerView();
        if (created)
            progressbar.setVisibility(View.GONE);
        return view;
    }

    @SuppressLint("RestrictedApi")
    private void setUpRecyclerView() {
        Query query = postRef.orderBy("id", Query.Direction.DESCENDING).limit(30);
        options = new FirestoreRecyclerOptions.Builder<PostItems>()
                .setQuery(query, PostItems.class)
                .build();
        adapter = new PostAdapter(options, Objects.requireNonNull(getContext()));
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setItemViewCacheSize(30);
        recyclerView.setAdapter(adapter);

        created = true;
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
            ClipboardManager clipboardManager = (ClipboardManager) Objects.requireNonNull(getContext()).getSystemService(CLIPBOARD_SERVICE);
            PostItems post = documentSnapshot.toObject(PostItems.class);
            ClipData clipData = ClipData.newPlainText(getString(R.string.check_text), Objects.requireNonNull(post).getText());
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(getContext(), getString(R.string.copied), Toast.LENGTH_SHORT).show();
        });
        adapter.setOnItemClickListener(new PostAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot) {
                PostItems post = documentSnapshot.toObject(PostItems.class);
                Intent intent = new Intent(getContext(), PostShow.class);
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
            public void onDeleteClick(final DocumentSnapshot documentSnapshot) {
                final PostItems postItems = documentSnapshot.toObject(PostItems.class);
                String title = getString(R.string.post_delete_dialog_title);
                String exit_yes = getString(R.string.homepage_exit_yes);
                String exit_no = getString(R.string.homepage_exit_no);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage(title).setPositiveButton(exit_yes, (dialog, which) -> {
                    StorageReference referenceSecond = storageReference.child(getString(R.string.firestore_database_for_post_photos) + "/" + documentSnapshot.getId() + ".jpg");
                    referenceSecond.delete().addOnSuccessListener(aVoid -> {

                    });
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
                            for (DocumentSnapshot documentSnapshot12 : Objects.requireNonNull(queryDocumentSnapshots)) {
                                db.collection(getString(R.string.firestore_database_for_posts)).document(String.valueOf(Objects.requireNonNull(postItems).getId()))
                                        .collection(getString(R.string.firestore_database_for_comments))
                                        .document(documentSnapshot12.getId()).delete();
                            }
                        }
                    });

                    db.collection(getString(R.string.firestore_database_for_users)).document(String.valueOf(Objects.requireNonNull(postItems).getUserId()))
                            .collection(getString(R.string.firestore_database_for_likedby)).get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot queryDocumentSnapshots = task.getResult();
                            for (DocumentSnapshot documentSnapshot12 : Objects.requireNonNull(queryDocumentSnapshots)) {
                                String id = documentSnapshot12.getId();
                                String[] likedId = id.split("-");
                                if (Integer.valueOf(likedId[0].trim()) == postItems.getId()) {
                                    db.collection(getString(R.string.firestore_database_for_users)).document(String.valueOf(Objects.requireNonNull(postItems).getUserId()))
                                            .collection(getString(R.string.firestore_database_for_likedby)).document(documentSnapshot12.getId()).delete();
                                }
                            }
                        }
                    });


                    db.collection(getString(R.string.firestore_database_for_posts)).document(String.valueOf(Objects.requireNonNull(postItems).getId()))
                            .delete()
                            .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), getString(R.string.post_deleted_succesfully), Toast.LENGTH_LONG).show())
                            .addOnFailureListener(e -> Toast.makeText(getContext(), getString(R.string.post_not_deleted), Toast.LENGTH_LONG).show());
                }).setNegativeButton(exit_no, (dialog, which) -> {

                }).show();
            }

            @Override
            public void onUpdateClick(DocumentSnapshot documentSnapshot) {
                PostItems post = documentSnapshot.toObject(PostItems.class);
                Intent intent = new Intent(getContext(), PostUpdate.class);
                intent.putExtra("post", post);
                startActivity(intent);
            }

            @Override
            public void onCommentClick(DocumentSnapshot documentSnapshot) {
                PostItems post = documentSnapshot.toObject(PostItems.class);
                Intent intent = new Intent(getContext(), PostShow.class);
                intent.putExtra("post", post);
                startActivity(intent);
            }

            @Override
            public void onImageClick(DocumentSnapshot snapshot) {
                PostItems user = snapshot.toObject(PostItems.class);
                if (Objects.requireNonNull(user).getUserId() != UserAdapter.getUserId()) {
                    Intent intent = new Intent(getContext(), UserProfileFromPosts.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                }
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

    private void button_function() {
        startActivity(new Intent(getActivity(), PostCreate.class));
    }
}