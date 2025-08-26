package org.example.avanceproyecto;

import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

public interface Observer {

    default public void show_mainlayout(){}
    default public void show_agregarTarea(BorderPane main_pane_borderpane){};
}
