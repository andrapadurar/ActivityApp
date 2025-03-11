module com.example.practic {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.practic to javafx.fxml;
    exports com.example.practic;
}