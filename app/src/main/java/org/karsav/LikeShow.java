package org.karsav;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.r0adkll.slidr.Slidr;

import java.util.Objects;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class LikeShow extends AppCompatActivity {

    final String name = UserAdapter.getUserName() + " " + UserAdapter.getUserSurName();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    long id;
    Toolbar toolbar;
    TextView toolbarText;
    ImageView back;
    LikeAdapter adapter;
    LikeShowItems likeShowItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_show);
        Slidr.attach(this);
        init();
    }

    private void init() {
        back = findViewById(R.id.back_arrow);
        toolbar = findViewById(R.id.toolbar);
        toolbarText = findViewById(R.id.toolbar_text);

        back.setOnClickListener(v -> finish());

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        toolbarText.setText(getString(R.string.liked_by));

        likeShowItems = (LikeShowItems) getIntent().getSerializableExtra("likes");
        id = likeShowItems.getId();
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        CollectionReference likeRef = db.collection(getString(R.string.firestore_database_for_posts));
        Query query = likeRef.document(String.valueOf(id)).collection(getString(R.string.firestore_database_for_likes)).orderBy("id", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<LikeShowItems> options = new FirestoreRecyclerOptions.Builder<LikeShowItems>()
                .setQuery(query, LikeShowItems.class)
                .build();
        adapter = new LikeAdapter(options, getApplicationContext());
        RecyclerView recyclerView = findViewById(R.id.like_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setItemViewCacheSize(50);
        recyclerView.setAdapter(adapter);
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
