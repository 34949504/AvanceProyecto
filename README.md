# AvanceProyecto

Este proyecto es una aplicación desarrollada en **JavaFX** y gestionada con **Maven**.

---

## Requisitos

- **Java 21** 
- **Maven** instalado y configurado en tu sistema

> **Nota:** No es necesario instalar JavaFX manualmente; Maven descargará todas las dependencias automáticamente.

---

## Cómo ejecutar el proyecto

### 1. Clonar el repositorio

```bash
git clone https://github.com/34949504/AvanceProyecto
cd AvanceProyecto
```

### 2. Compilar y ejecutar usando Maven

```bash
mvn clean javafx:run



# Algoritmos

## Grafos

Los utilicé para crear una relación entre los objetos **proyectos**, **tareas**, y **empleados**, aunque me parece que era super innecesario y solo agrega complejidad tonta al programa.


Los grafos son buenos para crear relaciones de **redes sociales** y conexiones grandes de objetos.

Utilicé la librería [JGraphT](https://jgrapht.org/).

## Árbol binario

Lo utilicé para crear un `HashMap<departamento_id, empleado_root>`, entonces tengo la cantidad de departamentos en **árboles binarios**.

Me parece que también es *overkill*; hubiera sido suficiente un `HashMap<departamento_id, Array<Empleados>>`.

## HashMaps


Los **HashMaps** los utilicé en varias ocasiones en el código.
