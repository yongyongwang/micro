package io.github.mvertl;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface CreateOrderOutStream {

    @Output("output_create_order_task")
    MessageChannel notifyCreateOrder();

}
