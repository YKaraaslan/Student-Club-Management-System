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

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class AssignmentsNewShow extends AppCompatActivity implements AssignmentsNewFinishDialog.DialogListener {

    private static final String SETTINGS_PREF_NAME = "textSize";
    TextView assignments_name, assignments_creator, assignments_content, assignments_date_of_issue;
    TextView assignments_date_due;
    AssignmentsItem assignmentsItem;
    String name, creator, content, returns, date_of_issue, date_due, date_saved, ourReturns, category, dateSaved, responsibleNameSurname;
    LinearLayout linear_creator, linear_content, linear_dates;
    ProgressDialog progressDialog;
    DataBase karsav = new DataBase();
    int id, responsibleID;
    Toolbar toolbar;
    TextView creatorHeader, contentHeader, datesHeader;
    SharedPreferences settings;
    int textSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignments_new_show);
        Slidr.attach(this);
        init();
    }

    private void init() {
        assignments_name = findViewById(R.id.assignments_name);
        assignments_creator = findViewById(R.id.assignments_creator);
        assignments_content = findViewById(R.id.assignments_content);
        assignments_date_of_issue = findViewById(R.id.assignments_date_of_issue);
        assignments_date_due = findViewById(R.id.assignments_date_due);

        linear_creator = findViewById(R.id.linear_creator);
        linear_content = findViewById(R.id.linear_content);
        linear_dates = findViewById(R.id.linear_dates);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        View logoView = toolbar.getChildAt(1);
        logoView.setOnClickListener(v -> finish());

        creatorHeader = findViewById(R.id.creatorHeader);
        contentHeader = findViewById(R.id.contentHeader);
        datesHeader = findViewById(R.id.datesHeader);

        settings = getSharedPreferences(SETTINGS_PREF_NAME, MODE_PRIVATE);
        textSize = settings.getInt(SETTINGS_PREF_NAME, 15);

        assignments_name.setTextSize(textSize + 10);
        assignments_creator.setTextSize(textSize);
        assignments_content.setTextSize(textSize);
        assignments_date_of_issue.setTextSize(textSize);
        assignments_date_due.setTextSize(textSize);
        creatorHeader.setTextSize(textSize + 5);
        contentHeader.setTextSize(textSize + 5);
        datesHeader.setTextSize(textSize + 5);

        assignmentsItem = (AssignmentsItem) getIntent().getSerializableExtra("Assignments");

        name = assignmentsItem.getName().trim();
        responsibleNameSurname = assignmentsItem.getResponsibleName();
        creator = assignmentsItem.getCreator().trim();
        category = assignmentsItem.getCategory();
        content = assignmentsItem.getContent().trim();
        date_of_issue = assignmentsItem.getDateOfIssue();
        date_due = assignmentsItem.getDueDate();
        date_saved = assignmentsItem.getDateSaved();
        returns = assignmentsItem.getReturns();
        id = assignmentsItem.getId();
        responsibleID = assignmentsItem.getResponsibleId();

        if (creator.isEmpty() || creator.equalsIgnoreCase("null")) {
            linear_creator.setVisibility(View.GONE);
        }
        if (content.isEmpty() || content.equalsIgnoreCase("null")) {
            linear_content.setVisibility(View.GONE);
        }
        if (date_of_issue.isEmpty() || date_of_issue.equalsIgnoreCase("null")) {
            assignments_date_of_issue.setVisibility(View.GONE);
        }
        if (date_due.isEmpty() || date_due.equalsIgnoreCase("null")) {
            assignments_date_due.setVisibility(View.GONE);
        }

        assignments_name.setText(name);
        assignments_creator.setText(creator);
        assignments_content.setText(content);
        assignments_date_of_issue.setText(getString(R.string.given_date, date_of_issue));
        assignments_date_due.setText(getString(R.string.due_dated, date_due));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.assignments_new_show, menu);
        if (UserAdapter.getProjeKay() != null && UserAdapter.getProjeKay().equalsIgnoreCase("VAR")) {
            menu.findItem(R.id.update).setVisible(true);
            menu.findItem(R.id.delete).setVisible(true);
            menu.findItem(R.id.done).setVisible(true);
        } else if (UserAdapter.getUserId() == responsibleID || UserAdapter.getGorevKay().equalsIgnoreCase("VAR")) {
            menu.findItem(R.id.done).setVisible(true);
        } else {
            menu.findItem(R.id.update).setVisible(false);
            menu.findItem(R.id.delete).setVisible(false);
            menu.findItem(R.id.done).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.update:
                Intent intent = new Intent(AssignmentsNewShow.this, AssignmentsNewUpdate.class);
                intent.putExtra("UpdateAssignment", assignmentsItem);
                startActivity(intent);
                finish();
                break;

            case R.id.delete:
                String title = getString(R.string.assignment_delete_dialog_title);
                String text = getString(R.string.assignment_delete_dialog_message);
                String yes = getString(R.string.assignment_delete_yes);
                String no = getString(R.string.assignment_delete_no);
                AlertDialog.Builder builder = new AlertDialog.Builder(AssignmentsNewShow.this);
                builder.setTitle(title).setMessage(text).setPositiveButton(yes, (dialog, which) -> foo_delete()).setNegativeButton(no, (dialog, which) -> {

                }).show();
                break;

            case R.id.done:
                openDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openDialog() {
        AssignmentsNewFinishDialog dialog = new AssignmentsNewFinishDialog();
        dialog.show(getSupportFragmentManager(), getString(R.string.example_dialog));
    }

    @Override
    public void applyTexts(String ourReturn) {
        ourReturns = ourReturn;
        finishAssignment();
    }

    @SuppressLint("StaticFieldLeak")
    private void finishAssignment() {
        new AsyncTask<String, String, String>() {

            final String progress_title = getString(R.string.assignment_done_progress_title);
            final String progress_message = getString(R.string.assignment_progress_message);
            final String connection = getString(R.string.mainactivity_connection);
            final String finished = getString(R.string.assignment_finished);

            protected void onPreExecute() {
                progressDialog = new ProgressDialog(AssignmentsNewShow.this);
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
                    karsav.AssignmentNewDeleteFromDatabase(id);
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                    dateSaved = sdf.format(new Date());
                    karsav.AssignmentNewFinish(name, category, content, responsibleNameSurname, creator, date_due, responsibleID, date_of_issue, dateSaved, ourReturns);
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
                        Toast.makeText(AssignmentsNewShow.this, connection, Toast.LENGTH_SHORT).show();
                        break;

                    case "done":
                        startActivity(new Intent(AssignmentsNewShow.this, HomePage.class));
                        Toast.makeText(AssignmentsNewShow.this, finished, Toast.LENGTH_LONG).show();
                        finish();
                        break;
                }
            }
        }.execute("delete");
    }

    @SuppressLint("StaticFieldLeak")
    private void foo_delete() {
        new AsyncTask<String, String, String>() {

            final String progress_title = getString(R.string.assignment_delete_progress_title);
            final String progress_message = getString(R.string.assignment_progress_message);
            final String connection = getString(R.string.mainactivity_connection);
            final String deleted = getString(R.string.assignment_deleted);

            protected void onPreExecute() {
                progressDialog = new ProgressDialog(AssignmentsNewShow.this);
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
                    karsav.AssignmentNewDeleteFromDatabase(id);
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
                        Toast.makeText(AssignmentsNewShow.this, connection, Toast.LENGTH_SHORT).show();
                        break;

                    case "done":
                        startActivity(new Intent(AssignmentsNewShow.this, HomePage.class));
                        Toast.makeText(AssignmentsNewShow.this, deleted, Toast.LENGTH_LONG).show();
                        finish();
                        break;
                }
            }
        }.execute("delete");
    }
}
