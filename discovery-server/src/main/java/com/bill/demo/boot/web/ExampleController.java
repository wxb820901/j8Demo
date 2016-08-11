package com.bill.demo.boot.web;

import com.bill.demo.boot.domain.ExampleEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by WANGBIL on 7/26/2016.
 */

@RestController
@ConfigurationProperties
public class ExampleController {
    @Value("${dev.config.name}")
    String name = "World";

    @RequestMapping("/")
    public String home() {
        return "Hello World! " + name;
    }

    @RequestMapping("/demo/e1")
    public ExampleEntity getEntity() {
        return new ExampleEntity(2, "wangbil");
    }

    @RequestMapping("/demo/{param}")
    public ExampleEntity getEntity(
            @PathVariable("param") String parameter) {
        return new ExampleEntity(3, parameter);
    }
}
