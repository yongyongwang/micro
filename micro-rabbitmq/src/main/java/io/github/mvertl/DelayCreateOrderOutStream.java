package io.github.mvertl;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface DelayCreateOrderOutStream {

    @Output("delay_output_create_order_task")
    MessageChannel notifyCreateOrder();

}
