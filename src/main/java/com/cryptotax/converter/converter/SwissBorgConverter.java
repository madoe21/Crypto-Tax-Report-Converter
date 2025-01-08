package com.cryptotax.converter.converter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.cryptotax.converter.data.in.SwissBorgData;
import com.cryptotax.converter.data.out.BlockpitData;
import com.cryptotax.converter.interfaces.Converter;
import com.cryptotax.converter.interfaces.Reader;
import com.cryptotax.converter.interfaces.Writer;
import com.cryptotax.converter.writer.BlockpitWriter;
import com.opencsv.exceptions.CsvValidationException;

public class SwissBorgConverter implements Converter {
    private static final String INTEGRATION_NAME = "SwissBorg";
    private Reader<SwissBorgData> reader;
    private Writer<?> writer;
    private Class<?> writerType;
    private List<SwissBorgData> dataToConvert;
    private List<?> dataConverted;

    @SuppressWarnings("unchecked")
    public SwissBorgConverter(Reader<?> reader, Writer<?> writer) {
        this.reader = (Reader<SwissBorgData>) reader;
        this.writer = writer;
        this.writerType = writer.getGenericType();
    }

    @Override
    public void readInputFile() throws CsvValidationException, IOException {
        dataToConvert = reader.getData();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void convert() {
        if (writerType.equals(BlockpitData.class)) {
            dataConverted = new ArrayList<>();

            for (SwissBorgData swissBorgData : dataToConvert) {
                BlockpitData data = null;
                String date = swissBorgData.getTimeUTC();
                String label = "";
                String outgoingAsset = "";
                String outgoingAmount = "";
                String incomingAsset = "";
                String incomingAmount = "";
                String feeAsset = "";
                String feeAmount = "";
                String comment = "";
                String transactionId = "";

                switch (swissBorgData.getType()) {
                    case "Deposit":
                        incomingAsset = swissBorgData.getCurrency();
                        incomingAmount = BlockpitData.formatValue(swissBorgData.getNetAmount());
                        feeAsset = swissBorgData.getCurrency();
                        feeAmount = BlockpitData.formatValue(swissBorgData.getFee());
                        comment = swissBorgData.getNote();
                        label = "Deposit";
                        data = new BlockpitData(date,
                                INTEGRATION_NAME, label, outgoingAsset, outgoingAmount,
                                incomingAsset, incomingAmount, feeAsset, feeAmount, comment, transactionId);
                        ((List<BlockpitData>) dataConverted).add(data);
                        break;
                    case "Sell":
                        outgoingAsset = swissBorgData.getCurrency();
                        outgoingAmount = BlockpitData.formatValue(swissBorgData.getGrossAmount());
                        feeAsset = swissBorgData.getCurrency();
                        feeAmount = BlockpitData.formatValue(swissBorgData.getFee());
                        comment = swissBorgData.getNote();
                        label = "Withdrawal";
                        data = new BlockpitData(date,
                                INTEGRATION_NAME, label, outgoingAsset, outgoingAmount,
                                incomingAsset, incomingAmount, feeAsset, feeAmount, comment, transactionId);
                        ((List<BlockpitData>) dataConverted).add(data);
                        break;
                    case "Buy":
                        for (BlockpitData blockpitData : (List<BlockpitData>) dataConverted) {
                            if (StringUtils.equals(blockpitData.getLabel(), "Withdrawal")
                                    && StringUtils.equals(blockpitData.getDate(), date)) {
                                data = blockpitData;
                            }
                        }

                        incomingAsset = swissBorgData.getCurrency();
                        incomingAmount = BlockpitData.formatValue(swissBorgData.getGrossAmount());
                        feeAsset = swissBorgData.getCurrency();
                        feeAmount = BlockpitData.formatValue(swissBorgData.getFee());
                        label = "Deposit";

                        if (data == null) {
                            comment = swissBorgData.getNote();
                        } else {
                            label = "Trade";
                            outgoingAsset = data.getOutgoingAsset();
                            outgoingAmount = data.getOutgoingAmount();
                            comment = data.getComment() + "; " + swissBorgData.getNote();
                            dataConverted.remove(data);
                        }

                        data = new BlockpitData(date,
                                INTEGRATION_NAME, label, outgoingAsset, outgoingAmount,
                                incomingAsset, incomingAmount, feeAsset, feeAmount, comment, transactionId);
                        ((List<BlockpitData>) dataConverted).add(data);
                        break;
                    case "Withdrawal":
                        outgoingAsset = swissBorgData.getCurrency();
                        outgoingAmount = BlockpitData.formatValue(swissBorgData.getNetAmount());
                        feeAsset = swissBorgData.getCurrency();
                        feeAmount = BlockpitData.formatValue(swissBorgData.getFee());
                        comment = swissBorgData.getNote();
                        label = "Withdrawal";
                        data = new BlockpitData(date,
                                INTEGRATION_NAME, label, outgoingAsset, outgoingAmount,
                                incomingAsset, incomingAmount, feeAsset, feeAmount, comment, transactionId);
                        ((List<BlockpitData>) dataConverted).add(data);
                        break;
                    default:
                        label = "Unknown";
                        comment = "Unknown transaction type";
                        data = new BlockpitData(date,
                                INTEGRATION_NAME, label, outgoingAsset, outgoingAmount,
                                incomingAsset, incomingAmount, feeAsset, feeAmount, comment, transactionId);
                        ((List<BlockpitData>) dataConverted).add(data);
                        break;
                }
            }
        } else {
            throw new UnsupportedOperationException("Unknown Writer-Type");
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