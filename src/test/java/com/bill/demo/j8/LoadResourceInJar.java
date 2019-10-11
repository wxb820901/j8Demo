package com.bill.demo.j8;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import redis.clients.jedis.AdvancedBinaryJedisCommands;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class LoadResourceInJar {

    @Test
    public void test() throws IOException {
        //download properties from some jar
        File tempJson1dFile = File.createTempFile(
                "tempFile-",
                ".properties",
                new File(this.getClass().getClassLoader().getResource("").getFile()));
        //create temp file under the current classpath
        tempJson1dFile.deleteOnExit();

        InputStream inputStream
                = AdvancedBinaryJedisCommands.class.getClassLoader()
                .getResourceAsStream("META-INF/maven/redis.clients/jedis/pom.properties");
        //AdvancedBinaryJedisCommands in the same jar as META-INF/maven/redis.clients/jedis/pom.properties
        FileUtils.writeByteArrayToFile(tempJson1dFile, IOUtils.toString(inputStream).getBytes());
        inputStream.close();

    }
}
