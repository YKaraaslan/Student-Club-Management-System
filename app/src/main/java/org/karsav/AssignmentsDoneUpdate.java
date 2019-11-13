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
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class AssignmentsDoneUpdate extends AppCompatActivity {
    private static final String SETTINGS_PREF_NAME = "textSize";
    EditText official_name_surname, assignment_name, content, category, assignments_date_of_issue, assignments_date_due;
    EditText assignments_date_saved, assignments_returns, assignments_last_time;
    String official_name_surnameString, assignment_nameString, contentString, categoryString, assignments_date_of_issueString;
    String assignments_date_savedString, assignments_date_dueString, assignments_returnsString, assignments_last_timeString;
    int id, responsibleId;
    DataBase karsav = new DataBase();
    ProgressDialog progressDialog;
    TimePickerDialog timePickerDialog;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    String userName, finalTime, creator;
    AssignmentsItem assignmentsItem;
    Toolbar toolbar;
    SharedPreferences settings;
    int textSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignments_done_update);
        Slidr.attach(this);
        init();
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        official_name_surname = findViewById(R.id.official_name_surname);
        assignment_name = findViewById(R.id.assignment_name);
        content = findViewById(R.id.content);
        category = findViewById(R.id.category);
        assignments_date_of_issue = findViewById(R.id.assignments_date_of_issue);
        assignments_date_due = findViewById(R.id.assignments_date_due);
        assignments_date_saved = findViewById(R.id.assignments_date_saved);
        assignments_last_time = findViewById(R.id.assignments_last_time);
        assignments_returns = findViewById(R.id.assignments_returns);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        View logoView = toolbar.getChildAt(1);
        logoView.setOnClickListener(v -> finish());

        settings = getSharedPreferences(SETTINGS_PREF_NAME, MODE_PRIVATE);
        textSize = settings.getInt(SETTINGS_PREF_NAME, 15);

        official_name_surname.setTextSize(textSize);
        assignment_name.setTextSize(textSize);
        content.setTextSize(textSize);
        category.setTextSize(textSize);
        assignments_date_of_issue.setTextSize(textSize);
        assignments_date_due.setTextSize(textSize);
        assignments_date_saved.setTextSize(textSize);
        assignments_last_time.setTextSize(textSize);
        assignments_returns.setTextSize(textSize);

        userName = UserAdapter.getUserName() + " " + UserAdapter.getUserSurName();

        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener getDate = (view, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "dd.MM.yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            assignments_date_of_issue.setText(sdf.format(myCalendar.getTime()));
        };

        assignments_date_of_issue.setOnClickListener(v -> new DatePickerDialog(AssignmentsDoneUpdate.this, getDate, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        final Calendar myCalendarTwo = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener getDateTwo = (view, year, month, dayOfMonth) -> {
            myCalendarTwo.set(Calendar.YEAR, year);
            myCalendarTwo.set(Calendar.MONTH, month);
            myCalendarTwo.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "dd.MM.yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            assignments_date_due.setText(sdf.format(myCalendarTwo.getTime()));
        };

        assignments_date_due.setOnClickListener(v -> new DatePickerDialog(AssignmentsDoneUpdate.this, getDateTwo, myCalendarTwo.get(Calendar.YEAR), myCalendarTwo.get(Calendar.MONTH),
                myCalendarTwo.get(Calendar.DAY_OF_MONTH)).show());

        final Calendar myCalendarThree = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener getDateThree = (view, year, month, dayOfMonth) -> {
            myCalendarThree.set(Calendar.YEAR, year);
            myCalendarThree.set(Calendar.MONTH, month);
            myCalendarThree.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "dd.MM.yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            assignments_date_saved.setText(sdf.format(myCalendarThree.getTime()));
        };

        assignments_date_saved.setOnClickListener(v -> new DatePickerDialog(AssignmentsDoneUpdate.this, getDateThree, myCalendarThree.get(Calendar.YEAR), myCalendarThree.get(Calendar.MONTH),
                myCalendarThree.get(Calendar.DAY_OF_MONTH)).show());

        assignments_last_time.setOnClickListener(v -> {
            calendar = Calendar.getInstance();
            currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            currentMinute = calendar.get(Calendar.MINUTE);

            timePickerDialog = new TimePickerDialog(AssignmentsDoneUpdate.this, (view, hourOfDay, minute) -> {
                String text = hourOfDay + ":" + minute;
                assignments_last_time.setText(text);
            }, currentHour, currentMinute, true);
            timePickerDialog.show();
        });

        assignmentsItem = (AssignmentsItem) getIntent().getSerializableExtra("UpdateAssignment");

        id = assignmentsItem.getId();
        responsibleId = assignmentsItem.getResponsibleId();
        content.setText(assignmentsItem.getContent());

        official_name_surname.setText(assignmentsItem.getResponsibleName());
        assignment_name.setText(assignmentsItem.getName());
        content.setText(assignmentsItem.getContent());
        category.setText(assignmentsItem.getCategory());
        assignments_date_of_issue.setText(assignmentsItem.getDateOfIssue());
        assignments_date_due.setText(assignmentsItem.getDueDate());
        assignments_date_saved.setText(assignmentsItem.getDateSaved());
        assignments_returns.setText(assignmentsItem.getReturns());
        assignments_last_time.setText("19:23");
        creator = assignmentsItem.getCreator();
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
            assignments_date_of_issueString = assignments_date_of_issue.getText().toString();
            assignments_date_dueString = assignments_date_due.getText().toString();
            assignments_date_savedString = assignments_date_saved.getText().toString();
            assignments_returnsString = assignments_returns.getText().toString();
            assignments_last_timeString = assignments_last_time.getText().toString();
            finalTime = assignments_date_dueString + assignments_last_timeString;
        } catch (Exception ex) {
            Toast.makeText(AssignmentsDoneUpdate.this, getString(R.string.mainactivity_blank), Toast.LENGTH_SHORT).show();
        }
        if (assignment_nameString.equalsIgnoreCase("null") || assignment_nameString.isEmpty()) {
            Toast.makeText(AssignmentsDoneUpdate.this, getString(R.string.name_empty), Toast.LENGTH_SHORT).show();
        } else {
            String text = getString(R.string.assignment_update_dialog_text);
            String yes = getString(R.string.assignment_add_yes);
            String no = getString(R.string.assignment_add_no);
            AlertDialog.Builder builder = new AlertDialog.Builder(AssignmentsDoneUpdate.this);
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
            final String updated = getString(R.string.assignment_updated);

            protected void onPreExecute() {
                progressDialog = new ProgressDialog(AssignmentsDoneUpdate.this);
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
                    karsav.updateAssignment(id, assignment_nameString, categoryString, contentString, official_name_surnameString, creator, assignments_date_dueString,
                            responsibleId, assignments_date_of_issueString, assignments_date_savedString, assignments_returnsString);
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
                        Toast.makeText(AssignmentsDoneUpdate.this, connection, Toast.LENGTH_SHORT).show();
                        break;

                    case "done":
                        startActivity(new Intent(AssignmentsDoneUpdate.this, HomePage.class));
                        Toast.makeText(AssignmentsDoneUpdate.this, updated, Toast.LENGTH_LONG).show();
                        finish();
                        break;
                }
            }
        }.execute("updateAssignment");
    }
}
