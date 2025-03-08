package mercadolivre.processoseletivo.Inbound.repository;

import feign.Param;
import mercadolivre.processoseletivo.Inbound.entity.ShippingPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ShippingPackageRepository extends JpaRepository<ShippingPackage, UUID> {

    @Query("SELECT p FROM ShippingPackage p WHERE " +
            "(:sender IS NULL OR p.sender = :sender) AND " +
            "(:recipient IS NULL OR p.recipient = :recipient)")
    List<ShippingPackage> filterShippingPackage(@Param("sender") String sender,
                                                @Param("recipient") String recipient);

}