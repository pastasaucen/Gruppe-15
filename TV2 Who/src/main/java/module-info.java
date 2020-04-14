module gruppe15 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens gruppe15 to javafx.fxml;
    exports gruppe15;
}