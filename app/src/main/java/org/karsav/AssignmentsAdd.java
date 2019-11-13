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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.r0adkll.slidr.Slidr;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class AssignmentsAdd extends AppCompatActivity {
    private static final String SETTINGS_PREF_NAME = "textSize";
    EditText assignment_name, content, category, assignments_date_due, assignments_last_time;
    AutoCompleteTextView official_name_surname;
    String official_name_surnameString, assignment_nameString, contentString, categoryString;
    String assignments_date_dueString, assignments_last_timeString;
    int id, responsibleId;
    DataBase karsav = new DataBase();
    ProgressDialog progressDialog;
    TimePickerDialog timePickerDialog;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    String fromWhom, finalTime, currentTime;
    String name, surname;
    Toolbar toolbar;
    SharedPreferences settings;
    int textSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignments_add);
        Slidr.attach(this);
        init();
    }

    private void init() {
        official_name_surname = findViewById(R.id.official_name_surname);
        assignment_name = findViewById(R.id.assignment_name);
        content = findViewById(R.id.content);
        category = findViewById(R.id.category);
        assignments_date_due = findViewById(R.id.assignments_date_due);
        assignments_last_time = findViewById(R.id.assignments_last_time);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        View logoView = toolbar.getChildAt(1);
        logoView.setOnClickListener(v -> finish());
        toolbar.setTitle(getString(R.string.add_assignments));

        settings = getSharedPreferences(SETTINGS_PREF_NAME, MODE_PRIVATE);
        textSize = settings.getInt(SETTINGS_PREF_NAME, 15);

        official_name_surname.setTextSize(textSize);
        assignment_name.setTextSize(textSize);
        content.setTextSize(textSize);
        category.setTextSize(textSize);
        assignments_date_due.setTextSize(textSize);
        assignments_last_time.setTextSize(textSize);

        fromWhom = UserAdapter.getUserName() + " " + UserAdapter.getUserSurName();

        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener getDate = (view, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "dd.MM.yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            assignments_date_due.setText(sdf.format(myCalendar.getTime()));
        };

        assignments_date_due.setOnClickListener(v -> new DatePickerDialog(AssignmentsAdd.this, getDate, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        assignments_last_time.setOnClickListener(v -> {
            calendar = Calendar.getInstance();
            currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            currentMinute = calendar.get(Calendar.MINUTE);

            timePickerDialog = new TimePickerDialog(AssignmentsAdd.this, (view, hourOfDay, minute) -> {
                String text = hourOfDay + ":" + minute;
                assignments_last_time.setText(text);
            }, currentHour, currentMinute, true);
            timePickerDialog.show();
        });
        Map map = RegisterMemberGet.getFullName();
        Collection<String> values = map.values();
        ArrayList<String> listOfValues = new ArrayList<>(values);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.list_item_for_users, R.id.users_list_item, listOfValues);
        official_name_surname.setAdapter(adapter);
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
            official_name_surnameString = official_name_surname.getText().toString();
            assignment_nameString = assignment_name.getText().toString();
            contentString = content.getText().toString();
            categoryString = category.getText().toString();
            assignments_date_dueString = assignments_date_due.getText().toString();
            assignments_last_timeString = assignments_last_time.getText().toString();
            finalTime = assignments_date_dueString + " " + assignments_last_timeString;
        } catch (Exception ex) {
            Toast.makeText(AssignmentsAdd.this, getString(R.string.mainactivity_blank), Toast.LENGTH_SHORT).show();
        }
        if (assignment_nameString.equalsIgnoreCase("null") || assignment_nameString.isEmpty()) {
            Toast.makeText(AssignmentsAdd.this, getString(R.string.name_empty), Toast.LENGTH_SHORT).show();
        } else {
            String text = getString(R.string.assignment_add_dialog_text);
            String yes = getString(R.string.assignment_add_yes);
            String no = getString(R.string.assignment_add_no);
            AlertDialog.Builder builder = new AlertDialog.Builder(AssignmentsAdd.this);
            builder.setMessage(text).setPositiveButton(yes, (dialog, which) -> update()).setNegativeButton(no, (dialog, which) -> {
            }).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void update() {
        new AsyncTask<String, String, String>() {

            final String progress_title = getString(R.string.assignment_add_progress_title);
            final String progress_message = getString(R.string.register_reference_progress_message);
            final String connection = getString(R.string.mainactivity_connection);
            final String updated = getString(R.string.assignment_added);

            protected void onPreExecute() {
                progressDialog = new ProgressDialog(AssignmentsAdd.this);
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
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                    currentTime = sdf.format(new Date());
                    String[] nameSurname = official_name_surnameString.split((" "), 3);
                    if (nameSurname.length == 2) {
                        name = nameSurname[0].trim();
                        surname = nameSurname[1].trim();
                    } else if (nameSurname.length == 3) {
                        name = nameSurname[0].trim() + " " + nameSurname[1].trim();
                        surname = nameSurname[2].trim();
                    }
                    karsav.addAssignment(assignment_nameString, categoryString, contentString, name, surname, fromWhom, finalTime, currentTime);
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
                        Toast.makeText(AssignmentsAdd.this, connection, Toast.LENGTH_SHORT).show();
                        break;

                    case "done":
                        startActivity(new Intent(AssignmentsAdd.this, HomePage.class));
                        Toast.makeText(AssignmentsAdd.this, updated, Toast.LENGTH_LONG).show();
                        finish();
                        break;
                }
            }
        }.execute("addAssignment");
    }
}
