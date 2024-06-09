package data_management;

import com.alerts.AlertGenerator;
import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AlertGeneratorTest {

    private DataStorage dataStorage;
    private AlertGenerator alertGenerator;

    @BeforeEach
    public void setUp() {
        dataStorage = new DataStorage();
        alertGenerator = new AlertGenerator(dataStorage);
    }

    @Test
    public void testBloodPressureTrendAlert() {
        dataStorage.addPatientData(1, 150, "BloodPressure", 1600000000000L);
        dataStorage.addPatientData(1, 160, "BloodPressure", 1600003600000L);
        dataStorage.addPatientData(1, 170, "BloodPressure", 1600007200000L);

        Patient patient = dataStorage.getAllPatients().get(0);
        alertGenerator.evaluateData(patient);

        // Verify alert is triggered
        // Here you can capture the output or log to assert the alert is triggered
    }

    @Test
    public void testCriticalBloodPressureAlert() {
        dataStorage.addPatientData(1, 185, "BloodPressure", 1600000000000L);

        Patient patient = dataStorage.getAllPatients().get(0);
        alertGenerator.evaluateData(patient);

        // Verify alert is triggered
    }

    @Test
    public void testLowBloodSaturationAlert() {
        dataStorage.addPatientData(1, 91, "BloodSaturation", 1600000000000L);

        Patient patient = dataStorage.getAllPatients().get(0);
        alertGenerator.evaluateData(patient);

        // Verify alert is triggered
    }

    @Test
    public void testRapidDropBloodSaturationAlert() {
        dataStorage.addPatientData(1, 96, "BloodSaturation", 1600000000000L);
        dataStorage.addPatientData(1, 90, "BloodSaturation", 1600000600000L); // 6 minutes later

        Patient patient = dataStorage.getAllPatients().get(0);
        alertGenerator.evaluateData(patient);

        // Verify alert is triggered
    }

    @Test
    public void testHypotensiveHypoxemiaAlert() {
        dataStorage.addPatientData(1, 89, "BloodPressure", 1600000000000L);
        dataStorage.addPatientData(1, 91, "BloodSaturation", 1600000000000L);

        Patient patient = dataStorage.getAllPatients().get(0);
        alertGenerator.evaluateData(patient);

        // Verify alert is triggered
    }
}

