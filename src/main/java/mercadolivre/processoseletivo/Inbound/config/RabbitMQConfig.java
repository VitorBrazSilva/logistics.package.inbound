package mercadolivre.processoseletivo.Inbound.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "shipping.package.exchange";
    public static final String QUEUE_NAME_HOLIDAY = "holiday.queue";
    public static final String ROUTING_KEY_HOLIDAY = "shipping.package.created";
    public static final String QUEUE_NAME_FUNFACT = "funfact.queue";
    public static final String ROUTING_KEY_FUNFACT = "holiday.added";

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public TopicExchange shippingPackageExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue holidayQueue() {
        return new Queue(QUEUE_NAME_HOLIDAY, true);
    }

    @Bean
    public Binding holidayQueueBinding(Queue holidayQueue, TopicExchange exchange) {
        return BindingBuilder.bind(holidayQueue).to(exchange).with(ROUTING_KEY_HOLIDAY);
    }

    @Bean
    public Queue funFactQueue() {
        return new Queue(QUEUE_NAME_FUNFACT, true);
    }

    @Bean
    public Binding funFactQueueBinding(Queue funFactQueue, TopicExchange exchange) {
        return BindingBuilder.bind(funFactQueue).to(exchange).with(ROUTING_KEY_FUNFACT);
    }
}