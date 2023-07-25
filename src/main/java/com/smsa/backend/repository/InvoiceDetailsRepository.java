package com.smsa.backend.repository;

import com.smsa.backend.model.InvoiceDetails;
import com.smsa.backend.model.InvoiceDetailsId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceDetailsRepository extends JpaRepository<InvoiceDetails, InvoiceDetailsId> {
    List<InvoiceDetails> findBySheetUniqueId(String sheetUniqueId);

    List<InvoiceDetails> findAllBySheetUniqueId(String sheetUniqueId);
}
