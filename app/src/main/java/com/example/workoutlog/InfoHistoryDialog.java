package com.example.workoutlog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

public class InfoHistoryDialog {
    private static AlertDialog dialog;

    public static void showInfoDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Inflate the custom layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.info_history_dialog, null);
        builder.setView(dialogView);

        ImageView ivCloseDialogInfo = dialogView.findViewById(R.id.ivCloseDialogHistory);

        ivCloseDialogInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog
                dismissInfoDialog();
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    public static void dismissInfoDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
