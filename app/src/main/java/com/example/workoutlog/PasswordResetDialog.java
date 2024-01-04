package com.example.workoutlog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordResetDialog
{
    private static AlertDialog dialog;

        public static void showInfoDialog(Context context) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            // Inflate the custom layout
            LayoutInflater inflater = LayoutInflater.from(context);
            View dialogView = inflater.inflate(R.layout.zaboravljena_lozinka_dialog, null);
            builder.setView(dialogView);

            Button btnPosalji = dialogView.findViewById(R.id.btnPosaljiReset);
            Button btnOdustani = dialogView.findViewById(R.id.btnOdustaniReset);
            EditText etEmail = dialogView.findViewById(R.id.etEmailReset);

            btnPosalji.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth auth = FirebaseAuth.getInstance();

                    String email = etEmail.getText().toString();

                    if(TextUtils.isEmpty(email))
                    {
                        Toast.makeText(context, context.getString(R.string.UnesiteEmailZaReset), Toast.LENGTH_SHORT).show();

                    }
                    else {
                        auth.sendPasswordResetEmail(email)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // Password reset email sent successfully
                                            Toast.makeText(context, context.getString(R.string.ResetEmailSucces), Toast.LENGTH_SHORT).show();
                                            dismissInfoDialog();

                                        } else {
                                            // Failed to send reset password email
                                            Toast.makeText(context, context.getString(R.string.ResetEmailFail), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    }
                }
            });
            btnOdustani.setOnClickListener(new View.OnClickListener() {
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
