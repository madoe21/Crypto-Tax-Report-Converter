package com.cryptotax.converter.reader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.cryptotax.converter.data.in.KriptomatData;
import com.cryptotax.converter.interfaces.Reader;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class KriptomatReader implements Reader<KriptomatData> {

    private static final int NUM_OF_LINES_TO_SKIP = 3;
    private static final int NUM_OF_COLUMNS = 9;

    private String inputPath;

    public KriptomatReader(String inputPath) {
        this.inputPath = inputPath;
    }

    @Override
    public List<KriptomatData> getData() throws IOException, CsvValidationException {
        CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(inputPath), "UTF-8"));
        List<KriptomatData> data = new ArrayList<>();

        reader.skip(NUM_OF_LINES_TO_SKIP);

        // Read header
        String[] header = reader.readNext();
        if (header == null || header.length != NUM_OF_COLUMNS) {
            reader.close();
            throw new IllegalArgumentException("Invalid CSV format. Missing required columns.");
        }
        // Process each row
        String[] line;
        while ((line = reader.readNext()) != null) {
            String timestamp = line[0];
            String transactionType = line[1];
            String asset = line[2];
            String amountTransacted = line[3];
            String pricePerCoin = line[4];
            String amountEUR = line[5];
            String transactedInclusiveFees = line[6];
            String address = line[7];
            String notes = line[8];
            data.add(new KriptomatData(timestamp, transactionType, asset, amountTransacted,
                    pricePerCoin, amountEUR, transactedInclusiveFees, address, notes));
        }
        reader.close();
        return data;
    }

    @Override
    public Class<KriptomatData> getGenericType() {
        return KriptomatData.class;
    }
}
