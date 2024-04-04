package io.github.mvertl;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface DelayCreateOrderInStream {

    @Input("delay_input_create_order_task")
    SubscribableChannel subscribableCreateOrder();

}
