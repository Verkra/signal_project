package com.data_management;

public class PatientRecord {
    private int patientId;
    private String recordType;
    private double measurementValue;
    private long timestamp;

    public PatientRecord(int patientId, double measurementValue, String recordType, long timestamp) {
        this.patientId = patientId;
        this.measurementValue = measurementValue;
        this.recordType = recordType;
        this.timestamp = timestamp;
    }

    public int getPatientId() {
        return patientId;
    }

    public double getMeasurementValue() {
        return measurementValue;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getRecordType() {
        return recordType;
    }
}