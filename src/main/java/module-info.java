module com.example.demojavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    // requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires static lombok;
    requires org.hibernate.orm.core;
    requires java.persistence;
    requires java.naming;

    opens com.n19.ltmproject to javafx.fxml;
    exports com.n19.ltmproject.client.controller;
    opens com.n19.ltmproject.client.controller to javafx.fxml;
    exports com.n19.ltmproject.client;
    opens com.n19.ltmproject.client to javafx.fxml;
    opens com.n19.ltmproject.server.model to javafx.base, org.hibernate.orm.core;
}