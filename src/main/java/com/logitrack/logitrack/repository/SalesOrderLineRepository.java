package com.logitrack.logitrack.repository;

import com.logitrack.logitrack.model.SalesOrderLine;
import com.logitrack.logitrack.model.enums.SalesOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalesOrderLineRepository extends JpaRepository<SalesOrderLine, Long> {

    long countByProduct_SkuAndSalesOrder_StatusIn(String sku, List<SalesOrderStatus> statuses);
}