package mercadolivre.processoseletivo.Inbound.repository;

import mercadolivre.processoseletivo.Inbound.entity.ShippingPackage;
import mercadolivre.processoseletivo.Inbound.entity.TrackingEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TrackingEvenRepository extends JpaRepository<TrackingEvent, UUID> {

    List<TrackingEvent> findByShippingPackage(ShippingPackage shippingPackage);
}
