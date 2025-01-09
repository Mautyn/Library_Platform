module com.example.library_platform {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires com.jfoenix;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens library_platform.Client to javafx.fxml;
    opens library_platform to javafx.fxml;

    exports library_platform.Client;
    exports library_platform.Server;
    exports library_platform.Shared;
    exports library_platform.Client.views;
    opens library_platform.Client.views to javafx.fxml;
}