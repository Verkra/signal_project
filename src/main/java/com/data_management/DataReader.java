package com.data_management;

/**
 * Interface for reading data and adding it to a DataStorage instance.
 */
public interface DataReader {
    /**
     * Starts reading data and storing it in the specified DataStorage instance.
     *
     * @param dataStorage the DataStorage instance to store the data
     */
    void startReading(DataStorage dataStorage);

    /**
     * Stops reading data.
     */
    void stopReading();
}