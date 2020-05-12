module gruppe15 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires junit;
    requires org.postgresql.jdbc;

    opens presentation to javafx.fxml;
    exports presentation;
    exports test to junit;
}