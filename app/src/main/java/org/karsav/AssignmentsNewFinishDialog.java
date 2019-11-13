package org.karsav;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class AssignmentsNewFinishDialog extends AppCompatDialogFragment {
    private TextInputEditText returns;
    private DialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.dialog_layout, null);
        String title = getString(R.string.assignment_done_dialog_title);
        String yes = getString(R.string.assignment_done_yes);
        String no = getString(R.string.assignment_done_no);
        builder.setView(view).setMessage(title).setNegativeButton(no, (dialogInterface, i) -> {
        })
                .setPositiveButton(yes, (dialogInterface, i) -> {
                    String ourReturn = Objects.requireNonNull(returns.getText()).toString();
                    listener.applyTexts(ourReturn);
                });

        returns = view.findViewById(R.id.returns);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    public interface DialogListener {
        void applyTexts(String ourReturn);
    }
}