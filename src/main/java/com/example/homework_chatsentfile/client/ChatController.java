package com.example.homework_chatsentfile.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ChatController implements Initializable {
    @FXML
    public ListView<String> listView;
    @FXML
    public TextField input;
    @FXML
    public ListView statuses;
    private Path clientDir;
    private IoNet net;
    private byte[] buf;
    private final static int SIZE = 8192;

    @FXML
    public void SendFile(ActionEvent actionEvent) throws IOException {
        String fileName = input.getText();
        Path file = clientDir.resolve(fileName);
        net.writeUTF("/sendFile");
        net.writeUTF(fileName.replaceAll(" +", "_"));
        net.writeLong(Files.size(file));
        try (FileInputStream fileInputStream = new FileInputStream(file.toFile())) {
            int read;
            while ((read = fileInputStream.read(buf)) != -1) {
                net.writeBytes(buf, 0, read);
            }
        }
    }

    private void addStatus(String message) {
        Platform.runLater(() -> statuses.getItems().add(message));
    }

    private void initClickListener() {
        listView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                String item = listView.getSelectionModel().getSelectedItem();
                input.setText(item);
            }
        });
    }

    private void fillFileView() throws IOException {
        List<String> files = Files.list(clientDir).map(p -> p.getFileName().toString()).collect(Collectors.toList());
        listView.getItems().addAll(files);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            buf = new byte[SIZE];
            clientDir = Paths.get("client");
            fillFileView();
            initClickListener();
            Socket socket = new Socket("localhost", 8189);
            net= new IoNet(this::addStatus, socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}