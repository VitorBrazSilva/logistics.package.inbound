package mercadolivre.processoseletivo.Inbound.repository;

import feign.Param;
import mercadolivre.processoseletivo.Inbound.entity.ShippingPackage;
import mercadolivre.processoseletivo.Inbound.entity.TrackingEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TrackingEventRepository extends JpaRepository<TrackingEvent, UUID> {

    List<TrackingEvent> findByShippingPackage(ShippingPackage shippingPackage);
    List<TrackingEvent> findByCreatedAtBefore(LocalDateTime limit);

    @Modifying
    @Query("DELETE FROM TrackingEvent e WHERE e.createdAt < :limit")
    void deleteByCreatedAtBefore(@Param("limit") LocalDateTime limit);
}
