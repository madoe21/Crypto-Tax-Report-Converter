package com.cryptotax.converter.converter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.cryptotax.converter.data.in.RevolutData;
import com.cryptotax.converter.data.out.BlockpitData;
import com.cryptotax.converter.interfaces.Converter;
import com.cryptotax.converter.interfaces.Reader;
import com.cryptotax.converter.interfaces.Writer;
import com.cryptotax.converter.writer.BlockpitWriter;
import com.opencsv.exceptions.CsvValidationException;

public class RevolutConverter implements Converter {
    private static final String INTEGRATION_NAME = "Revolut";
    private Reader<RevolutData> reader;
    private Writer<?> writer;
    private Class<?> writerType;
    private List<RevolutData> dataToConvert;
    private List<?> dataConverted;

    @SuppressWarnings("unchecked")
    public RevolutConverter(Reader<?> reader, Writer<?> writer) {
        this.reader = (Reader<RevolutData>) reader;
        this.writer = writer;
        this.writerType = writer.getGenericType();
    }

    @Override
    public void readInputFile() throws CsvValidationException, IOException {
        dataToConvert = reader.getData();
    }

    @Override
    public void convert() {
        if (writerType.equals(BlockpitData.class)) {
            dataConverted = dataToConvert.stream().map(revolutData -> {
                String date = revolutData.getDate();
                String label = "";
                String outgoingAsset = "";
                String outgoingAmount = "";
                String incomingAsset = "";
                String incomingAmount = "";
                String feeAsset = "";
                String feeAmount = "";
                String comment = revolutData.getType();
                String transactionId = "";

                switch (revolutData.getType()) {
                    case "Einsetzen":
                        label = "Withdrawal";
                        outgoingAsset = getCurrencyFromValue(revolutData.getSymbol());
                        outgoingAmount = BlockpitData
                                .formatValue(removeCurrencyAndDotFromValue(revolutData.getQuantity(), outgoingAsset));
                        break;
                    case "Verkaufen":
                        label = "Trade";
                        outgoingAsset = getCurrencyFromValue(revolutData.getSymbol());
                        outgoingAmount = BlockpitData
                                .formatValue(removeCurrencyAndDotFromValue(revolutData.getQuantity(), outgoingAsset));
                        incomingAsset = getCurrencyFromValue(revolutData.getValue());
                        incomingAmount = BlockpitData
                                .formatValue(removeCurrencyAndDotFromValue(revolutData.getValue(), incomingAsset));
                        feeAsset = getCurrencyFromValue(revolutData.getFees());
                        feeAmount = BlockpitData
                                .formatValue(removeCurrencyAndDotFromValue(revolutData.getFees(), feeAsset));
                        break;
                    case "Belohnung fürs Lernen":
                        label = "Gift Received";
                        incomingAsset = getCurrencyFromValue(revolutData.getSymbol());
                        incomingAmount = BlockpitData
                                .formatValue(removeCurrencyAndDotFromValue(revolutData.getQuantity(), incomingAsset));
                        break;
                    case "Staking-Prämie":
                        label = "Staking";
                        incomingAsset = getCurrencyFromValue(revolutData.getSymbol());
                        incomingAmount = BlockpitData
                                .formatValue(removeCurrencyAndDotFromValue(revolutData.getQuantity(), incomingAsset));
                        break;
                    default:
                        label = "Unknown";
                        comment = "Unknown transaction type";
                        break;
                }

                return new BlockpitData(date,
                        INTEGRATION_NAME, label, outgoingAsset, outgoingAmount,
                        incomingAsset, incomingAmount, feeAsset, feeAmount, comment, transactionId);
            }).collect(Collectors.toList());
        } else {
            throw new UnsupportedOperationException("Unknown Writer-Type");
        }
    }

    private String getCurrencyFromValue(String value) {
        if (value.contains("€")) {
            return "EUR";
        } else if (value.contains("$")) {
            return "USD";
        }
        return value;
    }

    private String removeCurrencyAndDotFromValue(String value, String currency) {
        switch (currency) {
            case "EUR":
                value = value.replace("€", "");
            case "USD":
                value = value.replace("$", "");
            default:
                return value.replace(".", "");
        }
    }

    @Override
    public void writeOutputFile() throws IOException {
        if (writerType.equals(BlockpitData.class)) {
            List<BlockpitData> blockpitDatas = dataConverted.stream()
                    .filter(item -> item instanceof BlockpitData)
                    .map(item -> (BlockpitData) item)
                    .collect(Collectors.toList());
            ((BlockpitWriter) writer).writeToFile(blockpitDatas);
        } else {
            throw new UnsupportedOperationException("Unknown Writer-Type");
        }
    }
}