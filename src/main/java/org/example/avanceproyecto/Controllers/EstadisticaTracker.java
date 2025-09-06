package org.example.avanceproyecto.Controllers;

import lombok.Getter;
import lombok.Setter;
import org.example.avanceproyecto.ControllerUtils.Observer;
import org.example.avanceproyecto.Tarea.TareaNodo;
import org.json.JSONObject;

@Getter @Setter
public class EstadisticaTracker implements Observer {

    JSONObject estadistica_json;

    @Override
    public void init() {

    }


    @Override
    public void tarea_creada(TareaNodo tareaNodo) {

        System.out.println("tarea creada");

    }
}
