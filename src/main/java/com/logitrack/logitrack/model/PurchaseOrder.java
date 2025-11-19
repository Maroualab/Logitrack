package com.logitrack.logitrack.model;


import com.logitrack.logitrack.model.enums.PurchaseOrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "purchase_orders")
@Getter
@Setter
@NoArgsConstructor
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(nullable = false)
    private PurchaseOrderStatus status;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime expectedDeliveryDate;


    @OneToMany(mappedBy = "purchaseOrder",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY )
    private List<PurchaseOrderLine> orderLines = new ArrayList<>();


    public void addLine(PurchaseOrderLine line) {
        orderLines.add(line);
        line.setPurchaseOrder(this);
    }

    public void removeLine(PurchaseOrderLine line) {
        orderLines.remove(line);
        line.setPurchaseOrder(null);
    }
}