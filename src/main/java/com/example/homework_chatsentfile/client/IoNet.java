package com.example.homework_chatsentfile.client;


import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class IoNet implements Closeable {
    private final CallBack callback;
    private final Socket socket;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;
    private final byte[] buf;
    private final static int SIZE = 8192;

    public IoNet(CallBack callback, Socket socket) throws IOException{
        this.callback = callback;
        this.socket = socket;
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
        buf = new byte[SIZE];
        Thread readThread = new Thread(this::readMessages);
        readThread.setDaemon(true);
        readThread.start();
    }

    public void writeUTF(String msg) throws IOException{
        outputStream.writeUTF(msg);
        outputStream.flush();
    }

    public void writeLong(long size) throws IOException{
        outputStream.writeLong(size);
        outputStream.flush();
    }

    public void writeBytes(byte[] bytes, int off, int cnt) throws IOException{
        outputStream.write(bytes, off, cnt);
        outputStream.flush();
    }

    private void readMessages() {
        try{
            while(true){
                String message = inputStream.readUTF();
                callback.onReceive(message);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        outputStream.close();
        inputStream.close();
        socket.close();
    }
}
