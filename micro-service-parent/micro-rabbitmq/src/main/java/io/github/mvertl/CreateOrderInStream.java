package io.github.mvertl;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface CreateOrderInStream {

    @Output("input_create_order_task")
    MessageChannel notifyCreateOrder();

}
