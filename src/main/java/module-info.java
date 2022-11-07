module com.example.homework_chatsentfile {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.homework_chatsentfile to javafx.fxml;
    exports com.example.homework_chatsentfile;
}