package mercadolivre.processoseletivo.Inbound.consumer;

import lombok.RequiredArgsConstructor;
import mercadolivre.processoseletivo.Inbound.config.RabbitMQConfig;
import mercadolivre.processoseletivo.Inbound.entity.ShippingPackage;
import mercadolivre.processoseletivo.Inbound.service.ShippingPackageService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMQConsumer {

    private final ShippingPackageService shippingPackageService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME_HOLIDAY)
    public void consumeHolidayAdded(ShippingPackage shippingPackage) {
        shippingPackageService.updateHolliday(shippingPackage);
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME_FUNFACT)
    public void consumeFunFactAdded(ShippingPackage shippingPackage) {
        shippingPackageService.updateFunFact(shippingPackage);
    }

}