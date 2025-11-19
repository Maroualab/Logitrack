package com.logitrack.logitrack.repository;

import com.logitrack.logitrack.model.PurchaseOrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderLineRepository extends JpaRepository<PurchaseOrderLine, Long> {
    List<PurchaseOrderLine> findAllByPurchaseOrderId(Long purchaseOrderId);

}