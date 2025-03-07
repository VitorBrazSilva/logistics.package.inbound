package mercadolivre.processoseletivo.Inbound.repository;

import mercadolivre.processoseletivo.Inbound.entity.ShippingPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ShippingPackageRepository extends JpaRepository<ShippingPackage, UUID> {
    List<ShippingPackage> findBySender(String sender);
    List<ShippingPackage> findByRecipient(String recipient);
}