package org.karsav;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;

import java.util.Objects;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class Users extends Fragment {

    RecyclerView recyclerView;
    FirestoreRecyclerOptions<UsersItems> options;
    Query query;
    CollectionReference userReference;
    private UsersAdapter userAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.users, container, false);
        recyclerView = view.findViewById(R.id.users_recycler_view);
        userReference = db.collection(getString(R.string.firestore_database_for_users));
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                .build();
        db.setFirestoreSettings(settings);
        setUpRecyclerView();
        return view;
    }

    private void setUpRecyclerView() {
        query = userReference.orderBy("status", Query.Direction.DESCENDING);
        options = new FirestoreRecyclerOptions.Builder<UsersItems>()
                .setQuery(query, UsersItems.class)
                .build();
        userAdapter = new UsersAdapter(options, Objects.requireNonNull(getContext()));
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setItemViewCacheSize(100);
        recyclerView.setAdapter(userAdapter);

        userAdapter.setOnItemClickListener(documentSnapshot -> {
            UsersItems usersItems = documentSnapshot.toObject(UsersItems.class);
            if (Objects.requireNonNull(usersItems).getId() != UserAdapter.getUserId()) {
                Intent intent = new Intent(getContext(), UserProfileFromUserList.class);
                intent.putExtra("user", usersItems);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        userAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        userAdapter.stopListening();
    }
}
