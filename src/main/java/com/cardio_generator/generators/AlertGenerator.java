package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;

public class AlertGenerator implements PatientDataGenerator {

    // Static variable names should be in UPPER_SNAKE_CASE.
    public static final Random RANDOM_GENERATOR = new Random();

    // Variable names should be in camelCase. Comment adjusted for clarity.
    private boolean[] alertStates; // false indicates resolved, true indicates pressed

    public AlertGenerator(int patientCount) {
        // Adjust array name to camelCase.
        alertStates = new boolean[patientCount + 1];
    }

    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            // Use camelCase for variable names.
            if (alertStates[patientId]) {
                if (RANDOM_GENERATOR.nextDouble() < 0.9) { // 90% chance to resolve
                    alertStates[patientId] = false;
                    // Output the alert resolution status.
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
                }
            } else {
                // Local variable names should be in camelCase.
                double lambda = 0.1; // Average rate (alerts per period), adjust based on desired frequency
                double probability = -Math.expm1(-lambda); // Probability of at least one alert in the period
                boolean alertTriggered = RANDOM_GENERATOR.nextDouble() < probability;

                if (alertTriggered) {
                    alertStates[patientId] = true;
                    // Output the alert triggered status.
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
                }
            }
        } catch (Exception e) {
            // Improved error handling message with detailed information.
            System.err.printf("An error occurred while generating alert data for patient %d: %s%n", patientId, e.getMessage());
            e.printStackTrace();
        }
    }
}

