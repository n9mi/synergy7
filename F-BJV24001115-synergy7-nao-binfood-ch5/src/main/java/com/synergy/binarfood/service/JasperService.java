package com.synergy.binarfood.service;

import com.synergy.binarfood.model.order.InvoiceResponse;

public interface JasperService {
    public byte[] getInvoiceReport(InvoiceResponse invoice);
}
