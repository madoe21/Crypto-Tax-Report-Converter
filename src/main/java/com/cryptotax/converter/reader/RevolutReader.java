package com.cryptotax.converter.reader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.cryptotax.converter.data.in.RevolutData;
import com.cryptotax.converter.interfaces.Reader;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class RevolutReader implements Reader<RevolutData> {

    private static final int NUM_OF_COLUMNS = 7;

    private String inputPath;

    public RevolutReader(String inputPath) {
        this.inputPath = inputPath;
    }

    @Override
    public List<RevolutData> getData() throws CsvValidationException, IOException {
        CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(inputPath), "UTF-8"));
        List<RevolutData> data = new ArrayList<>();

        // Read header
        String[] header = reader.readNext();
        if (header == null || header.length != NUM_OF_COLUMNS) {
            reader.close();
            throw new IllegalArgumentException("Invalid CSV format. Missing required columns.");
        }

        // Process each row
        String[] line;
        while ((line = reader.readNext()) != null) {
            String symbol = line[0];
            String type = line[1];
            String quantity = line[2];
            String price = line[3];
            String value = line[4];
            String fees = line[5];
            String date = line[6];
            data.add(new RevolutData(symbol, type, quantity, price, value, fees, date));
        }
        reader.close();
        return data;
    }

    @Override
    public Class<RevolutData> getGenericType() {
        return RevolutData.class;
    }
}
