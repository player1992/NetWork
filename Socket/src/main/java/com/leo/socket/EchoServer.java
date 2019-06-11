package com.leo.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * <p>Date:2019/6/11.2:37 PM</p>
 * <p>Author:niu bao</p>
 * <p>Desc:</p>
 */
public class EchoServer {
    private final ServerSocket mServerSocket;

    public EchoServer(int port) throws IOException {
        mServerSocket = new ServerSocket(port);
    }

    public void run() throws IOException {
        Socket client = mServerSocket.accept();
        handleClient(client);
    }

    private void handleClient(Socket client) throws IOException {
        InputStream inputStream = client.getInputStream();
        OutputStream outputStream = client.getOutputStream();
        byte[] buffer = new byte[1024];
        int n;
        while ((n = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, n);
            boolean keepAlive = client.getKeepAlive();
            int localPort = client.getLocalPort();
            String s = client.getLocalAddress().toString();
            System.out.println("keepAlive : " + keepAlive);
            System.out.println("localPort : " + localPort);
            System.out.println("LocalAddress : " + s);
        }
    }

    public static void main(String[] args) {
        EchoServer echoServer = null;
        try {
            echoServer = new EchoServer(5849);
            echoServer.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
