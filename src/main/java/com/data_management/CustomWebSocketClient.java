package com.data_management;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.logging.Logger;

public class CustomWebSocketClient extends WebSocketClient {
    private static final Logger logger = Logger.getLogger(CustomWebSocketClient.class.getName());
    private DataStorage dataStorage;

    public CustomWebSocketClient(URI serverUri, DataStorage dataStorage) {
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
            logger.info("Received message: " + message);
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
        logger.severe("WebSocket error: " + ex.getMessage());
    }
}
