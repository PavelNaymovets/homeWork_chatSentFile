package com.example.homework_chatsentfile.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Handler implements Runnable {
    private final static int SIZE = 8192;
    private Path serverDir;
    private boolean running;
    private final byte[] buf;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;
    private final Socket socket;


    public Handler(Socket socket) throws IOException {
        running = true;
        buf = new byte[SIZE];
        this.socket = socket;
        serverDir = Paths.get("server");
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        try{
            while(running){
                String command = inputStream.readUTF();
                if(command.equals("/exit")){
                    outputStream.writeUTF("Client disconnected");
                    close();
                    break;
                } else if(command.equals("/sendFile")){
                    String fileName = inputStream.readUTF();
                    long size = inputStream.readLong();
                    try(FileOutputStream fileOutputStream = new FileOutputStream((serverDir.resolve(fileName).toFile()))){
                        outputStream.writeUTF("File " + fileName + " created");
                        for(int i = 0; i < (size + SIZE -1)/SIZE; i++){
                            int read = inputStream.read(buf);
                            fileOutputStream.write(buf, 0, read);
                            outputStream.writeUTF("Uploaded " + (i + 1) + " batch");
                        }
                    }
                    outputStream.writeUTF("File successfully uploaded.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void close() throws IOException {
        outputStream.close();
        inputStream.close();
        socket.close();
    }
}
