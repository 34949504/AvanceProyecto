package org.example.avanceproyecto.ControllerUtils;

public enum Prioridad {

    baja,
    media,
    none,
    alta;

    public static Prioridad getPrioridad(String prioridad) {
        if (prioridad == null) return null; // handle null input
        prioridad = prioridad.toLowerCase();

        switch (prioridad) {
            case "baja":
                return Prioridad.baja;
            case "media":
                return Prioridad.media;
            case "alta":
                return Prioridad.alta;
            case "none":
                return Prioridad.none;
            default:
                throw new IllegalArgumentException("Prioridad desconocida: " + prioridad);
        }
    }
}
