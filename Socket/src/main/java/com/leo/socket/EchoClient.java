package com.leo.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;

/**
 * <p>Date:2019/6/11.2:37 PM</p>
 * <p>Author:niu bao</p>
 * <p>Desc:</p>
 */
public class EchoClient {

    private final Socket mSocket;

    public EchoClient(String host, int port) throws IOException {
        mSocket = new Socket(host, port);
    }

    public void run() throws IOException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    readResponse();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();


        OutputStream out = mSocket.getOutputStream();
        byte[] buffer = new byte[1024];
        int n;
        while ((n = System.in.read(buffer)) > 0) {
            out.write(buffer, 0, n);
        }
    }
    public static void main(String [] args) throws IOException {

        EchoClient localhost = new EchoClient("localhost", 5849);
        localhost.run();

    }
    private void readResponse() throws IOException {
        InputStream inputStream = mSocket.getInputStream();
        byte[] buffer = new byte[1024];
        int n;
        while ((n = inputStream.read(buffer)) > 0) {
            System.out.write(buffer, 0, n);
        }
    }
}
