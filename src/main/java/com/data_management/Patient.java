package com.data_management;

import java.util.ArrayList;
import java.util.List;

public class Patient {
    private int patientId;
    private List<PatientRecord> patientRecords;

    public Patient(int patientId) {
        this.patientId = patientId;
        this.patientRecords = new ArrayList<>();
    }

    public void addRecord(double measurementValue, String recordType, long timestamp) {
        PatientRecord record = new PatientRecord(this.patientId, measurementValue, recordType, timestamp);
        this.patientRecords.add(record);
    }

    public List<PatientRecord> getRecords(long startTime, long endTime, String recordType) {
        List<PatientRecord> filteredRecords = new ArrayList<>();
        for (PatientRecord record : patientRecords) {
            if (record.getTimestamp() >= startTime && record.getTimestamp() <= endTime && record.getRecordType().equals(recordType)) {
                filteredRecords.add(record);
            }
        }
        return filteredRecords;
    }

    public List<PatientRecord> getRecords(long startTime, long endTime) {
        List<PatientRecord> filteredRecords = new ArrayList<>();
        for (PatientRecord record : patientRecords) {
            if (record.getTimestamp() >= startTime && record.getTimestamp() <= endTime) {
                filteredRecords.add(record);
            }
        }
        return filteredRecords;
    }

    public List<PatientRecord> getRecords() {
        return patientRecords;
    }

    public String getPatientId() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPatientId'");
    }
}