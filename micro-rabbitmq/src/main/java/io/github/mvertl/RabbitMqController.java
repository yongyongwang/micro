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

    @GetMapping("/send/message")
    public void sendMessage(@RequestParam("msg")String msg){
        createOrderOutStream.notifyCreateOrder().send(MessageBuilder.withPayload(msg).build());
    }

}
