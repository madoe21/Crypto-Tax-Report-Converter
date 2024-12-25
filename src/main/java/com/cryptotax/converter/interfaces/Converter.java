package com.cryptotax.converter.interfaces;

import java.io.IOException;

import com.opencsv.exceptions.CsvValidationException;

public interface Converter {
    void readInputFile() throws CsvValidationException, IOException;

    void convert();

    void writeOutputFile() throws IOException;
}
