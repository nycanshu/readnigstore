module org.example.readnigstore {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires static lombok;
    requires java.sql;
    requires mysql.connector.j;

    opens org.example.readnigstore to javafx.fxml;
    exports org.example.readnigstore;
    exports org.example.readnigstore.Controller;
    opens org.example.readnigstore.Controller to javafx.fxml;

    // Add exports and opens for Helper and Service packages to enable testing
    exports org.example.readnigstore.Helper; // allows access to Helper package
    exports org.example.readnigstore.Service;

    opens org.example.readnigstore.Helper to org.mockito;  // allows Mockito to use reflection
}
