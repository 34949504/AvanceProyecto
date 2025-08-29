package org.example.avanceproyecto.Controllers;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Getter @Setter
public class SharedStates {
    private AtomicBoolean thread_active = new AtomicBoolean(true);
    private AtomicInteger atomicInteger = new AtomicInteger(1);

}
