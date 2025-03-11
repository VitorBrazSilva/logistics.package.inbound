package mercadolivre.processoseletivo.Inbound.entity;

import jakarta.persistence.*;
import lombok.*;
import mercadolivre.processoseletivo.Inbound.enums.ShippingPackageStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "shipping_package",
        indexes = {
                @Index(name = "idx_sender", columnList = "sender"),
                @Index(name = "idx_recipient", columnList = "recipient"),
                @Index(name = "idx_sender_recipient", columnList = "sender, recipient")
        })
public class ShippingPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String sender;

    @Column(nullable = false)
    private String recipient;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShippingPackageStatus status;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private LocalDateTime deliveredAt;

    private LocalDate estimatedDeliveryDate;

    @Column(nullable = true)
    private Boolean isHoliday;

    private String funFact;


}
