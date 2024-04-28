package com.cardio_generator.outputs;

/**
 * Defines the contract for output strategies used in the health data simulation system.
 * Implementations of this interface determine how and where patient data is output,
 * enabling flexibility in how data is logged or communicated.
 */
public interface OutputStrategy {

    /**
     * Outputs the specified data associated with a patient at a given time.
     * 
     * @param patientId The identifier of the patient whose data is being output.
     * @param timestamp The timestamp (in milliseconds since Unix epoch) when the data was recorded.
     * @param label A label describing the type of data (e.g., "HeartRate", "Saturation").
     * @param data The actual data value in a string format, which could represent different types of metrics.
     */
    void output(int patientId, long timestamp, String label, String data);
}
