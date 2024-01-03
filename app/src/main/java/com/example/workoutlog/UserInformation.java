package com.example.workoutlog;

public class UserInformation {
    private String Ime;
    private String Prezime;
    private String Email;
    private String NodeId;
    private String ProfilePicture;

    public UserInformation(String ime, String prezime, String email, String nodeId, String profilePicture) {
        Ime = ime;
        Prezime = prezime;
        Email = email;
        NodeId = nodeId;
        ProfilePicture = profilePicture;
    }

    public UserInformation() {
        // Default constructor required for Firebase
    }
    public String getIme() {
        return Ime;
    }

    public String getPrezime() {
        return Prezime;
    }

    public String getEmail() {
        return Email;
    }



    public String getNodeId() {
        return NodeId;
    }

    public void setIme(String ime) {
        Ime = ime;
    }

    public void setPrezime(String prezime) {
        Prezime = prezime;
    }

    public void setEmail(String email) {
        Email = email;
    }


    public void setNodeId(String nodeId) {
        NodeId = nodeId;
    }

    public String getProfilePicture() {
        return ProfilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        ProfilePicture = profilePicture;
    }
}
