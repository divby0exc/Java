module com.mailservice.sendit {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.mailservice.sendit to javafx.fxml;
    exports com.mailservice.sendit;
    exports com.mailservice.sendit.mailprotocol;
    opens com.mailservice.sendit.mailprotocol to javafx.fxml;
}