package com.example.kafkademo.kafka;

import java.util.Properties;
import javax.annotation.PostConstruct;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * kafka原生重试
 * @Author: yu.zhang7
 * @Date: 2021/3/21
 */
@Component
public class DemoProducer2 {
    private KafkaProducer producer;

    @Value("test2")
    private String topic;

    @PostConstruct
    private void init() {
        Properties properties = KafkaConfiguration.initConfig();
        producer = new KafkaProducer(properties);
    }

    public void sendMsg(String value) {
        String key = "2222222_" + value;
        byte[] keyByte = key.getBytes();
        ProducerRecord producerRecord = new ProducerRecord(topic, keyByte, value.getBytes());
        producer.send(producerRecord, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (exception != null) {
                    exception.printStackTrace();
                }else {
                    System.out.println(metadata.topic() + "-" + metadata.partition() + ":" + metadata.offset());
                }
            }
        });

    }
}
