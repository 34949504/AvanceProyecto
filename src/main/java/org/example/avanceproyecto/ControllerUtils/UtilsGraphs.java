package org.example.avanceproyecto.ControllerUtils;

import org.example.avanceproyecto.Controllers.Proyecto.objects.ProyectoObject;
import org.example.avanceproyecto.Controllers.Proyecto.objects.ProyectoObjectCreados;
import org.example.avanceproyecto.Controllers.Proyecto.objects.TareaObject;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Funciones de utileria para los grafos
 */
public class UtilsGraphs {
    
    // Get all graphs from the map
    public static Collection<Graph<Object, DefaultEdge>> getAllGraphs(Map<String, Graph<Object, DefaultEdge>> projectGraphs) {
        return projectGraphs.values();
    }
    
    // Get all project names/keys from the map
    public static Set<String> getAllProjectNames(Map<String, Graph<Object, DefaultEdge>> projectGraphs) {
        return projectGraphs.keySet();
    }
    
    // Get a specific graph by project name
    public static Graph<Object, DefaultEdge> getGraphForProject(Map<String, Graph<Object, DefaultEdge>> projectGraphs, String projectName) {
        return projectGraphs.get(projectName);
    }
    
    // Get ProyectoObject from a specific graph
    public static ProyectoObject getProyectoObjectFromGraph(Graph<Object, DefaultEdge> graph) {
        return graph.vertexSet().stream()
                .filter(ProyectoObject.class::isInstance)
                .map(ProyectoObject.class::cast)
                .findFirst()
                .orElse(null);
    }
    
    // Get ProyectoObject by project name from the map
    public static ProyectoObject getProyectoObject(Map<String, Graph<Object, DefaultEdge>> projectGraphs, String projectName) {
        Graph<Object, DefaultEdge> graph = projectGraphs.get(projectName);
        if (graph != null) {
            return getProyectoObjectFromGraph(graph);
        }
        return null;
    }
    
    // Get all ProyectoObjects from all graphs in the map
    public static <T extends ProyectoObject> List<T> getAllProyectoObjects(
            Map<String, Graph<Object, DefaultEdge>> projectGraphs,
            Class<T> type) {

        return projectGraphs.values().stream()
                .map(UtilsGraphs::getProyectoObjectFromGraph) // returns ProyectoObject
                .filter(Objects::nonNull)
                .map(type::cast)                              // runtime type check
                .collect(Collectors.toList());
    }

    // Get all tasks from a specific graph
    public static List<TareaObject> getAllTasksFromGraph(Graph<Object, DefaultEdge> graph) {
        return graph.vertexSet().stream()
                .filter(TareaObject.class::isInstance)
                .map(TareaObject.class::cast)
                .collect(Collectors.toList());
    }
    
    // Get tasks for a specific project from a graph
    public static List<TareaObject> getTasksForProject(Graph<Object, DefaultEdge> graph, ProyectoObjectCreados proyecto) {
        return graph.outgoingEdgesOf(proyecto).stream()
                .map(graph::getEdgeTarget)
                .filter(TareaObject.class::isInstance)
                .map(TareaObject.class::cast)
                .collect(Collectors.toList());
    }
    
    // Get project for a specific task from a graph
    public static ProyectoObjectCreados getProjectForTask(Graph<Object, DefaultEdge> graph, TareaObject tarea) {
        return graph.incomingEdgesOf(tarea).stream()
                .map(graph::getEdgeSource)
                .filter(ProyectoObjectCreados.class::isInstance)
                .map(ProyectoObjectCreados.class::cast)
                .findFirst()
                .orElse(null);
    }
    
    // Check if project exists in the map
    public static boolean hasProject(Map<String, Graph<Object, DefaultEdge>> projectGraphs, String projectName) {
        return projectGraphs.containsKey(projectName);
    }
    
    // Remove a project graph from the map
    public static Graph<Object, DefaultEdge> removeProjectGraph(Map<String, Graph<Object, DefaultEdge>> projectGraphs, String projectName) {
        return projectGraphs.remove(projectName);
    }
    
}