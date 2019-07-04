package com.bill.demo.j8;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class ProcessTest {
    public static void main(String args[]) throws IOException {
        String command = "C:\\Program Files\\Amazon\\AWSCLI\\bin\\aws.exe";

        waitForProcess(command);

    }

    public static void waitForProcess(String command) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        Process p = pb.start();
        while(p.isAlive()) {
            try {
                new Thread().join(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(command+" ==> over");

    }
    @Test
    public void test() throws IOException {
        String command = "C:\\Program Files\\Amazon\\AWSCLI\\bin\\aws.exe";

        waitForProcess(command);
    }
}
