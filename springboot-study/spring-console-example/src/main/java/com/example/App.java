package com.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        // 获取 resources 中的 Beans.xml 到上下文 context
        ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
        // 获取在 Beans.xml 中定义的 id 为 helloWorld 的 bean
        HelloWorld obj = (HelloWorld) context.getBean("helloWorld");
        // 使用获得的 bean 的 getMessage 方法
        obj.getMessage();
        // 关闭上下文，防止内存泄漏
        ((ClassPathXmlApplicationContext) context).close();
    }
}
