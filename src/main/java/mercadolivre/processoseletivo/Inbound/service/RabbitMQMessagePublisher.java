package mercadolivre.processoseletivo.Inbound.service;

import mercadolivre.processoseletivo.Inbound.entity.ShippingPackage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQMessagePublisher {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQMessagePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendPackageCreated(ShippingPackage shippingPackage, String EXCHANGE_NAME, String ROUTING_KEY) {
        rabbitTemplate.convertAndSend(
                EXCHANGE_NAME,
                ROUTING_KEY,
                shippingPackage
        );
    }
}