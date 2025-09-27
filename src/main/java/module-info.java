module org.example.avanceproyecto {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires org.json;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires static lombok;
    requires javafx.base;
    requires javafx.graphics;
    requires com.fasterxml.jackson.databind;
    requires jdk.jshell;

    opens org.example.avanceproyecto to javafx.fxml;
    exports org.example.avanceproyecto;
    exports org.example.avanceproyecto.Tarea;
    opens org.example.avanceproyecto.Tarea to javafx.fxml;
    exports org.example.avanceproyecto.ControllerUtils;
    opens org.example.avanceproyecto.ControllerUtils to javafx.fxml;
    exports org.example.avanceproyecto.Controllers;
    opens org.example.avanceproyecto.Controllers to javafx.fxml;
    exports org.example.avanceproyecto.Controllers.Proyecto;
    opens org.example.avanceproyecto.Controllers.Proyecto to javafx.fxml;
}