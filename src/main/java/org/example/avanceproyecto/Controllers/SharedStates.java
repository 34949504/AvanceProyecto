package org.example.avanceproyecto.Controllers;
/*
Datos atomicos que permite una facil comunicacion entre dos clases como VerTareas y TaskDoer
 */

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Getter @Setter
public class SharedStates {
    private AtomicBoolean thread_active = new AtomicBoolean(true);
    private AtomicInteger speed = new AtomicInteger(1000);
    private HashMap<String, ArrayList<Integer>> empleados_ocupados = new HashMap<>();


    public SharedStates() {
       String[] departamentos = {"recursos humanos","finanzas","it","ventas","marketing"};
        for (int i = 0; i < departamentos.length; i++) {
            empleados_ocupados.put(departamentos[i],new ArrayList<>());
        }

    }

}
