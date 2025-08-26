package org.example.avanceproyecto;

public class TaskAdministrator {




    public void add_task(String departamento,String tarea,String tipo_tarea) {
        TipoTarea tipoTarea = TipoTarea.get_enum_by_string_comparison(tipo_tarea);
        if (tipoTarea == TipoTarea.Urgente) {

        }
        else if (tipoTarea == TipoTarea.No_Urgente) {

        }
        else if (tipoTarea == TipoTarea.Lista) {

        }
    }

}
