package org.example.avanceproyecto.Controllers.Proyecto.objects;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

/**
 * Clase abstracta que guarda datos en comun entre un proyectoCreado y un proyectoAsignado
 */
@Getter @Setter
public abstract class ProyectoObject {
    protected String proycto_name;  // Use protected so subclasses can access
    protected String descripcion;
    protected String fecha_de_entrega;

    public static class KEYS {
        public static final String PROYECTO_NOMBRE = "proyecto_nombre";
        public static final String DESCRIPCION = "descripcion";
        public static final String FECHA_DE_ENTREGA = "fecha_de_entrega";
        public static final String TAREAS_PROYECTO = "tareas_proyecto";
        public static final String EMPLEADO_TAREAS = "empleado_tareas";
        // Nested keys for TareaObject
        public static final String TITLE = "title";
        public static final String DESC = "desc";
    }

    // Helper method to set common fields
    protected void setBasicFields(JSONObject jsonObject) {
        this.proycto_name = jsonObject.getString(KEYS.PROYECTO_NOMBRE);
        this.descripcion = jsonObject.getString(KEYS.DESCRIPCION);
        this.fecha_de_entrega = jsonObject.getString(KEYS.FECHA_DE_ENTREGA);
    }

    // Helper method to put common fields
    protected void putBasicFields(JSONObject jsonObject) {
        jsonObject.put(KEYS.PROYECTO_NOMBRE, this.proycto_name);
        jsonObject.put(KEYS.DESCRIPCION, this.descripcion);
        jsonObject.put(KEYS.FECHA_DE_ENTREGA, this.fecha_de_entrega);
    }

    @Override
    public String toString() {
        return proycto_name;
    }
}
