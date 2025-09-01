/*
Metodos que permiten a las clases comunicarse
 */

package org.example.avanceproyecto.ControllerUtils;
import javafx.scene.layout.BorderPane;
import org.example.avanceproyecto.Tarea.TareaNodo;
import org.example.avanceproyecto.Tarea.TipoTarea;

import java.util.ArrayList;

public interface Observer {
    default boolean show_layout(Class<?> clazz) {
        return false;
    }
    default ArrayList<TareaNodo> get_node_tarea_array(TipoTarea tipoTarea){return null;}
    default void updateTable(TipoTarea tipoTarea){};
    void init();
    default void updateSecondsInTable(int seconds){};
}
