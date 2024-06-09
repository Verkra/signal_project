package com.data_management;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.logging.Logger;

/**
 * WebSocket-based implementation of DataReader.
 * Reads patient data from a WebSocket server and stores it in a DataStorage instance.
 */
public class WebSocketDataReader extends WebSocketClient implements DataReader {
    private static final Logger logger = Logger.getLogger(WebSocketDataReader.class.getName());
    private DataStorage dataStorage;

    /**
     * Constructs a WebSocketDataReader with the specified server URI and DataStorage.
     *
     * @param serverUri   the URI of the WebSocket server
     * @param dataStorage the DataStorage instance for storing patient data
     */
    public WebSocketDataReader(URI serverUri, DataStorage dataStorage) {
        super(serverUri);
        this.dataStorage = dataStorage;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        logger.info("Connected to WebSocket server");
    }

    @Override
    public void onMessage(String message) {
        try {
            String[] parts = message.split(",");
            int patientId = Integer.parseInt(parts[0]);
            long timestamp = Long.parseLong(parts[1]);
            String recordType = parts[2];
            double measurementValue = Double.parseDouble(parts[3]);

            if (patientId <= 0 || timestamp <= 0) {
                logger.warning("Invalid message data: patientId=" + patientId + ", timestamp=" + timestamp);
                return;
            }

            dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
        } catch (Exception e) {
            logger.severe("Error processing WebSocket message: " + e.getMessage());
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        logger.info("Disconnected from WebSocket server. Code: " + code + ", Reason: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public void startReading(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        connect();
    }

    @Override
    public void stopReading() {
        close();
    }
}