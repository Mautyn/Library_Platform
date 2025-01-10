module com.example.library_platform {
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.desktop;
    requires java.sql;
    requires com.jfoenix;

    opens library_platform.Client to javafx.fxml;
    opens library_platform to javafx.fxml;

    exports library_platform.Client;
    exports library_platform.Server;
    exports library_platform.Shared;
    exports library_platform.Client.view;
    opens library_platform.Client.view to javafx.fxml;
    opens library_platform.Shared to javafx.fxml;
}