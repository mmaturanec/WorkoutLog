package com.example.workoutlog;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OdabirNovogTreningaDialogAdapter {
    private final Activity activity;
    private final Dialog dialog;

    public OdabirNovogTreningaDialogAdapter(Activity activity) {
        this.activity = activity;
        dialog = new Dialog(activity);
    }

    public void showDialog(String selectedDate) {
        dialog.setContentView(R.layout.odaberi_novi_trening_dialog);
        //String selectedDate = convertMillisToDate(selectedDateMillis);
        //Log.d("SelectedDate", selectedDate);
        Button btnOdaberiPredlozak = dialog.findViewById(R.id.btnOdaberiPredlozak);
        Button btnOdaberiNoviTrening = dialog.findViewById(R.id.btnOdaberiNoviTrening);
        Button btnOdustaniOdabir = dialog.findViewById(R.id.btnOdustaniOdabir);

        btnOdaberiPredlozak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start OdabirPredloskaActivity
                dialog.dismiss();
                Intent intent = new Intent(activity, OdabirPredloskaActivity.class);
                intent.putExtra("datum", selectedDate);
                intent.putExtra("type", "noviTreningClean");
                activity.startActivity(intent);
            }
        });

        btnOdaberiNoviTrening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start NewTemplateMain Activity
                dialog.dismiss();
                Intent intent = new Intent(activity, NewTemplateMain.class);
                intent.putExtra("datum", selectedDate);
                intent.putExtra("type", "noviTreningClean");
                activity.startActivity(intent);
            }
        });

        btnOdustaniOdabir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
