module gruppe15 {
    requires javafx.controls;
    requires javafx.fxml;

    opens gruppe15 to javafx.fxml;
    exports gruppe15;
}