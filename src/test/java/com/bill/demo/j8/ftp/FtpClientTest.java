package com.bill.demo.j8.ftp;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;


import static org.junit.Assert.assertTrue;


public class FtpClientTest {
//    @Test
//    public void givenRemoteFile_whenDownloading_thenItIsOnTheLocalFilesystem() throws IOException {
//        String ftpUrl = String.format(
//                "ftp://user:password@localhost:%d/foobar.txt", fakeFtpServer.getServerControlPort());
//
//        URLConnection urlConnection = new URL(ftpUrl).openConnection();
//        InputStream inputStream = urlConnection.getInputStream();
//        Files.copy(inputStream, new File("downloaded_buz.txt").toPath());
//        inputStream.close();
//
//        assertTrue(new File("downloaded_buz.txt").exists());
//
//        new File("downloaded_buz.txt").delete(); // cleanup
//    }
}
