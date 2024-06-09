package com.alerts;

public interface Alert {
    String getPatientId();
    String getCondition();
    long getTimestamp();
    String getDetails(); // Method to get additional details of the alert
}
