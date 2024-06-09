package com.data_management;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import com.alerts.AlertGenerator;

public class DataStorage {
    private static final Logger logger = Logger.getLogger(DataStorage.class.getName());
    private ConcurrentHashMap<Integer, Patient> patientMap;

    public DataStorage() {
        this.patientMap = new ConcurrentHashMap<>();
    }

    public void addPatientData(int patientId, double measurementValue, String recordType, long timestamp) {
        if (patientId <= 0 || timestamp <= 0) {
            logger.warning("Invalid patient data: patientId=" + patientId + ", timestamp=" + timestamp);
            return;
        }

        logger.info("Adding data for patientId=" + patientId + ", timestamp=" + timestamp + ", recordType=" + recordType + ", measurementValue=" + measurementValue);
        Patient patient = patientMap.get(patientId);
        if (patient == null) {
            patient = new Patient(patientId);
            patientMap.put(patientId, patient);
        }
        patient.addRecord(measurementValue, recordType, timestamp);
    }

    public List<PatientRecord> getRecords(int patientId, long startTime, long endTime, String recordType) {
        Patient patient = patientMap.get(patientId);
        if (patient != null) {
            return patient.getRecords(startTime, endTime, recordType);
        }
        return new ArrayList<>();
    }

    public List<Patient> getAllPatients() {
        return new ArrayList<>(patientMap.values());
    }

    public static void main(String[] args) {
        DataStorage storage = new DataStorage();
        WebSocketDataReader reader = new WebSocketDataReader(URI.create("ws://localhost:8080"), storage);
        reader.startReading(storage);

        List<PatientRecord> records = storage.getRecords(1, 1700000000000L, 1800000000000L, "HeartRate");
        for (PatientRecord record : records) {
            System.out.println("Record for Patient ID: " + record.getPatientId() +
                    ", Type: " + record.getRecordType() +
                    ", Data: " + record.getMeasurementValue() +
                    ", Timestamp: " + record.getTimestamp());
        }

        AlertGenerator alertGenerator = new AlertGenerator(storage);

        for (Patient patient : storage.getAllPatients()) {
            alertGenerator.evaluateData(patient);
        }
    }
}
