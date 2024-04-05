package io.github.mvertl;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableBinding({DeathCreateOrderInStream.class
//        , DelayCreateOrderInStream.class
        , CreateOrderInStream.class
        , DelayCreateOrderOutStream.class
        , CreateOrderOutStream.class})
@EnableRabbit
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class);
    }
}