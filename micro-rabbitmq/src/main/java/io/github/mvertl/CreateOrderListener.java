package io.github.mvertl;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class CreateOrderListener {

    private static final Logger logger = LoggerFactory.getLogger(CreateOrderListener.class);

    @Resource
    private CreateOrderOutStream createOrderOutStream;

    @StreamListener("input_create_order_task")
    public void createOrder(String msg, @Header(AmqpHeaders.CHANNEL) Channel channel,
                            @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag) throws IOException {
        logger.info(msg);
        if (msg.contains("死信")) {
            channel.basicNack(deliveryTag, false, false);
        } else {
            channel.basicAck(deliveryTag, false);
        }
    }

    @StreamListener("delay_input_create_order_task")
    @SendTo("output_create_order_task")
    public String delayCreateOrder(String msg, Message message, @Header(AmqpHeaders.MESSAGE_ID) String messageId
            , @Header(AmqpHeaders.CHANNEL) Channel channel
            , @Header("x-death") List<Map<String, Object>> xDeath
            , @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag) throws IOException, InterruptedException {
        logger.info("msgId:" + messageId + msg);
        if (!CollectionUtils.isEmpty(xDeath)) {
            Integer deathCount = Optional.ofNullable(xDeath.get(0).get("count")).map(String::valueOf).map(Integer::parseInt).orElse(0);
            if (deathCount > 3) {
                channel.basicAck(deliveryTag, false);
                return null;
            }
        }
        Thread.sleep(3000);
        //createOrderOutStream.notifyCreateOrder().send(message);
        return msg;
    }

}
