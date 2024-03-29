package com.synergy.binfood.utils;

import com.synergy.binfood.model.order.OrderItemResponse;
import com.synergy.binfood.model.order.OrderResponse;
import com.synergy.binfood.utils.exception.ReceiptWriterError;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReceiptWriterUtil {
    public static void writeToPDF(OrderResponse orderDetails) throws IOException, Exception {
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

            contentStream.showText(String.format("ORDER ID : %s", orderDetails.getOrderId()));
            contentStream.newLine();

            contentStream.showText("=============================================");
            contentStream.newLine();

            for (OrderItemResponse orderItemResponse: orderDetails.getOrderItems()) {
                contentStream.showText(String.format("%s - %s : Rp. %,d x %d pcs : Rp. %,d",
                        orderItemResponse.getMenuName(), orderItemResponse.getVariantName(),
                        orderItemResponse.getMenuPrice(), orderItemResponse.getQuantity(),
                        orderItemResponse.getTotalPerItem()));
                contentStream.newLine();
            }

            contentStream.showText("=============================================");
            contentStream.newLine();

            contentStream.showText(String.format("TOTAL : Rp. %,d", orderDetails.getTotalPrice()));
            contentStream.newLine();

            contentStream.showText(String.format("Pay at : %s",
                    new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(orderDetails.getPayAt())));
            contentStream.newLine();

            contentStream.close();
            String receiptFileNameDate = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(createdAt);
            String fileName = String.format("RECEIPT_%s.pdf", receiptFileNameDate);
            receiptDoc.save(folderPath + "/" + fileName);
            receiptDoc.close();
        } catch (IOException e) {
            System.out.println("Something wrong : " + e.getMessage());
            throw new IOException(e.getMessage());
        } catch (Exception e) {
            System.out.println("Can't write receipt : " + e.getMessage());
            throw new Exception(e.getMessage());
        }
    }
}
