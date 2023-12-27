package com.example.workoutlog;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class PotvrdiBrisanjeTemplateDialog {
    private static AlertDialog dialog;

    public interface DialogClickListener {
        void onNegativeButtonClick();
    }

    public static void showDialog(Context context, final PotvrdiBrisanjeTemplateDialog.DialogClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Inflate the custom layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.potvrdi_odabcivanje_template_dialog, null);
        builder.setView(dialogView);

        // Get the buttons from the custom layout
        Button obrisiButton = dialogView.findViewById(R.id.btnOdustaniOdabir);
        Button odustaniButton = dialogView.findViewById(R.id.alertOdustaniTemplate);

        // Set button click listeners
        obrisiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExerciseSingleton.destroyInstance();
                Intent intentMainmenu = new Intent(context, MainMenuActivity.class);
                intentMainmenu.putExtra("INITIAL_FRAGMENT", 1);
                context.startActivity(intentMainmenu);
                dismissDialog();
                if (listener != null) {
                    listener.onNegativeButtonClick();
                }
            }
        });

        odustaniButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });

        // Show the dialog
        dialog = builder.create();
        dialog.show();
    }

    public static void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
