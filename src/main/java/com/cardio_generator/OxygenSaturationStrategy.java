package com.cardio_generator;

import com.alerts.Alert;
import com.alerts.AlertFactory;
import com.data_management.PatientRecord;
import java.util.List;

public class OxygenSaturationStrategy implements AlertStrategy {
    @Override
    public void checkAlert(List<PatientRecord> records, AlertFactory alertFactory) {
        for (int i = 0; i < records.size(); i++) {
            PatientRecord r1 = records.get(i);

            if ("BloodSaturation".equals(r1.getRecordType())) {
                if (r1.getMeasurementValue() < 92) {
                    Alert alert = alertFactory.createAlert(String.valueOf(r1.getPatientId()), "Low Blood Saturation", r1.getTimestamp());
                    triggerAlert(alert);
                }

                if (i >= 1) {
                    PatientRecord r0 = records.get(i - 1);
                    if ("BloodSaturation".equals(r0.getRecordType())
                            && r0.getTimestamp() >= r1.getTimestamp() - 600000
                            && r0.getMeasurementValue() - r1.getMeasurementValue() >= 5) {
                        Alert alert = alertFactory.createAlert(String.valueOf(r1.getPatientId()), "Rapid Drop in Blood Saturation", r1.getTimestamp());
                        triggerAlert(alert);
                    }
                }
            }
        }
    }

    private void triggerAlert(Alert alert) {
        System.out.println("Alert Triggered: " + alert.getCondition() + " for Patient ID: " + alert.getPatientId() + " at " + alert.getTimestamp());
    }
}

