package com.cardio_generator;

import com.alerts.Alert;
import com.alerts.AlertFactory;
import com.data_management.PatientRecord;
import java.util.List;

public class HeartRateStrategy implements AlertStrategy {
    @Override
    public void checkAlert(List<PatientRecord> records, AlertFactory alertFactory) {
        for (PatientRecord record : records) {
            if ("HeartRate".equals(record.getRecordType())) {
                if (record.getMeasurementValue() < 60 || record.getMeasurementValue() > 100) {
                    Alert alert = alertFactory.createAlert(String.valueOf(record.getPatientId()), "Abnormal Heart Rate", record.getTimestamp());
                    triggerAlert(alert);
                }
            }
        }
    }

    private void triggerAlert(Alert alert) {
        System.out.println("Alert Triggered: " + alert.getCondition() + " for Patient ID: " + alert.getPatientId() + " at " + alert.getTimestamp());
    }
}
