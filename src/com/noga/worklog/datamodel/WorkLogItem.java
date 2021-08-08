package com.noga.worklog.datamodel;

import javafx.scene.control.TextArea;

import java.time.LocalDate;

public class WorkLogItem {
    private LocalDate date;
    private double hours;
    private double wages;
    private double wagesTimesHours;
    private String notes;

    public WorkLogItem(LocalDate date, double hours, double wages, String notes) {
        this.date = date;
        this.hours = hours;
        this.wages = wages;
        this.notes = notes;
        this.wagesTimesHours = wages * hours;
    }

    public WorkLogItem(LocalDate date, double hours, double wages) {
        this.date = date;
        this.hours = hours;
        this.wages = wages;
        this.wagesTimesHours = wages * hours;
        this.notes = "";
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getHours() {
        return hours;
    }

    public void setHours(double hours) {
        this.hours = hours;
    }

    public double getWages() {
        return wages;
    }

    public double getWagesTimesHours() {
        return wagesTimesHours;
    }

    public void setWages(double wages) {
        this.wages = wages;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
