package com.example.mobileprogrammingproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class TransactionDeleteDialogFragment extends DialogFragment {

    public interface ConfirmationListener {
        void onConfirmDelete();
        void onCancel();
    }

    private ConfirmationListener listener;

    public void setListener(ConfirmationListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirm Deletion")
               .setMessage("Are you sure you want to delete this transaction?")
               .setPositiveButton("Yes, Delete", (dialog, id) -> {
                   if (listener != null) listener.onConfirmDelete();
               })
               .setNegativeButton("Cancel", (dialog, id) -> {
                   if (listener != null) listener.onCancel();
                   dismiss();
               });
        return builder.create();
    }
}
