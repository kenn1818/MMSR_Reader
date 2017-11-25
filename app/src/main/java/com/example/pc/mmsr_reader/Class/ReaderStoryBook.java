package com.example.pc.mmsr_reader.Class;

/**
 * Created by pc on 11/24/2017.
 */

public class ReaderStoryBook {
    public String userID;
    public String storybookID;
    public String dateStartedReading;
    public String dateFinishedReading;
    public double rateValue;

    public ReaderStoryBook() {
    }

    public ReaderStoryBook(String userID, String storybookID, String dateStartedReading, String dateFinishedReading, double rateValue) {
        this.userID = userID;
        this.storybookID = storybookID;
        this.dateStartedReading = dateStartedReading;
        this.dateFinishedReading = dateFinishedReading;
        this.rateValue = rateValue;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getStorybookID() {
        return storybookID;
    }

    public void setStorybookID(String storybookID) {
        this.storybookID = storybookID;
    }

    public String getDateStartedReading() {
        return dateStartedReading;
    }

    public void setDateStartedReading(String dateStartedReading) {
        this.dateStartedReading = dateStartedReading;
    }

    public String getDateFinishedReading() {
        return dateFinishedReading;
    }

    public void setDateFinishedReading(String dateFinishedReading) {
        this.dateFinishedReading = dateFinishedReading;
    }

    public double getRateValue() {
        return rateValue;
    }

    public void setRateValue(double rateValue) {
        this.rateValue = rateValue;
    }
}
