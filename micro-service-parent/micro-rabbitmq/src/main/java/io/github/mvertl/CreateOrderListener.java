package io.github.mvertl;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class CreateOrderListener {

    private static final Logger logger= LoggerFactory.getLogger(CreateOrderListener.class);

    @StreamListener("input_create_order_task")
    public void createOrder(String msg, @Header(AmqpHeaders.CHANNEL) Channel channel,
                            @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag){
        logger.info(msg);
    }

}
