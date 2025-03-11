package mercadolivre.processoseletivo.Inbound;

import mercadolivre.processoseletivo.Inbound.controller.dto.TrackingEventRequestDto;
import mercadolivre.processoseletivo.Inbound.entity.ShippingPackage;
import mercadolivre.processoseletivo.Inbound.entity.TrackingEvent;
import mercadolivre.processoseletivo.Inbound.repository.ShippingPackageRepository;
import mercadolivre.processoseletivo.Inbound.repository.TrackingEventRepository;
import mercadolivre.processoseletivo.Inbound.service.TrackingEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrackingEventServiceTest {

    @Mock
    private ShippingPackageRepository shippingPackageRepository;

    @Mock
    private TrackingEventRepository trackingEventRepository;

    @InjectMocks
    private TrackingEventService trackingEventService;

    private TrackingEventRequestDto requestDto;
    private ShippingPackage shippingPackage;

    @BeforeEach
    @DisplayName("Setup test data before each test")
    void setUp() {
        UUID packageId = UUID.randomUUID();

        requestDto = new TrackingEventRequestDto();
        requestDto.setId(packageId);
        requestDto.setLocation("Centro de Distribuição");
        requestDto.setDescription("Pacote chegou ao centro");

        shippingPackage = new ShippingPackage();
        shippingPackage.setId(packageId);
        shippingPackage.setDescription("Livros para entrega");
        shippingPackage.setSender("Mercado Livre");
        shippingPackage.setRecipient("João Silva");
        shippingPackage.setCreatedAt(LocalDateTime.now());
        shippingPackage.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should create a tracking event successfully")
    void createEvent_ShouldCreateTrackingEventSuccessfully() {
        when(shippingPackageRepository.findById(requestDto.getId())).thenReturn(Optional.of(shippingPackage));

        trackingEventService.createEvent(requestDto);

        verify(trackingEventRepository, times(1)).save(any(TrackingEvent.class));
    }

    @Test
    @DisplayName("Should throw RuntimeException when package is not found")
    void createEvent_ShouldThrowException_WhenPackageNotFound() {
        when(shippingPackageRepository.findById(requestDto.getId())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> trackingEventService.createEvent(requestDto));

        assertEquals("Package not found", exception.getMessage());
        verify(trackingEventRepository, never()).save(any(TrackingEvent.class));
    }
}