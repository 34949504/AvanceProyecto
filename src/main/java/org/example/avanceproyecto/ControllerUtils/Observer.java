/*
Metodos que permiten a las clases comunicarse
 */

package org.example.avanceproyecto.ControllerUtils;
import org.example.avanceproyecto.Controllers.Proyecto.objects.ProyectoObjectAsignado;
import org.example.avanceproyecto.Controllers.Proyecto.objects.ProyectoObjectCreados;
import org.example.avanceproyecto.Tarea.TareaNodo;
import org.example.avanceproyecto.Tarea.TipoTarea;

import java.util.ArrayList;

/**
 * Contrato para que las controladores se comuniquen
 */
public interface Observer {
    default boolean show_layout(Class<?> clazz) {
        return false;
    }
    default ArrayList<TareaNodo> get_node_tarea_array(TipoTarea tipoTarea,Prioridad prioridad){return null;}
    default void tareaTerminada(TareaNodo tareaNodo){};

    /**
     * La funcion init sirve para que la clase inicialize sin que haya conflictos de null etc
     */
    default void init(){};
    default void updateSecondsInTable(int seconds){};
    default void tarea_creada(TareaNodo tareaNodo){};
    default void pre_close_request(){}
    default void proyecto_has_been_created(ProyectoObjectCreados proyecto){}
    default void proyecto_has_been_assigned(ProyectoObjectCreados proyectoObject, ProyectoObjectAsignado proyectoObjectAsignado){}




    /**
     * Just to change color xd
     */
}
