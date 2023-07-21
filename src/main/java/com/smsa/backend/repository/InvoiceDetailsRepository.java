package com.smsa.backend.repository;

import com.smsa.backend.model.InvoiceDetails;
import com.smsa.backend.model.InvoiceDetailsId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceDetailsRepository extends JpaRepository<InvoiceDetails, InvoiceDetailsId> {

}
