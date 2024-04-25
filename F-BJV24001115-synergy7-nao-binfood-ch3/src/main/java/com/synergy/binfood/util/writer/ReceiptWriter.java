package com.synergy.binfood.util.writer;

import com.synergy.binfood.model.order.OrderDetailResponse;
import com.synergy.binfood.model.order.OrderResponse;
import com.synergy.binfood.util.exception.WriterException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReceiptWriter {

    public static void writeToPDF(OrderResponse order) throws WriterException {
        String folderPath = System.getProperty("user.dir") + "/receipts";

        try {
            File receiptFolder = new File(folderPath);
            if (!receiptFolder.exists()) {
                receiptFolder.mkdir();
            }

            Date createdAt = new Date();
            PDDocument receiptDoc = new PDDocument();
            receiptDoc.addPage(new PDPage());

            PDPage page = receiptDoc.getPage(0);
            PDPageContentStream contentStream = new PDPageContentStream(receiptDoc, page,
                    PDPageContentStream.AppendMode.APPEND,true,true);

            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(25, 725);

            contentStream.showText(String.format("ORDER ID : %s", order.getId()));
            contentStream.newLine();

            contentStream.showText(String.format("Ordered at : %s", order.getCreatedAt()));
            contentStream.newLine();

            contentStream.showText("=============================================");
            contentStream.newLine();

            int totalPrice = 0;
            for (OrderDetailResponse orderDetail: order.getOrderDetails()) {
                contentStream.showText(String.format("%s : Rp. %,d x %d pcs : Rp. %,d",
                        orderDetail.getProductName(), orderDetail.getProductPrice(),
                        orderDetail.getProductQuantity(), orderDetail.getTotalPrice()));
                totalPrice += orderDetail.getTotalPrice();
                contentStream.newLine();
            }

            contentStream.showText("=============================================");
            contentStream.newLine();

            contentStream.showText(String.format("TOTAL : Rp. %,d", totalPrice));
            contentStream.newLine();

            contentStream.showText(String.format("Pay at : %s", order.getCompletedAt()));
            contentStream.newLine();

            contentStream.close();
            String receiptFileNameDate = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(createdAt);
            String fileName = String.format("RECEIPT_%s.pdf", receiptFileNameDate);
            receiptDoc.save(folderPath + "/" + fileName);
            receiptDoc.close();
        } catch (Exception e) {
            throw new WriterException(e.getMessage());
        }
    }
}
