module org.example {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.clefia to javafx.fxml;
    exports org.clefia;
}