package cn.huwhy.nose;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class NoseApp {
    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(AppConfig.class).registerShutdownHook();
        System.out.println("started");
    }
}
