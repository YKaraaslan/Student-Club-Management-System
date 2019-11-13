package org.karsav;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.r0adkll.slidr.Slidr;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class MeetUpsMineShow extends AppCompatActivity {

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
}
