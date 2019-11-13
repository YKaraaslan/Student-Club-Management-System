package org.karsav;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class LoggedDataBase extends AppCompatActivity {

    private static final String LOGIN_STATU = "logged";
    private static final String KEY_PASS = "password";
    private static final String KEY_USERNAME = "username";
    private static final String PREF_NAME = "prefs";
    SharedPreferences loginSp, databaseSp;
    DataBase karsav = new DataBase();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        loginSp = getSharedPreferences(LOGIN_STATU, MODE_PRIVATE);
        databaseSp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        if (loginSp.getBoolean(LOGIN_STATU, true)) {
            logIn();
        } else {
            mainPage();
        }
        finish();
    }

    private void mainPage() {
        if (!karsav.Begin()) {
            try {
                karsav.RegisterUserDb();
                karsav.Disconnect();
                startActivity(new Intent(LoggedDataBase.this, MainActivity.class));
                finish();
            } catch (Exception ex) {
                ex.printStackTrace();
                finish();
            }
        }
        startActivity(new Intent(LoggedDataBase.this, MainActivity.class));
    }

    private void logIn() {
        String mail = databaseSp.getString(KEY_USERNAME, "");
        String pass = databaseSp.getString(KEY_PASS, "");
        assert mail != null;
        assert pass != null;
        if (mail.equalsIgnoreCase("") || pass.equalsIgnoreCase("")) {
            startActivity(new Intent(LoggedDataBase.this, MainActivity.class));
            finish();
        } else {
            if (!karsav.Begin()) {
                try {
                    karsav.Query(mail, pass);
                    karsav.Disconnect();
                    setProfileAdapter();
                    startActivity(new Intent(LoggedDataBase.this, HomePage.class));
                    finish();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    finish();
                }
            } else {
                finish();
            }
        }
    }

    public void setProfileAdapter() {
        final int[] postAmountInt = {0};
        final int[] likeAmountInt = {0};
        db.collection(getString(R.string.firestore_database_for_posts)).addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (!Objects.requireNonNull(queryDocumentSnapshots).isEmpty()) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    long id = (long) documentSnapshot.get("userId");
                    if (id == UserAdapter.getUserId()) {
                        postAmountInt[0]++;
                    }
                }
                ProfileAdapter.setPostAmount(postAmountInt[0]);
            }
        });

        db.collection(getString(R.string.firestore_database_for_users)).document(String.valueOf(UserAdapter.getUserId()))
                .collection(getString(R.string.firestore_database_for_likedby)).get().addOnSuccessListener(queryDocumentSnapshots -> {
            likeAmountInt[0] = queryDocumentSnapshots.size();
            ProfileAdapter.setLikeAmount(likeAmountInt[0]);
        });

        final DocumentReference documentReference = db.collection(getString(R.string.firestore_database_for_users))
                .document(String.valueOf(UserAdapter.getUserId()));

        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            String date = (String) documentSnapshot.get("status");
            if (!Objects.requireNonNull(date).equalsIgnoreCase("Online")) {
                DateFormatter dateFormatter = new DateFormatter();
                String status = dateFormatter.format(date);
                ProfileAdapter.setStatus((getString(R.string.last_login, status)));
            } else
                ProfileAdapter.setStatus((getString(R.string.online)));
        });
    }
}
