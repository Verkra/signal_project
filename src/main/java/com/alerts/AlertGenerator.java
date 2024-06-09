package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

public class AlertGenerator {
    private DataStorage dataStorage;

    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    public void evaluateData(Patient patient) {
        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE);

        checkBloodPressureAlerts(records);
        checkBloodSaturationAlerts(records);
        checkHypotensiveHypoxemiaAlert(records);
    }

    private void checkBloodPressureAlerts(List<PatientRecord> records) {
        // Trend Alert
        for (int i = 2; i < records.size(); i++) {
            PatientRecord r1 = records.get(i - 2);
            PatientRecord r2 = records.get(i - 1);
            PatientRecord r3 = records.get(i);

            if ("BloodPressure".equals(r1.getRecordType()) && "BloodPressure".equals(r2.getRecordType())
                    && "BloodPressure".equals(r3.getRecordType())) {
                if (r2.getMeasurementValue() - r1.getMeasurementValue() > 10
                        && r3.getMeasurementValue() - r2.getMeasurementValue() > 10) {
                    triggerAlert(new Alert(String.valueOf(r3.getPatientId()), "Increasing Blood Pressure Trend", r3.getTimestamp()));
                }

                if (r1.getMeasurementValue() - r2.getMeasurementValue() > 10
                        && r2.getMeasurementValue() - r3.getMeasurementValue() > 10) {
                    triggerAlert(new Alert(String.valueOf(r3.getPatientId()), "Decreasing Blood Pressure Trend", r3.getTimestamp()));
                }
            }
        }

        // Critical Threshold Alert
        for (PatientRecord record : records) {
            if ("BloodPressure".equals(record.getRecordType())) {
                if (record.getMeasurementValue() > 180 || record.getMeasurementValue() < 90) {
                    triggerAlert(new Alert(String.valueOf(record.getPatientId()), "Critical Blood Pressure Threshold", record.getTimestamp()));
                }
            }
        }
    }

    private void checkBloodSaturationAlerts(List<PatientRecord> records) {
        for (int i = 0; i < records.size(); i++) {
            PatientRecord r1 = records.get(i);

            if ("BloodSaturation".equals(r1.getRecordType())) {
                // Low Saturation Alert
                if (r1.getMeasurementValue() < 92) {
                    triggerAlert(new Alert(String.valueOf(r1.getPatientId()), "Low Blood Saturation", r1.getTimestamp()));
                }

                // Rapid Drop Alert
                if (i >= 1) {
                    PatientRecord r0 = records.get(i - 1);
                    if ("BloodSaturation".equals(r0.getRecordType())
                            && r0.getTimestamp() >= r1.getTimestamp() - 600000 // 10 minutes in milliseconds
                            && r0.getMeasurementValue() - r1.getMeasurementValue() >= 5) {
                        triggerAlert(new Alert(String.valueOf(r1.getPatientId()), "Rapid Drop in Blood Saturation", r1.getTimestamp()));
                    }
                }
            }
        }
    }

    private void checkHypotensiveHypoxemiaAlert(List<PatientRecord> records) {
        boolean lowBP = false;
        boolean lowOxygen = false;

        for (PatientRecord record : records) {
            if ("BloodPressure".equals(record.getRecordType()) && record.getMeasurementValue() < 90) {
                lowBP = true;
            }
            if ("BloodSaturation".equals(record.getRecordType()) && record.getMeasurementValue() < 92) {
                lowOxygen = true;
            }

            if (lowBP && lowOxygen) {
                triggerAlert(new Alert(String.valueOf(record.getPatientId()), "Hypotensive Hypoxemia Alert", record.getTimestamp()));
                break;
            }
        }
    }

    private void triggerAlert(Alert alert) {
        System.out.println("Alert Triggered: " + alert.getCondition() + " for Patient ID: " + alert.getPatientId() +
                " at " + alert.getTimestamp());
        
    }
}
