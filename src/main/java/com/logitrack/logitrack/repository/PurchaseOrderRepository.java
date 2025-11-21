package com.logitrack.logitrack.repository;

import com.logitrack.logitrack.model.PurchaseOrder;
import com.logitrack.logitrack.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {


}
