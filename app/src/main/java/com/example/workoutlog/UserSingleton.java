package com.example.workoutlog;

public class UserSingleton {
    private UserInformation user;
    private static UserSingleton instance = null;

    private UserSingleton() {
        // Private constructor to prevent instantiation
    }

    public static UserSingleton getInstance() {
        if (instance == null) {
            instance = new UserSingleton();
        }
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }

    public UserInformation getUser() {
        return user;
    }

    public void setUser(UserInformation user) {
        this.user = user;
    }
}
