package com.example.kafkademo.kafka;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

/**
 * @author: yu.zhang7
 * @created: 2021/03/19
 */
@Configuration
public class KafkaConfiguration {

    @Bean
    public KafkaTemplate<byte[], byte[]> gbBatteryProducerTemplate() {
        return new KafkaTemplate<>(gbBatteryProducerFactory());
    }

    private ProducerFactory<byte[], byte[]> gbBatteryProducerFactory() {
        return new DefaultKafkaProducerFactory<>(gbBatteryProducerConfigs());
    }

    private Map<String, Object> gbBatteryProducerConfigs() {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configMap.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);
//        configMap.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);
        configMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        configMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        configMap.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");

//        configMap.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, SecurityProtocol.SASL_PLAINTEXT.name);
//        configMap.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
//        configMap.put(SaslConfigs.SASL_JAAS_CONFIG,
//                PlainLoginModule.class.getName() + " required username=\"" + peKafkaInternalProperty.getGbBatteryClientSaslUser()
//                        + "\" password=\"" + peKafkaInternalProperty.getGbBatteryClientSaslPassword() + "\";");
        return configMap;
    }

    public static Properties initConfig() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);
//        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");
        return props;
    }
}
