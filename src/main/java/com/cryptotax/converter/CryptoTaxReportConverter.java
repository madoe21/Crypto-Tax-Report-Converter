package com.cryptotax.converter;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.cryptotax.converter.converter.*;
import com.cryptotax.converter.interfaces.*;
import com.cryptotax.converter.reader.*;
import com.cryptotax.converter.writer.*;
import com.opencsv.exceptions.CsvValidationException;

public class CryptoTaxReportConverter {

    private static final List<String> INPUT_TYPES = List.of("revolut", "swissborg", "kriptomat");
    private static final List<String> OUTPUT_TYPES = List.of("kriptomat");

    private String inputPath;
    private String inputType;
    private String outputPath;
    private String outputType;

    private Reader<?> reader;
    private Converter converter;
    private Writer<?> writer;

    public CryptoTaxReportConverter(String[] args) {
        this.inputType = args[0];
        this.inputPath = args[1];
        this.outputPath = args[2];
        this.outputType = "blockpit";
        checkArguments(args);
    }

    private void checkArguments(String[] args) {
        File inputFile = new File(inputPath);

        if (args.length != 3) {
            System.err.println("Usage: java Converter <inputType> <inputPath> <outputPath>");
            System.err.println("Available types: " + String.join(", ", INPUT_TYPES));
            return;
        }

        if (!inputFile.exists()) {
            System.err.println("Error: Input file does not exist at " + inputPath);
            return;
        }

        if (!INPUT_TYPES.contains(inputType)) {
            System.err.println("Error: Input type does not exist. Choose a valid input type.");
            return;
        }

        if (!OUTPUT_TYPES.contains(outputType)) {
            System.err.println("Error: Output type does not exist. Choose a valid output type.");
            return;
        }
    }

    private void startProcessing() {
        System.out.println("Input Type: " + inputType);
        System.out.println("Output Type: " + outputType);
        System.out.println("Input Path: " + inputPath);
        System.out.println("Output Path: " + outputPath);

        try {
            if (StringUtils.equals(outputType, "blockpit")) {
                writer = new BlockpitWriter(outputPath);
            }

            if (StringUtils.equals(inputType, "revolut")) {
                reader = new RevolutReader(inputPath);
                converter = new RevolutConverter(reader, writer);
            } else if (StringUtils.equals(inputType, "swissborg")) {
                reader = new SwissBorgReader(inputPath);
                converter = new SwissBorgConverter(reader, writer);
            } else if (StringUtils.equals(inputType, "kriptomat")) {
                reader = new KriptomatReader(inputPath);
                converter = new KriptomatConverter(reader, writer);
            }

            converter.readInputFile();
            converter.convert();
            converter.writeOutputFile();

            System.out.println("Conversion completed successfully. Output written to: " + outputPath);
        } catch (IOException e) {
            System.err.println("Error during conversion: " + e.getMessage());
            e.printStackTrace();
        } catch (CsvValidationException e) {
            System.err.println("Error during conversion: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        CryptoTaxReportConverter app = new CryptoTaxReportConverter(args);
        app.startProcessing();
    }
}
