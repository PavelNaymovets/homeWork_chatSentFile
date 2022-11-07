module com.example.homework_chatsentfile {
    requires javafx.controls;
    requires javafx.fxml;


    exports com.example.homework_chatsentfile.client;
    opens com.example.homework_chatsentfile.client to javafx.fxml;
}