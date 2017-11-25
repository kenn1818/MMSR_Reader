package com.example.pc.mmsr_reader.Class;

/**
 * Created by pc on 11/24/2017.
 */

public class Reader {
    public String userID;
    public String userName;
    public String password;
    public String userDOB;
    public String email;
    public int points;

    public Reader(){

    }

    public Reader(String userID, String userName, String password, String userDOB, String email, int points) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
        this.userDOB = userDOB;
        this.email = email;
        this.points = points;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserDOB() {
        return userDOB;
    }

    public void setUserDOB(String userDOB) {
        this.userDOB = userDOB;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
