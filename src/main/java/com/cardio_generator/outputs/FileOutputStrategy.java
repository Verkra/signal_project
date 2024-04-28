package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implements the OutputStrategy interface to write output data to files.
 * Each type of data (label) is written to a separate file within the specified base directory.
 */

// changed class name to UpperCamelCase from "fileOutputStrategy"
public class FileOutputStrategy implements OutputStrategy {
    // changed the variable name to camelCase "baseDirectory" from "BaseDirectory"
    private String baseDirectory;
    // fileMap ramins fileMap since its a final, but not static variable"
    public final ConcurrentHashMap<String, String> fileMap = new ConcurrentHashMap<>();

    /**
     * Constructs a new FileOutputStrategy with a specific base directory for file outputs.
     *
     * @param baseDirectory The directory where output files will be created.
     */
    // changed the constructor name to UpperCamelCase from "fileOutputStrategy"
    public FileOutputStrategy(String baseDirectory) {

        this.baseDirectory = baseDirectory;
    }

    /**
     * Outputs data to files, organizing it by label into separate files.
     * Ensures that each file is created if it does not exist and appends data to the file.
     *
     * @param patientId The patient's identifier.
     * @param timestamp The timestamp of the data record.
     * @param label The type of data, which also dictates the filename.
     * @param data The actual data to be outputted.
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Create the directory
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }
        // Chenge the name from "FilePath" to camelCase "filePath". Set the filePath variable
        
        // Get the file path from the map or compute a new one if absent
        String filePath = fileMap.computeIfAbsent(label, k -> Paths.get(baseDirectory, label + ".txt").toString());

        // Write the data to the file
        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
        } catch (Exception e) {
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
        }
    }
}