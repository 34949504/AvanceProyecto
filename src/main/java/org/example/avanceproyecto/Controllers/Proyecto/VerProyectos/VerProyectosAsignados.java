package org.example.avanceproyecto.Controllers.Proyecto.VerProyectos;



import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import lombok.Getter;
import lombok.Setter;
import org.example.avanceproyecto.ControllerUtils.*;
import org.example.avanceproyecto.ControllerUtils.Dialogs.VerProyectosAsignadosDialog;
import org.example.avanceproyecto.ControllerUtils.Observer;
import org.example.avanceproyecto.Controllers.Proyecto.ProyectoShared;
import org.example.avanceproyecto.Controllers.Proyecto.objects.EmpleadoTarea;
import org.example.avanceproyecto.Controllers.Proyecto.objects.ProyectoObjectAsignado;
import org.example.avanceproyecto.Controllers.Proyecto.objects.ProyectoObjectCreados;
import org.example.avanceproyecto.Controllers.Proyecto.objects.TareaObject;
import org.example.avanceproyecto.Controllers.SharedStates;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.json.JSONObject;

import java.util.*;

@Getter @Setter
public class VerProyectosAsignados implements Observer {

    @FXML
    private ListView<ProyectoObjectAsignado> proyectos_list = new ListView<>();
    private ObservableList<ProyectoObjectAsignado> data = FXCollections.observableArrayList(); //Fata de todos los tareas nodos
    private VerProyectosAsignadosDialog verProyectosAsignadosDialog;
    Parent layout;
    private SharedStates sharedStates;
    private ProyectoShared prsh;
    private ButtonColorManager buttonColorManager;
    private Map<String, Graph<Object, DefaultEdge>> projectGraphs = new HashMap<>();

    ArrayList<Observer> observers = new ArrayList<>();


    public VerProyectosAsignados(SharedStates sharedStates, ProyectoShared proyectoShared) {
        this.layout = Utils.load_fxml("/FXML/Proyecto/VerProyectos.fxml", this);
        this.sharedStates = sharedStates;
        this.prsh = proyectoShared;
        this.verProyectosAsignadosDialog = new VerProyectosAsignadosDialog(sharedStates.getStage(),"ver datos",new Rectangle2D(0,0,900,600));
        proyectos_list.setItems(data);
        initialize_list();

    }

    private void initialize_list() {
        proyectos_list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ProyectoObjectAsignado>() {
            @Override
            public void changed(ObservableValue<? extends ProyectoObjectAsignado> observableValue, ProyectoObjectAsignado proyectoObjectAsignado, ProyectoObjectAsignado t1) {
                System.out.println("changed selection");
//                verProyectosAsignadosDialog.setProyectoData(t1);
//                verProyectosAsignadosDialog.show();
            }
        });
        proyectos_list.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            ProyectoObjectAsignado selected = proyectos_list.getSelectionModel().getSelectedItem();
            if (!verProyectosAsignadosDialog.dialogIsShowing())
                verProyectosAsignadosDialog.setProyectoData(selected);
            verProyectosAsignadosDialog.show();
            System.out.println("clicked");
        });


        proyectos_list.setCellFactory(listView -> new ListCell<ProyectoObjectAsignado>() {
            @Override
            protected void updateItem(ProyectoObjectAsignado proyecto, boolean empty) {
                super.updateItem(proyecto, empty);

                if (empty || proyecto == null) {
                    setText(null);
                } else {
                    setText(proyecto.getProycto_name()); // Use getter instead of toString
                }
            }

        });


        JSONObject proyectosAsignados = prsh.getProyectos_asignados();
        Iterator<String> keys = proyectosAsignados.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            JSONObject proyecto = proyectosAsignados.getJSONObject(key);
            ProyectoObjectAsignado proyectoObjectAsignado = ProyectoObjectAsignado.fromJson(proyecto);
            Graph<Object, DefaultEdge> graph = createGraph(proyectoObjectAsignado);
            projectGraphs.put(proyectoObjectAsignado.getProycto_name(), graph);
        }
        updateData();
    }
    private void updateData() {
        data.clear();
        List<ProyectoObjectAsignado> proyectoObjectList = UtilsGraphs.getAllProyectoObjects(projectGraphs,ProyectoObjectAsignado.class);
        System.out.printf("Size of hashmap graph is %d", proyectoObjectList.size());
        if (proyectoObjectList.size() > 0)
            data.addAll(proyectoObjectList);
        proyectos_list.refresh();
    }
    private Graph<Object, DefaultEdge> createGraph(ProyectoObjectAsignado proyecto) {
        Graph<Object, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);

        graph.addVertex(proyecto);
        for (EmpleadoTarea tarea : proyecto.getEmpleadoTareas()) {
            Empleado empleado = tarea.getEmpleado();
            TareaObject tareaObject = tarea.getTareaObject();

            graph.addVertex(tarea);
            graph.addEdge(proyecto, tarea); // Project -> Task relationship
        }
        return graph;
    }

    @Override
    public void proyecto_has_been_assigned(ProyectoObjectCreados proyectoObject,ProyectoObjectAsignado proyectoObjectAsignado) {
        System.out.println("project created hre in verproyectos");
        Graph<Object, DefaultEdge> graph = createGraph(proyectoObjectAsignado);
        projectGraphs.put(proyectoObjectAsignado.getProycto_name(), graph);
        updateData();

    }
}
