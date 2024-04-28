package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Generates simulated blood saturation data for patients in a healthcare monitoring system.
 * The generator simulates small fluctuations in saturation values to mimic real patient data.
 */

public class BloodSaturationDataGenerator implements PatientDataGenerator {

    /**
     * Random generator for creating random saturation variations.
     */
    private static final Random random = new Random();

    /**
     * Array to hold the last recorded saturation values for each patient.
     */
    private int[] lastSaturationValues;

    /**
     * Constructs a new BloodSaturationDataGenerator with a specific number of patients.
     * Initializes last recorded saturation values for each patient between 95 and 100 inclusive.
     *
     * @param patientCount the number of patients this generator will handle
     */
    public BloodSaturationDataGenerator(int patientCount) {
        lastSaturationValues = new int[patientCount + 1];

        // Initialize with baseline saturation values for each patient
        for (int i = 1; i <= patientCount; i++) {
            lastSaturationValues[i] = 95 + random.nextInt(6); // Initializes with a value between 95 and 100
        }
    }

    /**
     * Generates and outputs new saturation data for a specified patient.
     * Simulates small random fluctuations in blood saturation levels and ensures values stay within realistic limits.
     *
     * @param patientId the identifier of the patient for whom to generate data
     * @param outputStrategy the strategy to use for outputting generated data
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            // Simulate blood saturation values
            int variation = random.nextInt(3) - 1; // -1, 0, or 1 to simulate small fluctuations
            int newSaturationValue = lastSaturationValues[patientId] + variation;

            // Ensure the saturation stays within a realistic and healthy range
            newSaturationValue = Math.min(Math.max(newSaturationValue, 90), 100);
            lastSaturationValues[patientId] = newSaturationValue;
            outputStrategy.output(patientId, System.currentTimeMillis(), "Saturation",
                    Double.toString(newSaturationValue) + "%");
        } catch (Exception e) {
            System.err.println("An error occurred while generating blood saturation data for patient " + patientId);
            e.printStackTrace(); // This will print the stack trace to help identify where the error occurred.
        }
    }
}
