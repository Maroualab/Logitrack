package com.logitrack.logitrack.repository;

import com.logitrack.logitrack.model.SalesOrder;
import com.logitrack.logitrack.model.enums.SalesOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {
    boolean  existsByWarehouseIdAndStatusIn(Long warehouseId, List<SalesOrderStatus> statuses);

    List<SalesOrder> findAllByStatus(SalesOrderStatus status);
}