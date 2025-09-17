/*
Metodos que permiten a las clases comunicarse
 */

package org.example.avanceproyecto.ControllerUtils;
import org.example.avanceproyecto.Tarea.TareaNodo;
import org.example.avanceproyecto.Tarea.TipoTarea;

import java.util.ArrayList;

public interface Observer {
    default boolean show_layout(Class<?> clazz) {
        return false;
    }
    default ArrayList<TareaNodo> get_node_tarea_array(TipoTarea tipoTarea){return null;}
    default void tareaTerminada(TipoTarea tipoTarea){};
    void init();
    default void updateSecondsInTable(int seconds){};
    default void tarea_creada(TareaNodo tareaNodo){};
    default void pre_close_request(){}
}
