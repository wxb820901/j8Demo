package com.bill.demo.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by WANGBIL on 7/25/2016.
 */


@EnableAutoConfiguration
@ComponentScan
public class ExampleApp {


    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(ExampleApp.class);
        app.addListeners(
                event -> {
                    System.out.println("====>");
                    System.out.println("====>====>");
                    System.out.println("====>====>====>");
                    System.out.println("====>====>====>====>");
                    System.out.println("====>====>====>====>====>" + event.getClass().getName());
                    System.out.println("====>====>====>====>");
                    System.out.println("====>====>====>");
                    System.out.println("====>====>");
                    System.out.println("====>");
                }
        );
        app.run(args);
    }
}
