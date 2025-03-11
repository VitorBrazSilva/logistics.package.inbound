package mercadolivre.processoseletivo.Inbound;

import jakarta.persistence.EntityNotFoundException;
import mercadolivre.processoseletivo.Inbound.client.DogApiClient;
import mercadolivre.processoseletivo.Inbound.client.dto.dogApi.DogFactAttributes;
import mercadolivre.processoseletivo.Inbound.client.dto.dogApi.DogFactData;
import mercadolivre.processoseletivo.Inbound.client.dto.dogApi.DogFactResponseDto;
import mercadolivre.processoseletivo.Inbound.client.dto.holidayDto.HolidayResponseDto;
import mercadolivre.processoseletivo.Inbound.config.RabbitMQConfig;
import mercadolivre.processoseletivo.Inbound.controller.dto.ShippingPackageEventsResponseDto;
import mercadolivre.processoseletivo.Inbound.controller.dto.ShippingPackageRequestDto;
import mercadolivre.processoseletivo.Inbound.controller.dto.ShippingPackageResponseDto;
import mercadolivre.processoseletivo.Inbound.entity.ShippingPackage;
import mercadolivre.processoseletivo.Inbound.entity.TrackingEvent;
import mercadolivre.processoseletivo.Inbound.enums.ShippingPackageStatus;
import mercadolivre.processoseletivo.Inbound.mapper.ShippingPackageMapper;
import mercadolivre.processoseletivo.Inbound.repository.ShippingPackageRepository;
import mercadolivre.processoseletivo.Inbound.repository.TrackingEventRepository;
import mercadolivre.processoseletivo.Inbound.service.HollidaysService;
import mercadolivre.processoseletivo.Inbound.service.RabbitMQMessagePublisher;
import mercadolivre.processoseletivo.Inbound.service.ShippingPackageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShippingPackageServiceTest {

    @Mock
    private ShippingPackageRepository shippingPackageRepository;

    @Mock
    private DogApiClient dogApiClient;

    @Mock
    private ShippingPackageMapper shippingPackageMapper;

    @Mock
    private TrackingEventRepository trackingEventRepository;

    @Mock
    private RabbitMQMessagePublisher rabbitMQMessagePublisher;

    @Mock
    private HollidaysService hollidaysService;


    @Spy
    @InjectMocks
    private ShippingPackageService shippingPackageService;

    private ShippingPackageRequestDto requestDto;
    private ShippingPackage shippingPackage;
    private TrackingEvent trackingEvent;
    private HolidayResponseDto holidayResponseDto;
    private UUID packageId;

    @BeforeEach
    @DisplayName("ShippingPackage Service Tests")
    void setUp() {
        requestDto = new ShippingPackageRequestDto();
        requestDto.setDescription("Livros para entrega");
        requestDto.setSender("Mercado Livre");
        requestDto.setRecipient("João Silva");
        requestDto.setEstimatedDeliveryDate(LocalDate.now().plusDays(5));

        packageId = UUID.randomUUID();
        shippingPackage = new ShippingPackage();
        shippingPackage.setId(packageId);
        shippingPackage.setDescription("Livros para entrega");
        shippingPackage.setSender("Mercado Livre");
        shippingPackage.setRecipient("João Silva");
        shippingPackage.setStatus(ShippingPackageStatus.CREATED);
        shippingPackage.setCreatedAt(LocalDateTime.now());
        shippingPackage.setUpdatedAt(LocalDateTime.now());
        shippingPackage.setEstimatedDeliveryDate(LocalDate.of(2025, 12, 25));


        trackingEvent = new TrackingEvent();
        trackingEvent.setId(packageId);
        trackingEvent.setShippingPackage(shippingPackage);
        trackingEvent.setLocation("Centro de Distribuição");
        trackingEvent.setDescription("Pacote chegou ao centro");
        trackingEvent.setCreatedAt(LocalDateTime.now());

        holidayResponseDto = new HolidayResponseDto();
        holidayResponseDto.setDate(LocalDate.of(2025, 12, 25));
        holidayResponseDto.setLocalName("Natal");


    }

    @Test
    @DisplayName("Should successfully create a shipping package")
    void createShippingPackageService_ShouldSavePackageSuccessfully() {
        when(shippingPackageMapper.toEntity(requestDto)).thenReturn(shippingPackage);
        when(shippingPackageRepository.save(shippingPackage)).thenReturn(shippingPackage);

        ShippingPackageResponseDto response = shippingPackageService.createShippingPackageService(requestDto);

        assertNotNull(response);
        assertEquals(shippingPackage.getId(), response.getId());
        assertEquals("Livros para entrega", response.getDescription());
        assertEquals(ShippingPackageStatus.CREATED, response.getStatus());

        assertNotNull(response.getCreatedAt());
        assertNotNull(response.getUpdatedAt());

        verify(shippingPackageRepository, times(1)).save(shippingPackage);
        verify(rabbitMQMessagePublisher, times(1)).sendPackageCreated(
                any(ShippingPackage.class),
                anyString(),
                anyString()
        );
    }

    @Test
    @DisplayName("Should throw exception when request is null")
    void createShippingPackageService_ShouldThrowException_WhenRequestDtoIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> shippingPackageService.createShippingPackageService(null));

        assertEquals("Request cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when ShippingPackageMapper returns null")
    void createShippingPackageService_ShouldThrowException_WhenMapperReturnsNull() {
        when(shippingPackageMapper.toEntity(requestDto)).thenReturn(null);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> shippingPackageService.createShippingPackageService(requestDto));

        assertEquals("Failed to map request to entity", exception.getMessage());
    }

    @Test
    @DisplayName("Should trigger async RabbitMQ message when a package is created")
    void createShippingPackageService_ShouldCallSendPackageCreatedAsync() {
        when(shippingPackageMapper.toEntity(requestDto)).thenReturn(shippingPackage);
        when(shippingPackageRepository.save(shippingPackage)).thenReturn(shippingPackage);

        shippingPackageService.createShippingPackageService(requestDto);

        verify(shippingPackageRepository, times(1)).save(shippingPackage);
        verify(shippingPackageService, times(1)).sendPackageCreatedAsync(shippingPackage);
    }

    @Test
    @DisplayName("Should update status from IN_TRANSIT to DELIVERED successfully and set deliveredAt")
    void updateStatus_ShouldUpdateStatusToDelivered() {
        shippingPackage.setStatus(ShippingPackageStatus.IN_TRANSIT);
        when(shippingPackageRepository.findById(packageId)).thenReturn(Optional.of(shippingPackage));
        when(shippingPackageRepository.save(any())).thenReturn(shippingPackage);

        ShippingPackageResponseDto response = shippingPackageService.updateStatus(packageId, ShippingPackageStatus.DELIVERED);

        assertNotNull(response);
        assertEquals(ShippingPackageStatus.DELIVERED, shippingPackage.getStatus());
        assertNotNull(shippingPackage.getUpdatedAt());
        assertNotNull(shippingPackage.getDeliveredAt());

        verify(shippingPackageRepository, times(1)).save(shippingPackage);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when package does not exist")
    void updateStatus_ShouldThrowException_WhenPackageNotFound() {
        when(shippingPackageRepository.findById(packageId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> shippingPackageService.updateStatus(packageId, ShippingPackageStatus.IN_TRANSIT));

        assertEquals("Package not found", exception.getMessage());
        verify(shippingPackageRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw IllegalStateException when status transition is invalid")
    void updateStatus_ShouldThrowException_WhenInvalidTransition() {
        shippingPackage.setStatus(ShippingPackageStatus.DELIVERED);
        when(shippingPackageRepository.findById(packageId)).thenReturn(Optional.of(shippingPackage));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> shippingPackageService.updateStatus(packageId, ShippingPackageStatus.IN_TRANSIT));

        assertEquals("Invalid status transition", exception.getMessage());
        verify(shippingPackageRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should return package details without events when includeEvents=false")
    void getShippingPackage_ShouldReturnPackageWithoutEvents_WhenIncludeEventsIsFalse() {
        when(shippingPackageRepository.findById(packageId)).thenReturn(Optional.of(shippingPackage));

        ShippingPackageEventsResponseDto response = shippingPackageService.getShippingPackage(packageId, false, 0, 10);

        assertNotNull(response);
        assertEquals(packageId, response.getId());
        assertEquals("Livros para entrega", response.getDescription());
        assertTrue(response.getEvents().isEmpty());

        verify(trackingEventRepository, never()).findByShippingPackage(any(), any());
    }

    @Test
    @DisplayName("Should return package details with paginated events when includeEvents=true")
    void getShippingPackage_ShouldReturnPackageWithEvents_WhenIncludeEventsIsTrue() {
        when(shippingPackageRepository.findById(packageId)).thenReturn(Optional.of(shippingPackage));

        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        Page<TrackingEvent> eventPage = new PageImpl<>(List.of(trackingEvent), pageable, 1);

        when(trackingEventRepository.findByShippingPackage(shippingPackage, pageable)).thenReturn(eventPage);

        ShippingPackageEventsResponseDto response = shippingPackageService.getShippingPackage(packageId, true, 0, 10);

        assertNotNull(response);
        assertEquals(packageId, response.getId());
        assertEquals("Livros para entrega", response.getDescription());
        assertFalse(response.getEvents().isEmpty());
        assertEquals(1, response.getEvents().size());
        assertEquals("Centro de Distribuição", response.getEvents().getFirst().getLocation());

        verify(trackingEventRepository, times(1)).findByShippingPackage(shippingPackage, pageable);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when package does not exist")
    void getShippingPackage_ShouldThrowException_WhenPackageNotFound() {
        when(shippingPackageRepository.findById(packageId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> shippingPackageService.getShippingPackage(packageId, true, 0, 10));

        assertEquals("Package not found", exception.getMessage());
        verify(trackingEventRepository, never()).findByShippingPackage(any(), any());
    }

    @Test
    @DisplayName("Should return a list of packages when packages exist")
    void listPackage_ShouldReturnListOfPackages() {

        ShippingPackage shippingPackage2 = new ShippingPackage();
        shippingPackage2.setId(packageId);
        shippingPackage2.setDescription("Peças Automotivas");
        shippingPackage2.setSender("Auto Peças");
        shippingPackage2.setRecipient("Emilio Varjão");
        shippingPackage2.setStatus(ShippingPackageStatus.IN_TRANSIT);
        shippingPackage2.setCreatedAt(LocalDateTime.now());
        shippingPackage2.setUpdatedAt(LocalDateTime.now());
        when(shippingPackageRepository.filterShippingPackage(null, null)).thenReturn(List.of(shippingPackage, shippingPackage2));

        List<ShippingPackageResponseDto> response = shippingPackageService.listPackage(null, null);

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("Livros para entrega", response.get(0).getDescription());
        assertEquals("Peças Automotivas", response.get(1).getDescription());

        verify(shippingPackageRepository, times(1)).filterShippingPackage(null, null);
    }

    @Test
    @DisplayName("Should return an empty list when no packages match the filter")
    void listPackage_ShouldReturnEmptyList_WhenNoPackagesFound() {
        when(shippingPackageRepository.filterShippingPackage("NonExistentSender", "NonExistentRecipient")).thenReturn(List.of());

        List<ShippingPackageResponseDto> response = shippingPackageService.listPackage("NonExistentSender", "NonExistentRecipient");

        assertNotNull(response);
        assertTrue(response.isEmpty());

        verify(shippingPackageRepository, times(1)).filterShippingPackage("NonExistentSender", "NonExistentRecipient");
    }

    @Test
    @DisplayName("Should return filtered packages when sender and recipient are provided")
    void listPackage_ShouldReturnFilteredPackages() {
        when(shippingPackageRepository.filterShippingPackage("Auto Peças", "Emilio Varjão")).thenReturn(List.of(shippingPackage));

        List<ShippingPackageResponseDto> response = shippingPackageService.listPackage("Auto Peças", "Emilio Varjão");

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Livros para entrega", response.getFirst().getDescription());

        verify(shippingPackageRepository, times(1)).filterShippingPackage("Auto Peças", "Emilio Varjão");
    }

    @Test
    @DisplayName("Should update isHoliday field and send RabbitMQ message")
    void updateHolliday_ShouldUpdateHolidayFieldAndSendRabbitMQMessage() {
        when(hollidaysService.getHolidays(2025, "BR")).thenReturn(List.of(holidayResponseDto));

        shippingPackageService.updateHolliday(shippingPackage);

        assertTrue(shippingPackage.getIsHoliday());
        verify(shippingPackageRepository, times(1)).save(shippingPackage);
        verify(rabbitMQMessagePublisher, times(1)).sendPackageCreated(
                shippingPackage, RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_FUNFACT
        );
    }

    @Test
    @DisplayName("Should update isHoliday field to false when estimated delivery date is not a holiday")
    void updateHolliday_ShouldSetIsHolidayToFalse_WhenNotHoliday() {
        when(hollidaysService.getHolidays(2025, "BR")).thenReturn(List.of());

        shippingPackageService.updateHolliday(shippingPackage);

        assertFalse(shippingPackage.getIsHoliday());
        verify(shippingPackageRepository, times(1)).save(shippingPackage);
        verify(rabbitMQMessagePublisher, times(1)).sendPackageCreated(
                shippingPackage, RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_FUNFACT
        );
    }

    @Test
    @DisplayName("Should update funFact field and save package")
    void updateFunFact_ShouldUpdateFunFactAndSavePackage() {
        DogFactAttributes attributes = new DogFactAttributes();
        attributes.setBody("Dogs have three eyelids.");

        DogFactData factData = new DogFactData();
        factData.setId(UUID.randomUUID().toString());
        factData.setType("fact");
        factData.setAttributes(attributes);

        DogFactResponseDto mockResponse = new DogFactResponseDto();
        mockResponse.setData(List.of(factData));

        when(dogApiClient.getRandomDogFact(1)).thenReturn(mockResponse);

        shippingPackageService.updateFunFact(shippingPackage);

        assertNotNull(shippingPackage.getFunFact());
        assertEquals("Dogs have three eyelids.", shippingPackage.getFunFact());
        verify(shippingPackageRepository, times(1)).save(shippingPackage);
    }
}