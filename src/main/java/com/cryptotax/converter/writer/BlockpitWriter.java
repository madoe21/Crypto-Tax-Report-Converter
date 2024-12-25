package com.cryptotax.converter.writer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.cryptotax.converter.data.out.BlockpitData;
import com.cryptotax.converter.interfaces.Writer;

public class BlockpitWriter implements Writer<BlockpitData> {

    private static final String[] HEADER = {
            "Date (UTC)", "Integration Name", "Label", "Outgoing Asset", "Outgoing Amount",
            "Incoming Asset", "Incoming Amount", "Fee Asset (optional)", "Fee Amount (optional)",
            "Comment (optional)", "Trx. ID (optional)"
    };

    private String filePath;

    public BlockpitWriter(String filePath) {
        this.filePath = filePath;
    }

    public void writeToFile(List<BlockpitData> rows) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Transactions");

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < HEADER.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(HEADER[i]);
        }

        int rowIndex = 1;
        for (BlockpitData rowData : rows) {
            Row row = sheet.createRow(rowIndex++);
            Cell date = row.createCell(0);
            Cell integrationName = row.createCell(1);
            Cell label = row.createCell(2);
            Cell outgoingAsset = row.createCell(3);
            Cell outgoingAmount = row.createCell(4);
            Cell incomingAsset = row.createCell(5);
            Cell incomingAmount = row.createCell(6);
            Cell feeAsset = row.createCell(7);
            Cell feeAmount = row.createCell(8);
            Cell comment = row.createCell(9);
            Cell transactionId = row.createCell(10);

            date.setCellValue(rowData.getDate());
            integrationName.setCellValue(rowData.getIntegrationName());
            label.setCellValue(rowData.getLabel());
            outgoingAsset.setCellValue(rowData.getOutgoingAsset());
            outgoingAmount.setCellValue(rowData.getOutgoingAmount());
            incomingAsset.setCellValue(rowData.getIncomingAsset());
            incomingAmount.setCellValue(rowData.getIncomingAmount());
            feeAsset.setCellValue(rowData.getFeeAsset());
            feeAmount.setCellValue(rowData.getFeeAmount());
            comment.setCellValue(rowData.getComment());
            transactionId.setCellValue(rowData.getTransactionId());
        }

        for (int i = 0; i < HEADER.length; i++) {
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        }
        workbook.close();
    }

    @Override
    public Class<BlockpitData> getGenericType() {
        return BlockpitData.class;
    }
}
