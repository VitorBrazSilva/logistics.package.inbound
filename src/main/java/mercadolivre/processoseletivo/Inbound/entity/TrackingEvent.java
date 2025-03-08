package mercadolivre.processoseletivo.Inbound.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tracking_event")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrackingEvent {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "shipping_package_id", nullable = false)
    private ShippingPackage shippingPackage;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String description;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime date;

}