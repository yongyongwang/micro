package io.github.mvertl;

import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class RabbitMqController {

    @Resource
    private CreateOrderOutStream createOrderOutStream;

    @Resource
    private DelayCreateOrderOutStream delayCreateOrderOutStream;

    @GetMapping("/send/message")
    public void sendMessage(@RequestParam("msg") String msg) {
        createOrderOutStream.notifyCreateOrder().send(MessageBuilder.withPayload(msg).build());
    }

    @GetMapping("/send/delay/message")
    public void sendDelayMessage(@RequestParam("msg") String msg, @RequestParam("seconds") Integer seconds) {
        delayCreateOrderOutStream.notifyCreateOrder().send(MessageBuilder.withPayload(msg)
                .setHeader("expiration", seconds)
                .setHeader("x-delay", seconds)
                .build());
    }

}
