package com.example.kafkademo;

import com.example.kafkademo.kafka.DemoProducer;
import com.example.kafkademo.kafka.DemoProducer2;
import com.example.kafkademo.kafka.DemoProducer3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class KafkaDemoApplication2 {

    @Autowired
    private DemoProducer demoProducer;

    @Autowired
    private DemoProducer2 demoProducer2;

    @Autowired
    private DemoProducer3 demoProducer3;

    public static void main(String[] args) {
        SpringApplication.run(KafkaDemoApplication2.class, args);
    }

    @GetMapping("/send")
    public String send(@RequestParam(value = "num", defaultValue = "1") String num) {
        for (int i = 0; i < 100; i++) {
            demoProducer.sendMsg(String.valueOf(i));
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return "ok";
    }

    @GetMapping("/send-batch")
    public String sendBatch() {
        for (int i = 1000; i < 1019; i++) {
            demoProducer.sendMsg(String.valueOf(i));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return "ok";
    }

    @GetMapping("/send2")
    public String send2() {
        for (int i = 0; i < 100; i++) {
            demoProducer2.sendMsg(String.valueOf(200 + i));
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return "send2 ok";
    }

    @GetMapping("/send2-batch")
    public String send2Batch() {
        for (int i = 0; i < 50; i++) {
            demoProducer2.sendMsg(String.valueOf(2000 + i));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return "send2-batch ok";
    }

    @GetMapping("/send3")
    public String send3() {
        for (int i = 0; i < 100; i++) {
            demoProducer3.sendMsg(String.valueOf(200 + i));
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return "send3 ok";
    }

    @GetMapping("/send3-batch")
    public String send3Batch() {
        for (int i = 0; i < 50; i++) {
            demoProducer3.sendMsg(String.valueOf(3000 + i));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return "send3-batch ok";
    }

}
