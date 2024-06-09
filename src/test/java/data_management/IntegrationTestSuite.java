package data_management;

import com.alerts.AlertGenerator;
import com.data_management.*;
import com.cardio_generator.generators.*;
import com.cardio_generator.outputs.OutputStrategy;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;


import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class IntegrationTestSuite {

    private static final int PATIENT_COUNT = 10;
    private DataStorage dataStorage;
    private WebSocketClient mockClient;
    private OutputStrategy mockOutputStrategy;
    private CustomWebSocketClient webSocketClient;

    @BeforeEach
    public void setup() throws Exception {
        dataStorage = new DataStorage();
        mockOutputStrategy = mock(OutputStrategy.class);
        webSocketClient = new CustomWebSocketClient(URI.create("ws://localhost:8080"), dataStorage);
        mockClient = Mockito.spy(webSocketClient);
    }

    @Test
    public void testDataGenerationAndStorage() throws Exception {
        // Initialize data generators
        BloodPressureDataGenerator bpGenerator = new BloodPressureDataGenerator(PATIENT_COUNT);
        BloodSaturationDataGenerator bsGenerator = new BloodSaturationDataGenerator(PATIENT_COUNT);
        ECGDataGenerator ecgGenerator = new ECGDataGenerator(PATIENT_COUNT);
        

        // Generate data for each patient
        for (int i = 1; i <= PATIENT_COUNT; i++) {
            bpGenerator.generate(i, mockOutputStrategy);
            bsGenerator.generate(i, mockOutputStrategy);
            ecgGenerator.generate(i, mockOutputStrategy);
            
        }

        // Verify output strategy calls
        verify(mockOutputStrategy, atLeast(1)).output(anyInt(), anyLong(), anyString(), anyString());

        // Simulate data reception via WebSocket
        for (int i = 1; i <= PATIENT_COUNT; i++) {
            webSocketClient.onMessage("1,1623456789000,BloodPressure,120.0");
            webSocketClient.onMessage("1,1623456789000,BloodSaturation,98.0");
            webSocketClient.onMessage("1,1623456789000,ECG,0.5");
        }

        // Validate data storage
        assertEquals(PATIENT_COUNT, dataStorage.getAllPatients().size());
        for (int i = 1; i <= PATIENT_COUNT; i++) {
            assertTrue(dataStorage.getRecords(i, 0, Long.MAX_VALUE, "BloodPressure").size() > 0);
            assertTrue(dataStorage.getRecords(i, 0, Long.MAX_VALUE, "BloodSaturation").size() > 0);
            assertTrue(dataStorage.getRecords(i, 0, Long.MAX_VALUE, "ECG").size() > 0);
        }
    }

    @Test
    public void testAlertGeneration() {
        AlertGenerator alertGenerator = new AlertGenerator(dataStorage);

        // Simulate data reception that should trigger alerts
        webSocketClient.onMessage("1,1623456789000,BloodPressure,190.0"); // High BP
        webSocketClient.onMessage("1,1623456789000,BloodSaturation,85.0"); // Low Saturation
        webSocketClient.onMessage("1,1623456789000,BloodPressure,80.0"); // Low BP for Hypotensive Hypoxemia

        Patient patient = dataStorage.getAllPatients().get(0);

        // Capture console output for alerts
        CountDownLatch latch = new CountDownLatch(1);
        System.setOut(new java.io.PrintStream(new java.io.ByteArrayOutputStream() {
            @Override
            public void flush() {
                latch.countDown();
            }
        }));

        alertGenerator.evaluateData(patient);

        // Wait for the latch to ensure alerts have been printed
        try {
            latch.await(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Restore original System.out
        System.setOut(System.out);

        // Further validation can be added here if necessary
    }

    @Test
    public void testConnectionHandling() throws Exception {
        // Use a CountDownLatch to wait for the close event
        CountDownLatch latch = new CountDownLatch(1);
        doAnswer(invocation -> {
            latch.countDown();
            return null;
        }).when(mockClient).onClose(anyInt(), anyString(), anyBoolean());

        // Connect the client
        mockClient.connectBlocking(); // Wait for the connection to be established
        assertTrue(mockClient.isOpen());

        // Simulate connection loss
        mockClient.closeConnection(1006, "Abnormal closure"); // Use closeConnection to simulate connection loss
        assertTrue(latch.await(5, TimeUnit.SECONDS)); // Wait for the onClose event to be triggered
        assertFalse(mockClient.isOpen());
    }

    @AfterEach
    public void teardown() {
        try {
            mockClient.closeBlocking();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

