package com.synergy.utils;

import com.synergy.model.Receipt;
import com.synergy.model.ReceiptItem;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReceiptWriter {
    public static void writeToPDF(String folderName, Receipt receipt) throws RuntimeException {
        String folderPath = System.getProperty("user.dir");
        try {
            File receiptFolder = new File(folderPath + "/" + folderName);
            if (!receiptFolder.exists()) {
                receiptFolder.mkdir();
            }

            PDDocument receiptDoc = new PDDocument();
            receiptDoc.addPage(new PDPage());

            PDPage page = receiptDoc.getPage(0);
            PDPageContentStream contentStream = new PDPageContentStream(receiptDoc, page, PDPageContentStream.AppendMode.APPEND,true,true);

            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(25, 725);

            contentStream.showText(receipt.merchantName + " receipt");
            contentStream.newLine();

            contentStream.showText("=============================================");
            contentStream.newLine();

            contentStream.showText(String.format("RECEIPT ID : %s", receipt.id));
            contentStream.newLine();

            Date createdAt = new Date();
            String receiptDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(createdAt);
            contentStream.showText(receiptDate);
            contentStream.newLine();

            contentStream.showText("=============================================");
            contentStream.newLine();

            for (ReceiptItem receiptItem: receipt.items) {
                contentStream.showText(
                        String.format("%s: Rp. %,d x %d pcs",
                                receiptItem.menuName,
                                receiptItem.menuPrice,
                                receiptItem.menuQuantity));
                contentStream.newLine();
            }

            contentStream.showText("=============================================");
            contentStream.newLine();

            contentStream.showText(String.format("Total : Rp. %,d", receipt.totalPrice));
            contentStream.newLine();

            contentStream.close();
            String receiptFileNameDate = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(createdAt);
            String fileName = String.format("RECEIPT_%s.pdf", receiptFileNameDate);
            System.out.println(folderPath + "/" + fileName);
            receiptDoc.save(folderPath + "/" + folderName + "/" + fileName);
            receiptDoc.close();
        } catch (IOException e) {
            System.out.println("Something wrong : " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            System.out.println("Can't write receipt : " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
