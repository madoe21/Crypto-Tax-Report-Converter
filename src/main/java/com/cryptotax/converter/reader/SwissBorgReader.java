package com.cryptotax.converter.reader;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.cryptotax.converter.data.in.SwissBorgData;
import com.cryptotax.converter.interfaces.Reader;

public class SwissBorgReader implements Reader<SwissBorgData> {
    private static final int NUM_OF_CURRENCY_LINE = 9;
    private static final int NUM_OF_CURRENCY_COLUMN = 5;
    private static final int NUM_OF_HEADER_LINE = 14;
    private static final int NUM_OF_COLUMNS = 11;

    private String inputPath;

    public SwissBorgReader(String inputPath) {
        this.inputPath = inputPath;
    }

    @Override
    public List<SwissBorgData> getData() throws IOException {
        List<SwissBorgData> data = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(inputPath);
                Workbook inputWorkbook = new XSSFWorkbook(fis)) {

            Sheet inputSheet = inputWorkbook.getSheetAt(0);
            String localCurrency = inputSheet.getRow(NUM_OF_CURRENCY_LINE - 1).getCell(NUM_OF_CURRENCY_COLUMN - 1)
                    .getStringCellValue();

            for (Row row : inputSheet) {
                if (row.getRowNum() < NUM_OF_HEADER_LINE - 1) {
                    continue;
                }
                if (row.getRowNum() == NUM_OF_HEADER_LINE - 1) {
                    if (row.getLastCellNum() < NUM_OF_COLUMNS) {
                        throw new IllegalArgumentException("Invalid XLSX format. Missing required columns.");
                    }
                    continue;
                }

                String localTime = row.getCell(0).getStringCellValue();
                String timeUTC = row.getCell(1).getStringCellValue();
                String type = row.getCell(2).getStringCellValue();
                String currency = row.getCell(3).getStringCellValue();
                String grossAmount = String.valueOf(row.getCell(4).getNumericCellValue());
                String grossAmountLocalCurrency = String.valueOf(row.getCell(5).getNumericCellValue());
                String fee = String.valueOf(row.getCell(6).getNumericCellValue());
                String feeLocalCurrency = String.valueOf(row.getCell(7).getNumericCellValue());
                String netAmount = String.valueOf(row.getCell(8).getNumericCellValue());
                String netAmountLocalCurrency = String.valueOf(row.getCell(9).getNumericCellValue());
                String note = row.getCell(10).getStringCellValue();

                data.add(new SwissBorgData(localTime, timeUTC, type, currency, grossAmount, grossAmountLocalCurrency,
                        fee, feeLocalCurrency,
                        netAmount, netAmountLocalCurrency, note, localCurrency));
            }
        }
        return data;
    }

    @Override
    public Class<SwissBorgData> getGenericType() {
        return SwissBorgData.class;
    }
}
