package com.example.workoutlog;
import android.os.Parcel;
import android.os.Parcelable;

public class ExerciseSet implements Parcelable {
    private String reps;
    private String weight;

    public ExerciseSet(){

    }
    public ExerciseSet(String reps, String weight) {
        this.reps = reps;
        this.weight = weight;
    }

    public String getReps() {
        return reps;
    }

    public String getWeight() {
        return weight;
    }

    // Parcelable implementation methods
    protected ExerciseSet(Parcel in) {
        reps = in.readString();
        weight = in.readString();
    }

    public static final Creator<ExerciseSet> CREATOR = new Creator<ExerciseSet>() {
        @Override
        public ExerciseSet createFromParcel(Parcel in) {
            return new ExerciseSet(in);
        }

        @Override
        public ExerciseSet[] newArray(int size) {
            return new ExerciseSet[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(reps);
        dest.writeString(weight);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void setReps(String reps) {
        this.reps = reps;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
