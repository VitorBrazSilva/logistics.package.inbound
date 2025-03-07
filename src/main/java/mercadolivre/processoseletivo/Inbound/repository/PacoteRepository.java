package mercadolivre.processoseletivo.Inbound.repository;

import mercadolivre.processoseletivo.Inbound.entity.Pacote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PacoteRepository extends JpaRepository<Pacote, UUID> {
    List<Pacote> findBySender(String sender);
    List<Pacote> findByRecipient(String recipient);
}