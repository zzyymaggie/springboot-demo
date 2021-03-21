package com.example.kafkademo.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * @Author: yu.zhang7
 * @Date: 2021/3/2
 */
@Component
public class DemoProducerRunner implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoProducer.class);

    @Value("test2")
    private String topic;

    @Autowired
    private KafkaTemplate<byte[], byte[]> gbBatteryProducerTemplate;

    public void sendMsg(String value) {
        String key = "1111111";
        String payload = "{}";
        byte[] keyByte = buildKey(key);
        ListenableFuture future = gbBatteryProducerTemplate.send(topic, keyByte, value.getBytes());
        LOGGER.debug(
            "success to send BatteryData info, key={},topic={},payload = {},", key, topic, payload);
        future.addCallback(o -> {
            LOGGER.info(
                "success to send BatteryData");
        }, throwable -> {
            LOGGER.error(
                "failed to send BatteryData", throwable);
        });

    }

    private byte[] buildKey(String keyStr) {
        return keyStr.getBytes();
    }


    @Override
    public void run(String... args) throws Exception {
        sendMsg("start2");
    }
}
