package com.example.kafkademo.kafka;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 自定义重试
 *
 * @Author: yu.zhang7
 * @Date: 2021/3/21
 */
@Component
public class DemoProducer3 {

    private KafkaProducer producer;

    @Value("test2")
    private String topic;

    private ThreadPoolExecutor threadPoolExecutor;

    @PostConstruct
    private void init() {
        Properties properties = KafkaConfiguration.initConfig();
        properties.put(ProducerConfig.RETRIES_CONFIG, 0);
        producer = new KafkaProducer(properties);

        int threadNum = Runtime.getRuntime().availableProcessors() + 1;
        int queueCapacity = 10000;
        threadPoolExecutor =
            new ThreadPoolExecutor(threadNum, threadNum, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(queueCapacity),
                new ThreadFactoryBuilder().setNameFormat(topic + "-retry-send-%d").build(),
                new CallerRunsPolicy());
    }

    public void sendMsg(String value) {
        String key = "333333_" + value;
        byte[] keyByte = key.getBytes();
        ProducerRecord producerRecord = new ProducerRecord(topic, keyByte, value.getBytes());
        producer.send(producerRecord, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (exception != null) {
                    exception.printStackTrace();
                    threadPoolExecutor.execute(() -> {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        sendMsg(value);
                    });
                } else {
                    System.out.println(metadata.topic() + "-" + metadata.partition() + ":" + metadata.offset());
                }
            }
        });
    }
}
