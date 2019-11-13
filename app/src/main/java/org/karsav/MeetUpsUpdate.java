package org.karsav;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.r0adkll.slidr.Slidr;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class MeetUpsUpdate extends AppCompatActivity {

    private static final String SETTINGS_PREF_NAME = "textSize";
    EditText name, topic, content, decisions, invitees, date, time;
    String nameString, topicString, contentString, decisionsString, inviteesString, dateString, timeString;
    int id;
    DataBase karsav = new DataBase();
    ProgressDialog progressDialog;
    TimePickerDialog timePickerDialog;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    String userName;
    MeetUpsItem meetUpsItem;
    Toolbar toolbar;
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> officialItems = new ArrayList<>();
    SharedPreferences settings;
    int textSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_ups_update);
        Slidr.attach(this);
        init();
    }

    private void init() {
        name = findViewById(R.id.name);
        topic = findViewById(R.id.topic);
        content = findViewById(R.id.content);
        decisions = findViewById(R.id.decisions);
        invitees = findViewById(R.id.invitees);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        View logoView = toolbar.getChildAt(1);
        logoView.setOnClickListener(v -> finish());
        toolbar.setTitle(getString(R.string.update_meetup));

        settings = getSharedPreferences(SETTINGS_PREF_NAME, MODE_PRIVATE);
        textSize = settings.getInt(SETTINGS_PREF_NAME, 15);

        name.setTextSize(textSize);
        topic.setTextSize(textSize);
        content.setTextSize(textSize);
        decisions.setTextSize(textSize);
        invitees.setTextSize(textSize);
        date.setTextSize(textSize);
        time.setTextSize(textSize);

        userName = UserAdapter.getUserName() + " " + UserAdapter.getUserSurName();

        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener getDate = (view, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "dd.MM.yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            date.setText(sdf.format(myCalendar.getTime()));
        };

        date.setOnClickListener(v -> new DatePickerDialog(MeetUpsUpdate.this, getDate, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        time.setOnClickListener(v -> {
            calendar = Calendar.getInstance();
            currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            currentMinute = calendar.get(Calendar.MINUTE);

            timePickerDialog = new TimePickerDialog(MeetUpsUpdate.this, (view, hourOfDay, minute) -> {
                String text = hourOfDay + ":" + minute;
                time.setText(text);
            }, currentHour, currentMinute, true);
            timePickerDialog.show();
        });

        meetUpsItem = (MeetUpsItem) getIntent().getSerializableExtra("UpdateMeetUp");

        id = meetUpsItem.getId();
        name.setText(meetUpsItem.getName());
        topic.setText(meetUpsItem.getTopic());
        content.setText(meetUpsItem.getContent());
        decisions.setText(meetUpsItem.getDecision());
        invitees.setText(meetUpsItem.getInvitees());
        date.setText(meetUpsItem.getDate());
        time.setText(meetUpsItem.getTime());


        invitees.setOnClickListener(v -> {
            Map map = RegisterOfficialGet.getNameSurname();
            listItems = (String[]) map.values().toArray(new String[0]);
            checkedItems = new boolean[listItems.length];

            AlertDialog.Builder mBuilder = new AlertDialog.Builder(MeetUpsUpdate.this);
            mBuilder.setTitle(R.string.officials);
            mBuilder.setMultiChoiceItems(listItems, checkedItems, (dialogInterface, position, isChecked) -> {
                if (isChecked) {
                    if (!officialItems.contains(position)) {
                        officialItems.add(position);
                    }
                } else if (officialItems.contains(position)) {
                    officialItems.remove(Integer.valueOf(position));
                }
            });

            mBuilder.setPositiveButton(R.string.ok, (dialogInterface, which) -> {
                StringBuilder item = new StringBuilder();
                for (int i = 0; i < officialItems.size(); i++) {
                    item.append(listItems[officialItems.get(i)]);
                    if (i != officialItems.size() - 1) {
                        item.append("\n");
                    }
                }
                invitees.setText(item.toString());
            });

            mBuilder.setNegativeButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss());

            mBuilder.setNeutralButton(R.string.clear_all, (dialogInterface, which) -> {
                for (int i = 0; i < checkedItems.length; i++) {
                    checkedItems[i] = false;
                    officialItems.clear();
                    invitees.setText("");
                }
            });

            AlertDialog mDialog = mBuilder.create();
            mDialog.show();
        });
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
            done();
        }
        return super.onOptionsItemSelected(item);
    }

    private void done() {
        try {
            nameString = name.getText().toString();
            topicString = topic.getText().toString();
            contentString = content.getText().toString();
            decisionsString = decisions.getText().toString();
            inviteesString = invitees.getText().toString();
            dateString = date.getText().toString();
            timeString = time.getText().toString();
        } catch (Exception ex) {
            Toast.makeText(MeetUpsUpdate.this, getString(R.string.mainactivity_blank), Toast.LENGTH_SHORT).show();
            return;
        }
        if (nameString.equalsIgnoreCase("null") || nameString.isEmpty()) {
            Toast.makeText(MeetUpsUpdate.this, getString(R.string.name_empty), Toast.LENGTH_SHORT).show();
        } else {
            String text = getString(R.string.meetup_update_dialog_text);
            String yes = getString(R.string.meetup_add_yes);
            String no = getString(R.string.meetup_add_no);
            AlertDialog.Builder builder = new AlertDialog.Builder(MeetUpsUpdate.this);
            builder.setMessage(text).setPositiveButton(yes, (dialog, which) -> update()).setNegativeButton(no, (dialog, which) -> {

            }).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void update() {
        new AsyncTask<String, String, String>() {

            final String progress_title = getString(R.string.project_update_progress_title);
            final String progress_message = getString(R.string.register_reference_progress_message);
            final String connection = getString(R.string.mainactivity_connection);
            final String updated = getString(R.string.meetup_updated);

            protected void onPreExecute() {
                progressDialog = new ProgressDialog(MeetUpsUpdate.this);
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
                    karsav.MeetUpsUpdate(nameString, topicString, contentString, decisionsString, inviteesString, userName, dateString, timeString, id);
                    karsav.MeetUpsDeleteUserList(id);
                    try {
                        String[] split = inviteesString.split("\n");
                        for (String s : split) {
                            String[] nameSurname = s.split((" "), 3);
                            karsav.MeetUpsAddInviteesToDatabase(nameSurname, nameString, topicString, contentString, decisionsString, userName, dateString, timeString);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
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
                        Toast.makeText(MeetUpsUpdate.this, connection, Toast.LENGTH_SHORT).show();
                        break;

                    case "done":
                        startActivity(new Intent(MeetUpsUpdate.this, HomePage.class));
                        Toast.makeText(MeetUpsUpdate.this, updated, Toast.LENGTH_LONG).show();
                        finish();
                        break;
                }
            }
        }.execute("updateMeetUp");
    }
}
