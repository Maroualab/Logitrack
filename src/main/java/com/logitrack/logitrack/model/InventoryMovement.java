package com.logitrack.logitrack.model;

import com.logitrack.logitrack.model.enums.MovementType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_movements")
@Getter
@Setter
@NoArgsConstructor
public class InventoryMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MovementType type;

    /**
     * La quantité de ce mouvement spécifique (ex: 50).
     * Note: On stocke la quantité absolue. Le 'type' (IN/OUT) dit le sens.
     */
    @Column(nullable = false)
    private int quantity;

    @CreationTimestamp // Géré automatiquement par JPA
    @Column(updatable = false)
    private LocalDateTime occurredAt;

    /**
     * Référence au document qui a causé ce mouvement.
     * (ex: "PO-123", "SO-500", "AJUST-OCT25")
     */
    private String referenceDocument;
}