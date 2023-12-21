package com.example.workoutlog;

import android.app.AlertDialog;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class InfoODodavanjuVjezbiDialog {

    private static AlertDialog dialog;

    public static void showInfoDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Inflate the custom layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.info_o_dodavanju_vjezbi_dialog, null);
        builder.setView(dialogView);

        ImageView ivCloseDialogInfo = dialogView.findViewById(R.id.ivCloseDialogInfo);

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
