package org.karsav;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.r0adkll.slidr.Slidr;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class MeetUpsShow extends AppCompatActivity {

    private static final String SETTINGS_PREF_NAME = "textSize";
    TextView meetups_name, meetups_creator, meetups_topic, meetups_content, meetups_date, meetups_decisions, meetups_invitees;
    TextView creatorTitle, topicTitle, contentTitle, dateTitle, decisionsTitle, inviteesTitle;
    MeetUpsItem meetUpsItem;
    String name, creator, topic, content, date, decisions, invitees, idString;
    LinearLayout linear_creator, linear_topic, linear_content, linear_decisions, linear_invitees;
    int id;
    ProgressDialog progressDialog;
    DataBase karsav = new DataBase();
    Toolbar toolbar;
    SharedPreferences settings;
    int textSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetups_show);
        Slidr.attach(this);
        init();
    }

    private void init() {
        meetups_name = findViewById(R.id.meetups_name);
        meetups_creator = findViewById(R.id.meetups_creator);
        meetups_topic = findViewById(R.id.meetups_topic);
        meetups_content = findViewById(R.id.meetups_content);
        meetups_date = findViewById(R.id.meetups_date);
        meetups_decisions = findViewById(R.id.meetups_decisions);
        meetups_invitees = findViewById(R.id.meetups_invitees);

        creatorTitle = findViewById(R.id.creatorTitle);
        topicTitle = findViewById(R.id.topicTitle);
        contentTitle = findViewById(R.id.contentTitle);
        dateTitle = findViewById(R.id.dateTitle);
        decisionsTitle = findViewById(R.id.decisionsTitle);
        inviteesTitle = findViewById(R.id.inviteesTitle);

        linear_creator = findViewById(R.id.linear_creator);
        linear_topic = findViewById(R.id.linear_topic);
        linear_content = findViewById(R.id.linear_content);
        linear_decisions = findViewById(R.id.linear_decisions);
        linear_invitees = findViewById(R.id.linear_invitees);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        View logoView = toolbar.getChildAt(1);
        logoView.setOnClickListener(v -> finish());

        settings = getSharedPreferences(SETTINGS_PREF_NAME, MODE_PRIVATE);
        textSize = settings.getInt(SETTINGS_PREF_NAME, 15);

        meetups_name.setTextSize(textSize + 10);
        meetups_creator.setTextSize(textSize);
        meetups_topic.setTextSize(textSize);
        meetups_content.setTextSize(textSize);
        meetups_date.setTextSize(textSize);
        meetups_decisions.setTextSize(textSize);
        meetups_invitees.setTextSize(textSize);
        creatorTitle.setTextSize(textSize + 5);
        topicTitle.setTextSize(textSize + 5);
        contentTitle.setTextSize(textSize + 5);
        dateTitle.setTextSize(textSize + 5);
        decisionsTitle.setTextSize(textSize + 5);
        inviteesTitle.setTextSize(textSize + 5);

        meetUpsItem = (MeetUpsItem) getIntent().getSerializableExtra("MeetUps");

        try {
            id = meetUpsItem.getId();
            name = meetUpsItem.getName().trim();
            creator = meetUpsItem.getCreator().trim();
            topic = meetUpsItem.getTopic().trim();
            content = meetUpsItem.getContent().trim();
            date = meetUpsItem.getDate().trim();
            decisions = meetUpsItem.getDecision().trim();
            invitees = meetUpsItem.getInvitees().trim();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (decisions.isEmpty() || decisions.equalsIgnoreCase("null")) {
            linear_decisions.setVisibility(View.GONE);
        }
        if (content.isEmpty() || content.equalsIgnoreCase("null")) {
            linear_content.setVisibility(View.GONE);
        }
        if (topic.isEmpty() || topic.equalsIgnoreCase("null")) {
            linear_topic.setVisibility(View.GONE);
        }
        if (creator.isEmpty() || creator.equalsIgnoreCase("null")) {
            linear_creator.setVisibility(View.GONE);
        }
        if (invitees.isEmpty() || invitees.equalsIgnoreCase("null")) {
            linear_invitees.setVisibility(View.GONE);
        }
        meetups_name.setText(name);
        meetups_creator.setText(creator);
        meetups_topic.setText(topic);
        meetups_content.setText(content);
        meetups_date.setText(date);
        meetups_decisions.setText(decisions);
        meetups_invitees.setText(invitees);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update_delete, menu);
        if (UserAdapter.getTopKay() != null && UserAdapter.getTopKay().equalsIgnoreCase("VAR")) {
            menu.findItem(R.id.update).setVisible(true);
            menu.findItem(R.id.delete).setVisible(true);
        } else {
            menu.findItem(R.id.update).setVisible(false);
            menu.findItem(R.id.delete).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.update:
                Intent intent = new Intent(MeetUpsShow.this, MeetUpsUpdate.class);
                intent.putExtra("UpdateMeetUp", meetUpsItem);
                startActivity(intent);
                finish();
                break;

            case R.id.delete:
                String title = getString(R.string.meetup_delete_dialog_title);
                String text = getString(R.string.meetup_delete_dialog_message);
                String yes = getString(R.string.reference_yes);
                String no = getString(R.string.reference_no);
                AlertDialog.Builder builder = new AlertDialog.Builder(MeetUpsShow.this);
                builder.setTitle(title).setMessage(text).setPositiveButton(yes, (dialog, which) -> foo_delete()).setNegativeButton(no, (dialog, which) -> {
                }).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("StaticFieldLeak")
    private void foo_delete() {
        new AsyncTask<String, String, String>() {

            final String progress_title = getString(R.string.register_reference_delete_progress_title);
            final String progress_message = getString(R.string.register_reference_progress_message);
            final String connection = getString(R.string.mainactivity_connection);
            final String deleted = getString(R.string.meetup_deleted);

            protected void onPreExecute() {
                progressDialog = new ProgressDialog(MeetUpsShow.this);
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
                    karsav.MeetUpDeleteFromDatabase(id);
                    karsav.Disconnect();
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
                        Toast.makeText(MeetUpsShow.this, connection, Toast.LENGTH_SHORT).show();
                        break;

                    case "done":
                        startActivity(new Intent(MeetUpsShow.this, HomePage.class));
                        Toast.makeText(MeetUpsShow.this, deleted, Toast.LENGTH_LONG).show();
                        finish();
                        break;
                }
            }
        }.execute("delete");
    }
}
