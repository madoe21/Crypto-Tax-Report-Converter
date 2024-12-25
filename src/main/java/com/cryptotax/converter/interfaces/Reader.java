package com.cryptotax.converter.interfaces;

import java.io.IOException;
import java.util.List;

import com.opencsv.exceptions.CsvValidationException;

public interface Reader<T extends DataIn> {
    List<T> getData() throws IOException, CsvValidationException;

    Class<T> getGenericType();
}
