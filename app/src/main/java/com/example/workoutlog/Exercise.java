package com.example.workoutlog;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class Exercise implements Parcelable {
    private String exerciseName;
    private ArrayList<ExerciseSet> exerciseSets;

    public Exercise() {

    }

    public Exercise(String exerciseName, ArrayList<ExerciseSet> exerciseSets) {
        this.exerciseName = exerciseName;
        this.exerciseSets = exerciseSets;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public ArrayList<ExerciseSet> getExerciseSets() {
        return exerciseSets;
    }

    // Parcelable implementation
    protected Exercise(Parcel in) {
        exerciseName = in.readString();
        exerciseSets = in.createTypedArrayList(ExerciseSet.CREATOR);
    }

    public static final Creator<Exercise> CREATOR = new Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel in) {
            return new Exercise(in);
        }

        @Override
        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(exerciseName);
        dest.writeTypedList(exerciseSets);
    }
}
