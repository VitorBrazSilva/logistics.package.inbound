package mercadolivre.processoseletivo.Inbound.cron;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mercadolivre.processoseletivo.Inbound.entity.TrackingEvent;
import mercadolivre.processoseletivo.Inbound.service.PurgeService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class DataCleanupCron {

    private final PurgeService purgeService;

    @Transactional
    @Scheduled(fixedDelay = 20000)
    public void purgeOldEvents() {
        LocalDateTime limit = LocalDateTime.now().minusMonths(1);

        log.info("🔍 Fetching events for backup before purge...");
        List<TrackingEvent> events = purgeService.findByEventsOld(limit);

        if (events.isEmpty()) {
            log.info("✅ No old events found for purging.");
            return;
        }

        log.info("📂 Saving backup of events before deleting...");
        purgeService.saveBackup(events);

        log.info("🗑️ Deleting old events...");
        purgeService.deletedyEventsOld(limit);

        log.info("✅ Purge completed successfully!");
    }
}