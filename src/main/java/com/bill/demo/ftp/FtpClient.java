package com.bill.demo.ftp;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;

public class FtpClient {

    private FTPClient ftp;

    // constructor

    private void open(String user, String password, String server) throws IOException {
        ftp = new FTPClient();

        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

        ftp.connect(server);
        int reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            throw new IOException("Exception in connecting to FTP Server");
        }

        ftp.login(user, password);
    }

    public void upload(String user, String password, String server, String remote, String path) throws IOException {
        open(user, password, server);
        InputStream inputStream = new FileInputStream(new File(path));
        ftp.appendFile(remote, inputStream);
        close();
    }
    private void close() throws IOException {
        ftp.disconnect();
    }
    public static void main(String args[]) throws IOException {
        FtpClient ftpClient = new FtpClient();
        ftpClient.upload("ftp6284044", "Password123", "43.226.149.106", "Web/JSCSS.html", "C:\\Users\\Bill_Wang\\Front\\JSCSS.html");
    }
}
