package com.cryptotax.converter.converter;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.cryptotax.converter.data.in.KriptomatData;
import com.cryptotax.converter.data.out.BlockpitData;
import com.cryptotax.converter.interfaces.Converter;
import com.cryptotax.converter.interfaces.Reader;
import com.cryptotax.converter.interfaces.Writer;
import com.cryptotax.converter.writer.BlockpitWriter;
import com.opencsv.exceptions.CsvValidationException;

public class KriptomatConverter implements Converter {
    private static final String INTEGRATION_NAME = "Kriptomat";
    private Reader<KriptomatData> reader;
    private Writer<?> writer;
    private Class<?> writerType;
    private List<KriptomatData> dataToConvert;
    private List<?> dataConverted;

    @SuppressWarnings("unchecked")
    public KriptomatConverter(Reader<?> reader, Writer<?> writer) {
        this.reader = (Reader<KriptomatData>) reader;
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
            dataConverted = dataToConvert.stream().map(kriptomatData -> {
                String date = kriptomatData.getTimestamp();
                String label = "";
                String outgoingAsset = "";
                String outgoingAmount = "";
                String incomingAsset = "";
                String incomingAmount = "";
                String pricePerCoin = "";
                String feeAsset = "";
                String feeAmount = "";
                String comment = kriptomatData.getNotes();
                String transactionId = "";

                String amountEUR;
                String amountTransacted = "";
                String transactedInclusiveFees = "";
                double fee = 0L;

                switch (kriptomatData.getTransactionType()) {
                    case "Buy":
                        transactedInclusiveFees = kriptomatData.getTransactedInclusiveFees();
                        amountTransacted = kriptomatData.getAmountTransacted();
                        pricePerCoin = kriptomatData.getPricePerCoin();
                        fee = Math.round((Double.valueOf(transactedInclusiveFees) -
                                (Double.valueOf(
                                        amountTransacted)
                                        * Double
                                                .valueOf(
                                                        pricePerCoin)))
                                * 100.0) / 100.0;

                        incomingAsset = kriptomatData.getAsset();
                        incomingAmount = BlockpitData.formatValue(kriptomatData.getAmountTransacted());
                        outgoingAsset = "EUR";
                        outgoingAmount = BlockpitData.formatValue(transactedInclusiveFees);
                        feeAsset = "EUR";
                        feeAmount = BlockpitData.formatValue(String.valueOf(fee));
                        label = "Trade";
                        break;
                    case "Sell":
                        amountEUR = kriptomatData.getAmountEUR();
                        transactedInclusiveFees = kriptomatData.getTransactedInclusiveFees();
                        fee = Double.valueOf(transactedInclusiveFees) - Double.valueOf(amountEUR);

                        incomingAsset = "EUR";
                        incomingAmount = BlockpitData.formatValue(amountEUR);
                        outgoingAsset = kriptomatData.getAsset();
                        outgoingAmount = BlockpitData.formatValue(kriptomatData.getAmountTransacted());
                        feeAsset = "EUR";
                        feeAmount = BlockpitData.formatValue(String.valueOf(fee));
                        label = "Trade";
                        break;
                    case "Deposit":
                        amountTransacted = kriptomatData.getAmountTransacted();
                        transactedInclusiveFees = kriptomatData.getTransactedInclusiveFees();
                        fee = Double.valueOf(transactedInclusiveFees) - Double.valueOf(amountTransacted);

                        incomingAsset = kriptomatData.getAsset();
                        incomingAmount = BlockpitData.formatValue(amountTransacted);
                        feeAsset = kriptomatData.getAsset();
                        feeAmount = BlockpitData.formatValue(String.valueOf(fee));
                        label = "Deposit";
                        break;
                    case "Withdrawal":
                        amountTransacted = kriptomatData.getAmountTransacted();
                        transactedInclusiveFees = kriptomatData.getTransactedInclusiveFees();
                        fee = Double.valueOf(transactedInclusiveFees) - Double.valueOf(amountTransacted);

                        outgoingAsset = kriptomatData.getAsset();
                        outgoingAmount = BlockpitData.formatValue(kriptomatData.getAmountTransacted());
                        feeAsset = kriptomatData.getAsset();
                        feeAmount = BlockpitData.formatValue(String.valueOf(fee));
                        label = "Withdrawal";
                        break;
                    case "Exchange":
                        amountEUR = kriptomatData.getAmountEUR();
                        transactedInclusiveFees = kriptomatData.getTransactedInclusiveFees();
                        fee = Double.valueOf(transactedInclusiveFees) - Double.valueOf(amountEUR);

                        outgoingAsset = getCurrencyFromComment(comment);
                        outgoingAmount = "???";
                        incomingAsset = kriptomatData.getAsset();
                        incomingAmount = BlockpitData.formatValue(kriptomatData.getAmountTransacted());
                        feeAsset = "EUR";
                        feeAmount = BlockpitData.formatValue(String.valueOf(fee));
                        label = "Trade";
                        break;
                    case "Referral_rewards":
                        incomingAsset = kriptomatData.getAsset();
                        incomingAmount = BlockpitData.formatValue(kriptomatData.getAmountTransacted());
                        label = "Gift Received";
                        break;
                    default:
                        label = "Unknown";
                        comment = "Unknown transaction type";
                        break;
                }

                return new BlockpitData(date, INTEGRATION_NAME, label, outgoingAsset, outgoingAmount,
                        incomingAsset, incomingAmount, feeAsset, feeAmount, comment, transactionId);
            }).collect(Collectors.toList());
        } else {
            throw new UnsupportedOperationException("Unknown Writer-Type");
        }
    }

    private String getCurrencyFromComment(String notes) {
        String result = "";
        String regex = "(?<=von\\s)([a-zA-Z]+)(?=mit)";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(notes);

        if (matcher.find()) {
            result = matcher.group(1).toUpperCase();
        }
        return result;
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