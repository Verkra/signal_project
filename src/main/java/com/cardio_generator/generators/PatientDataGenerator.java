package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Defines a contract for generating patient data within a healthcare monitoring system.
 * Implementing classes are expected to generate specific types of data for patients,
 * such as heart rate, blood saturation, or other metrics, and use an output strategy
 * to handle the results.
 */

public interface PatientDataGenerator {

    /**
     * Generates and outputs new data for a specified patient.
     *
     * @param patientId the identifier of the patient for whom to generate data
     * @param outputStrategy the strategy to use for outputting generated data
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}
