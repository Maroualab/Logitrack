package com.logitrack.logitrack.controller;

import com.logitrack.logitrack.dto.salesOrder.SalesOrderDTO;
import com.logitrack.logitrack.service.SalesOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/sales-orders")
@RequiredArgsConstructor
public class SalesOrderController {

    private final SalesOrderService salesOrderService;


    @PostMapping
    public ResponseEntity<SalesOrderDTO> createSalesOrder(@Valid @RequestBody SalesOrderDTO salesOrderDTO) {
        SalesOrderDTO createdOrder = salesOrderService.createSalesOrder(salesOrderDTO);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }


    @PostMapping("/{id}/reserve")
    public ResponseEntity<SalesOrderDTO> reserveSalesOrder(@PathVariable Long id) {
        SalesOrderDTO reservedOrder = salesOrderService.reserveOrder(id);
        return ResponseEntity.ok(reservedOrder);
    }


    @PostMapping("/{id}/ship")
    public ResponseEntity<SalesOrderDTO> shipSalesOrder(@PathVariable Long id) {
        SalesOrderDTO shippedOrder = salesOrderService.shipOrder(id);
        return ResponseEntity.ok(shippedOrder);
    }

    @GetMapping("/reservedSalesOrder")
    public ResponseEntity<List<SalesOrderDTO>> getReservedSalesOrder() {
        List<SalesOrderDTO> reservedSalesOrders = salesOrderService.AllReservedSalesOrders();
        return ResponseEntity.ok(reservedSalesOrders);
    }


}