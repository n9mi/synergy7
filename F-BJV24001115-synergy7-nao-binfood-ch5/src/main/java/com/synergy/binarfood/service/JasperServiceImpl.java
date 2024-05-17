package com.synergy.binarfood.service;

import com.synergy.binarfood.model.order.InvoiceResponse;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class JasperServiceImpl implements JasperService {
    public byte[] getInvoiceReport(InvoiceResponse invoice) {
        JasperReport jasperReport;
        try {
            jasperReport = (JasperReport) JRLoader
                    .loadObject(ResourceUtils.getFile("invoice.jasper"));
        } catch (JRException | FileNotFoundException e) {
            try {
                File file = ResourceUtils.getFile("classpath:jasper/invoice.jrxml");
                jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
                JRSaver.saveObject(jasperReport, "invoice.jasper");
            } catch (FileNotFoundException | JRException eF) {
                throw new RuntimeException(eF);
            }
        }

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(invoice.getItems());
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("totalPrice", invoice.getTotalPrice());
        parameters.put("username", invoice.getUsername());
        parameters.put("address", invoice.getAddress());
        parameters.put("completedAt", invoice.getCompletedAt());

        JasperPrint jasperPrint;
        byte[] reportContent;
        try {
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            reportContent = JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (JRException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        return reportContent;
    }
}
