package com.example.workoutlog;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class NoviTreningFragment extends Fragment {
    String selectedDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_novi_trening, container, false);

        OdabirNovogTreningaDialogAdapter dialogAdapter = new OdabirNovogTreningaDialogAdapter(requireActivity());
        CalendarView calendarView = view.findViewById(R.id.calendarView);
        Button btnNoviTreningDialog = view.findViewById(R.id.btnNoviTreningDialog);
        long dateInMillis = calendarView.getDate();
         selectedDate = convertMillisToDate(dateInMillis);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                    String  curDate = String.valueOf(dayOfMonth);
                    String  Year = String.valueOf(year);
                    String  Month = String.valueOf(month+1);

                    selectedDate = curDate+"-"+Month+"-"+Year;

            }
        });

        btnNoviTreningDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long selectedDateMillis = calendarView.getDate();
                dialogAdapter.showDialog(selectedDate);
            }
        });

        return view;
    }
    private String convertMillisToDate(long millis) {
        // Convert millis to date string here (you can use SimpleDateFormat or other methods)
        // This is just an example, implement your conversion logic
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return sdf.format(new Date(millis));
    }
}