package com.cryptotax.converter.interfaces;

import java.io.IOException;
import java.util.List;

public interface Writer<T extends DataOut> {
    void writeToFile(List<T> rows) throws IOException;

    Class<T> getGenericType();
}
