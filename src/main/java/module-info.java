module com.example.demojavafx {
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

    opens com.n19.ltmproject to javafx.fxml;
    exports com.n19.ltmproject;
    exports com.n19.ltmproject.controller;
    opens com.n19.ltmproject.controller to javafx.fxml;
}