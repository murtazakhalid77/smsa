package com.smsa.backend.repository;

import com.smsa.backend.model.InvoiceDetails;
import com.smsa.backend.model.InvoiceDetailsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface InvoiceDetailsRepository extends JpaRepository<InvoiceDetails, InvoiceDetailsId> {
    List<InvoiceDetails> findBySheetUniqueId(String sheetUniqueId);
    @Transactional
    @Modifying
    @Query("UPDATE InvoiceDetails i SET i.isSentInMail = true WHERE i.invoiceDetailsId.accountNumber = ?1 AND i.sheetUniqueId = ?2")
    void updateIsSentInMailByAccountNumberAndSheetUniqueId(String accountNumber, String sheetUniqueId);

    @Transactional
    @Modifying
    @Query("UPDATE InvoiceDetails i SET i.isSentInMail = false WHERE i.invoiceDetailsId.accountNumber = ?1 AND i.sheetUniqueId = ?2")
    void revertIsSentInMailByAccountNumberAndSheetUniqueId(String accountNumber, String sheetUniqueId);

    List<InvoiceDetails> findAllBySheetUniqueId(String sheetUniqueId);

    @Query("SELECT details.invoiceDetailsId.awb FROM InvoiceDetails details WHERE details.invoiceDetailsId.awb IN :awbList")
    List<String> findDuplicateAwbs(Set<String> awbList);;

    @Transactional
    @Modifying
    @Query("DELETE FROM InvoiceDetails i WHERE i.invoiceDetailsId.accountNumber = ?1 AND i.sheetUniqueId = ?2")
    void deleteInvoiceData(String accountNumber, String sheetUniqueId);
    ;
    @Transactional
    @Modifying
    void deleteBySheetUniqueId(String uniqueId);

}
