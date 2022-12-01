module com.example.homework_chatsentfile {
    requires javafx.controls;
    requires javafx.fxml;
    requires io.netty.transport;
    requires io.netty.codec;


    exports com.example.homework_chatsentfile.client;
    opens com.example.homework_chatsentfile.client to javafx.fxml;
}