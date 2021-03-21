package com.example.kafkademo.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * spring-kafka 原生重试
 *
 * @Author: yu.zhang7
 * @Date: 2021/3/2
 */
@Component
public class DemoProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoProducer.class);

    @Value("test2")
    private String topic;

    @Autowired
    private KafkaTemplate<byte[], byte[]> gbBatteryProducerTemplate;

    public void sendMsg(String value) {
        String key = "1111111_" + value;
        ListenableFuture future = gbBatteryProducerTemplate.send(topic, key.getBytes(), value.getBytes());
        LOGGER.debug(
            "success to send BatteryData info, key={},topic={},payload = {},", key, topic, value);
        future.addCallback(o -> {
                LOGGER.info(
                    "success to send BatteryData:" + value);
            }
            , throwable -> {
                LOGGER.error(
                    "failed to send BatteryData:" + value, throwable);
            }
        );
    }
}
