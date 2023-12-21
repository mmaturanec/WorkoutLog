package com.example.workoutlog;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class PotvrdiBrisanjeVjezbeDialog {
    private static AlertDialog dialog;

    public interface DialogClickListener {
        void onPositiveButtonClick(int position);
        void onNegativeButtonClick();
    }

    public static void showDeleteExerciseDialog(Context context, final DialogClickListener listener, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Inflate the custom layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.potvrdi_brisanje_vjezbe_dialog, null);
        builder.setView(dialogView);

        // Get the buttons from the custom layout
        Button obrisiButton = dialogView.findViewById(R.id.alertObrisiVj);
        Button odustaniButton = dialogView.findViewById(R.id.alertOdustaniVj);

        // Set button click listeners
        obrisiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onPositiveButtonClick(position);
                }
                dismissDeleteExerciseDialog();
            }
        });

        odustaniButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onNegativeButtonClick();
                }
                dismissDeleteExerciseDialog();
            }
        });

        // Show the dialog
        dialog = builder.create();
        dialog.show();
    }

    public static void dismissDeleteExerciseDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

}
