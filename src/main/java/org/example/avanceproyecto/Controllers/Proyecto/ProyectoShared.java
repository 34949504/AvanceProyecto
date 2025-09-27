package org.example.avanceproyecto.Controllers.Proyecto;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

@Getter @Setter
public class ProyectoShared {
    private JSONObject proyectos_creados;
    private JSONObject proyectos_asignados;
}
