package com.alerts;

import com.cardio_generator.AlertStrategy;
import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

/**
 * Generates and evaluates alerts based on patient data and alert strategies.
 */
public class AlertGenerator {
    private DataStorage dataStorage;
    private AlertStrategy alertStrategy;

    /**
     * Constructor for the AlertGenerator.
     *
     * @param dataStorage the data storage containing patient data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Sets the alert strategy to be used for evaluating alerts.
     *
     * @param alertStrategy the alert strategy
     */
    public void setAlertStrategy(AlertStrategy alertStrategy) {
        this.alertStrategy = alertStrategy;
    }

    /**
     * Evaluates patient data and generates alerts based on the specified alert factory.
     *
     * @param patient the patient whose data is to be evaluated
     * @param alertFactory the factory to create alerts
     */
    public void evaluateData(Patient patient, AlertFactory alertFactory) {
        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE);
        alertStrategy.checkAlert(records, alertFactory);
    }

    /**
     * Generates an alert with decorators for the specified patient and condition.
     *
     * @param patient the patient for whom the alert is generated
     * @param condition the condition described by the alert
     * @param timestamp the timestamp of the alert
     */
    public void generateAlert(Patient patient, String condition, long timestamp) {
        Alert basicAlert = new BasicAlert(patient.getPatientId(), condition, timestamp);
        Alert repeatedAlert = new RepeatedAlertDecorator(basicAlert, 60); // Recheck every 60 seconds
        Alert priorityAlert = new PriorityAlertDecorator(repeatedAlert, "High");

        System.out.println(priorityAlert.getDetails());

        // Trigger the recheck process
        if (repeatedAlert instanceof RepeatedAlertDecorator) {
            ((RepeatedAlertDecorator) repeatedAlert).recheckCondition();
        }
    }
}
