package org.example.avanceproyecto.Tarea;

public enum TipoTarea {
    Urgente,
    No_Urgente,
    Lista;

    public static TipoTarea get_enum_by_string_comparison(String type) {

        if (type.compareToIgnoreCase("Urgente") == 0) {
            return TipoTarea.Urgente;
        }
        if (type.compareToIgnoreCase("No urgente") == 0) {
            return TipoTarea.No_Urgente; // Fixed: should return NoUrgente, not Urgente
        }
        if (type.compareToIgnoreCase("listas") == 0) {
            return TipoTarea.Lista; // Fixed: should return Listas, not Urgente
        }

        // Fixed: proper exception throwing syntax
        throw new RuntimeException("Error: se está pasando un tipo que no está en TipoTarea. " +
                "Verificar que se pasa el string correcto en la función get_enum_by_string_comparison. " +
                "Valor recibido: " + type);
    }
}
