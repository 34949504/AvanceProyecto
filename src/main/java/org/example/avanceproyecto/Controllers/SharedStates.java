package org.example.avanceproyecto.Controllers;
/*
Datos atomicos que permite una facil comunicacion entre dos clases como VerTareas y TaskDoer
 */

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Getter @Setter
public class SharedStates {
    private AtomicBoolean thread_active = new AtomicBoolean(true);
    private AtomicInteger speed = new AtomicInteger(1000);

}
