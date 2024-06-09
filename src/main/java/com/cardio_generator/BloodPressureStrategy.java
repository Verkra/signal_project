package com.cardio_generator;

import com.alerts.Alert;
import com.alerts.AlertFactory;
import com.data_management.PatientRecord;
import java.util.List;

public class BloodPressureStrategy implements AlertStrategy {
    @Override
    public void checkAlert(List<PatientRecord> records, AlertFactory alertFactory) {
        for (int i = 2; i < records.size(); i++) {
            PatientRecord r1 = records.get(i - 2);
            PatientRecord r2 = records.get(i - 1);
            PatientRecord r3 = records.get(i);

            if ("BloodPressure".equals(r1.getRecordType()) && "BloodPressure".equals(r2.getRecordType())
                    && "BloodPressure".equals(r3.getRecordType())) {
                if (r2.getMeasurementValue() - r1.getMeasurementValue() > 10
                        && r3.getMeasurementValue() - r2.getMeasurementValue() > 10) {
                    Alert alert = alertFactory.createAlert(String.valueOf(r3.getPatientId()), "Increasing Blood Pressure Trend", r3.getTimestamp());
                    triggerAlert(alert);
                }

                if (r1.getMeasurementValue() - r2.getMeasurementValue() > 10
                        && r2.getMeasurementValue() - r3.getMeasurementValue() > 10) {
                    Alert alert = alertFactory.createAlert(String.valueOf(r3.getPatientId()), "Decreasing Blood Pressure Trend", r3.getTimestamp());
                    triggerAlert(alert);
                }
            }
        }

        for (PatientRecord record : records) {
            if ("BloodPressure".equals(record.getRecordType())) {
                if (record.getMeasurementValue() > 180 || record.getMeasurementValue() < 90) {
                    Alert alert = alertFactory.createAlert(String.valueOf(record.getPatientId()), "Critical Blood Pressure Threshold", record.getTimestamp());
                    triggerAlert(alert);
                }
            }
        }
    }

    private void triggerAlert(Alert alert) {
        System.out.println("Alert Triggered: " + alert.getCondition() + " for Patient ID: " + alert.getPatientId() + " at " + alert.getTimestamp());
    }
}

