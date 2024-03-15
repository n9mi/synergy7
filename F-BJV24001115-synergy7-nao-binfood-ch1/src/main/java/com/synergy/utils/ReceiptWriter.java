package com.synergy.utils;

import com.synergy.data.ReceiptItem;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReceiptWriter {
    private final String RECEIPT_FOLDER_PATH;
    private final String RECEIPT_FOLDER_NAME;
    private final String RECEIPT_FILENAME;
    private final Date createdAt;
    private final String merchantName;
    private final List<ReceiptItem> receiptItems;
    private final int totalPrice;

    public ReceiptWriter(String merchantName, List<ReceiptItem> receiptItems, int totalPrice) {
        this.RECEIPT_FOLDER_PATH = System.getProperty("user.dir");
        this.RECEIPT_FOLDER_NAME = "receipts";
        this.createdAt = new Date();
        this.merchantName = merchantName;
        String receiptFileNameDate = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(this.createdAt);
        this.RECEIPT_FILENAME = String.format("RECEIPT_%s.pdf", receiptFileNameDate);
        this.receiptItems = receiptItems;
        this.totalPrice = totalPrice;
    }

    public void generateReceipt() throws RuntimeException {
        try {
            File receiptFolder = new File(this.RECEIPT_FOLDER_PATH + "/" + this.RECEIPT_FOLDER_NAME);
            if (!receiptFolder.exists()) {
                receiptFolder.mkdir();
            }

            PDDocument receipt = new PDDocument();
            receipt.addPage(new PDPage());

            PDPage page = receipt.getPage(0);
            PDPageContentStream contentStream = new PDPageContentStream(receipt, page, PDPageContentStream.AppendMode.APPEND,true,true);

            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(25, 725);

            contentStream.showText(this.merchantName + " receipt");
            contentStream.newLine();

            contentStream.showText("=============================================");
            contentStream.newLine();

            for (ReceiptItem item: this.receiptItems) {
                contentStream.showText(item.getString());
                contentStream.newLine();
            }

            contentStream.showText("=============================================");
            contentStream.newLine();
            contentStream.showText(String.format("Total harga : Rp. %d", this.totalPrice));
            contentStream.newLine();

            contentStream.close();

            receipt.save(String.format("%s/%s/%s",
                    this.RECEIPT_FOLDER_PATH,
                    this.RECEIPT_FOLDER_NAME,
                    this.RECEIPT_FILENAME));
            receipt.close();

            System.out.println("Receipt created.");
        } catch (IOException e) {
            System.out.println("Something wrong : " + e.getMessage());
            throw new RuntimeException();
        }
    }
}
