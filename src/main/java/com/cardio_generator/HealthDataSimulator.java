package com.cardio_generator;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.alerts.AlertFactory;
import com.alerts.AlertGenerator;
import com.alerts.BloodOxygenAlertFactory;
import com.alerts.BloodPressureAlertFactory;
import com.alerts.ECGAlertFactory;
import com.cardio_generator.outputs.*;
import com.cardio_generator.generators.BloodPressureDataGenerator;
import com.cardio_generator.generators.BloodSaturationDataGenerator;
import com.cardio_generator.generators.BloodLevelsDataGenerator;
import com.cardio_generator.generators.ECGDataGenerator;
// import com.cardio_generator.outputs.ConsoleOutputStrategy;
// import com.cardio_generator.outputs.FileOutputStrategy;
// import com.cardio_generator.outputs.OutputStrategy;
// import com.cardio_generator.outputs.TcpOutputStrategy;
// import com.cardio_generator.outputs.WebSocketOutputStrategy;
import com.data_management.DataStorage;
import com.data_management.Patient;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Simulates the generation and dispatch of health data for multiple patients.
 * This simulation can output the data to various output strategies like console, file, WebSocket, or TCP.
 */

public class HealthDataSimulator {

    private static int patientCount = 50; // Default number of patients
    private static ScheduledExecutorService scheduler;
    private static OutputStrategy outputStrategy = new ConsoleOutputStrategy(); // Default output strategy
    private static final Random random = new Random();
    private static DataStorage dataStorage = DataStorage.getInstance();
    private static AlertGenerator alertGenerator = new AlertGenerator(dataStorage);

    private static HealthDataSimulator instance;

    private HealthDataSimulator() {
        // Private constructor to prevent instantiation
    }

    public static synchronized HealthDataSimulator getInstance() {
        if (instance == null) {
            instance = new HealthDataSimulator();
        }
        return instance;
    }

    /**
     * The main method to start the simulation.
     * Parses command line arguments to configure the simulation and initializes the scheduling of tasks.
     *
     * @param args the command line arguments
     * @throws IOException if an input or output operation is failed or interpreted
     */

    public static void main(String[] args) throws IOException {

        HealthDataSimulator simulator = HealthDataSimulator.getInstance();
        simulator.parseArguments(args);

        scheduler = Executors.newScheduledThreadPool(patientCount * 4);

        List<Integer> patientIds = initializePatientIds(patientCount);
        Collections.shuffle(patientIds); // Randomize the order of patient IDs

        scheduleTasksForPatients(patientIds);
    }

     /**
     * Parses the provided command-line arguments to set application configurations.
     *
     * @param args the array of command-line arguments
     * @throws IOException if an error occurs during output directory creation
     */
    private void parseArguments(String[] args) throws IOException {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-h":
                    printHelp();
                    System.exit(0);
                    break;
                case "--patient-count":
                    if (i + 1 < args.length) {
                        try {
                            patientCount = Integer.parseInt(args[++i]);
                        } catch (NumberFormatException e) {
                            System.err.println("Error: Invalid number of patients. Using default value: " + patientCount);
                        }
                    }
                    break;
                case "--output":
                    if (i + 1 < args.length) {
                        String outputArg = args[++i];
                        if (outputArg.equals("console")) {
                            outputStrategy = new ConsoleOutputStrategy();
                        } else if (outputArg.startsWith("file:")) {
                            String baseDirectory = outputArg.substring(5);
                            Path outputPath = Paths.get(baseDirectory);
                            if (!Files.exists(outputPath)) {
                                Files.createDirectories(outputPath);
                            }
                            outputStrategy = new FileOutputStrategy(baseDirectory);
                        } else if (outputArg.startsWith("websocket:")) {
                            try {
                                int port = Integer.parseInt(outputArg.substring(10));
                                // Initialize your WebSocket output strategy here
                                outputStrategy = new WebSocketOutputStrategy(port);
                                System.out.println("WebSocket output will be on port: " + port);
                            } catch (NumberFormatException e) {
                                System.err.println("Invalid port for WebSocket output. Please specify a valid port number.");
                            }
                        } else if (outputArg.startsWith("tcp:")) {
                            try {
                                int port = Integer.parseInt(outputArg.substring(4));
                                // Initialize your TCP socket output strategy here
                                outputStrategy = new TcpOutputStrategy(port);
                                System.out.println("TCP socket output will be on port: " + port);
                            } catch (NumberFormatException e) {
                                System.err.println("Invalid port for TCP output. Please specify a valid port number.");
                            }
                        } else {
                            System.err.println("Unknown output type. Using default (console).");
                        }
                    }
                    break;
                default:
                    System.err.println("Unknown option '" + args[i] + "'");
                    printHelp();
                    System.exit(1);
            }
        }
    }

    /**
     * Prints usage help for the application.
     */
    private void printHelp() {
        System.out.println("Usage: java HealthDataSimulator [options]");
        System.out.println("Options:");
        System.out.println("  -h                       Show help and exit.");
        System.out.println("  --patient-count <count>  Specify the number of patients to simulate data for (default: 50).");
        System.out.println("  --output <type>          Define the output method. Options are:");
        System.out.println("                             'console' for console output,");
        System.out.println("                             'file:<directory>' for file output,");
        System.out.println("                             'websocket:<port>' for WebSocket output,");
        System.out.println("                             'tcp:<port>' for TCP socket output.");
        System.out.println("Example:");
        System.out.println("  java HealthDataSimulator --patient-count 100 --output websocket:8080");
        System.out.println("  This command simulates data for 100 patients and sends the output to WebSocket clients connected to port 8080.");
    }

    /**
     * Initializes a list of patient IDs for the given count.
     *
     * @param patientCount the number of patients
     * @return a list of initialized patient IDs
     */

    private static List<Integer> initializePatientIds(int patientCount) {
        List<Integer> patientIds = new ArrayList<>();
        for (int i = 1; i <= patientCount; i++) {
            patientIds.add(i);
        }
        return patientIds;
    }

    /**
     * Schedules the generation and dispatch tasks for each patient at fixed intervals.
     *
     * @param patientIds the list of patient IDs
     */
    private static void scheduleTasksForPatients(List<Integer> patientIds) {
        ECGDataGenerator ecgDataGenerator = new ECGDataGenerator(patientCount);
        BloodSaturationDataGenerator bloodSaturationDataGenerator = new BloodSaturationDataGenerator(patientCount);
        BloodPressureDataGenerator bloodPressureDataGenerator = new BloodPressureDataGenerator(patientCount);
        BloodLevelsDataGenerator bloodLevelsDataGenerator = new BloodLevelsDataGenerator(patientCount);
        com.cardio_generator.generators.AlertGenerator alertGenerator = new com.cardio_generator.generators.AlertGenerator(patientCount);

        for (int patientId : patientIds) {
            scheduleTask(() -> ecgDataGenerator.generate(patientId, outputStrategy), 1, TimeUnit.SECONDS);
            scheduleTask(() -> bloodSaturationDataGenerator.generate(patientId, outputStrategy), 1, TimeUnit.SECONDS);
            scheduleTask(() -> bloodPressureDataGenerator.generate(patientId, outputStrategy), 1, TimeUnit.MINUTES);
            scheduleTask(() -> bloodLevelsDataGenerator.generate(patientId, outputStrategy), 2, TimeUnit.MINUTES);
            scheduleTask(() -> alertGenerator.generate(patientId, outputStrategy), 20, TimeUnit.SECONDS);

            // Schedule the alert evaluation using different strategies
            scheduleTask(() -> evaluateAlerts(patientId, new BloodPressureStrategy(), new BloodPressureAlertFactory()), 1, TimeUnit.MINUTES);
            scheduleTask(() -> evaluateAlerts(patientId, new HeartRateStrategy(), new ECGAlertFactory()), 1, TimeUnit.MINUTES);
            scheduleTask(() -> evaluateAlerts(patientId, new OxygenSaturationStrategy(), new BloodOxygenAlertFactory()), 1, TimeUnit.MINUTES);
        }
    }

     /**
     * Schedules a recurring task for execution.
     *
     * @param task the task to be scheduled
     * @param period the period between successive executions
     * @param timeUnit the time unit of the period
     */
    private static void scheduleTask(Runnable task, long period, TimeUnit timeUnit) {
        scheduler.scheduleAtFixedRate(task, random.nextInt(5), period, timeUnit);
    }

    /**
     * Evaluates alerts for a given patient using the specified strategy and alert factory.
     *
     * @param patientId the identifier of the patient
     * @param strategy the alert strategy to use
     * @param alertFactory the alert factory to use
     */
    private static void evaluateAlerts(int patientId, AlertStrategy strategy, AlertFactory alertFactory) {
        alertGenerator.setAlertStrategy(strategy);
        Patient patient = dataStorage.getPatient(patientId);
        if (patient != null) {
            alertGenerator.evaluateData(patient, alertFactory);
        }
    }
}
