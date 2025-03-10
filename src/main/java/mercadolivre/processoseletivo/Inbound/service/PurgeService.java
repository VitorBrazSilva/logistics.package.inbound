package mercadolivre.processoseletivo.Inbound.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mercadolivre.processoseletivo.Inbound.entity.TrackingEvent;
import org.springframework.stereotype.Service;
import mercadolivre.processoseletivo.Inbound.repository.TrackingEventRepository;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PurgeService {

    private static final String BACKUP_DIR = "backups/";
    private final TrackingEventRepository trackingEventRepository;

    public List<TrackingEvent> findByEventsOld(LocalDateTime limit) {
        return trackingEventRepository.findByCreatedAtBefore(limit);
    }

    public void saveBackup(List<TrackingEvent> events) {
        try {
            Files.createDirectories(Paths.get(BACKUP_DIR));
            String nameFile = BACKUP_DIR + "backup-" + LocalDate.now() + ".csv";

            FileWriter fileWriter = new FileWriter(nameFile, true);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(writer);

            if (Files.size(Paths.get(nameFile)) == 0){
                printWriter.println("id,shipping_package_id,location,description,createdAt");
            }
            for (TrackingEvent e : events) {
                printWriter.println(String.format("%s,%s,%s,%s,%s",
                        e.getId(), e.getShippingPackage().getId(), e.getLocation(),
                        e.getDescription(), e.getCreatedAt()));
            }


            printWriter.flush();
            log.info("📄 Backup saved in: {}", nameFile);

        } catch (IOException e) {
            log.error("❌ Error saving backup!", e);
        }
    }


    public void deletedyEventsOld(LocalDateTime limit) {
        trackingEventRepository.deleteByCreatedAtBefore(limit);
    }

}