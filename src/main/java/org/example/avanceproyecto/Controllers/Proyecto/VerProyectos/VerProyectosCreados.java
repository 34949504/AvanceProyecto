package org.example.avanceproyecto.Controllers.Proyecto.VerProyectos;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import lombok.Getter;
import lombok.Setter;
import org.example.avanceproyecto.ControllerUtils.ButtonColorManager;
import org.example.avanceproyecto.ControllerUtils.Dialogs.VerProyectoCreadosDialog;
import org.example.avanceproyecto.ControllerUtils.Observer;
import org.example.avanceproyecto.ControllerUtils.Utils;
import org.example.avanceproyecto.ControllerUtils.UtilsGraphs;
import org.example.avanceproyecto.Controllers.Proyecto.ProyectoShared;
import org.example.avanceproyecto.Controllers.Proyecto.objects.ProyectoObjectAsignado;
import org.example.avanceproyecto.Controllers.Proyecto.objects.ProyectoObjectCreados;
import org.example.avanceproyecto.Controllers.Proyecto.objects.TareaObject;
import org.example.avanceproyecto.Controllers.SharedStates;

import javafx.fxml.FXML;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.json.JSONObject;

import java.util.*;

@Getter
@Setter
public class VerProyectosCreados implements Observer {

    @FXML
    private ListView<ProyectoObjectCreados> proyectos_list = new ListView<>();
    private ObservableList<ProyectoObjectCreados> data = FXCollections.observableArrayList(); //Fata de todos los tareas nodos
    private VerProyectoCreadosDialog verProyectoDialog; Parent layout; private SharedStates sharedStates;
    private ProyectoShared prsh;
    private ButtonColorManager buttonColorManager;
    private Map<String, Graph<Object, DefaultEdge>> projectGraphs = new HashMap<>();

    ArrayList<Observer> observers = new ArrayList<>();


    public VerProyectosCreados(SharedStates sharedStates, ProyectoShared proyectoShared) {
        this.layout = Utils.load_fxml("/FXML/Proyecto/VerProyectos.fxml", this);
        this.sharedStates = sharedStates;
        this.prsh = proyectoShared;
        this.verProyectoDialog = new VerProyectoCreadosDialog(sharedStates.getStage(),"ver datos",new Rectangle2D(0,0,900,600));
        proyectos_list.setItems(data);
        initialize_list();

    }

    private void initialize_list() {
        proyectos_list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ProyectoObjectCreados>() {
            @Override
            public void changed(ObservableValue<? extends ProyectoObjectCreados> observableValue, ProyectoObjectCreados proyectoObject, ProyectoObjectCreados t1) {
//                System.out.println("changed selection");
//                verProyectoDialog.setProyectoObject(t1);
//                verProyectoDialog.show();

            }
        });
        proyectos_list.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            ProyectoObjectCreados selected = proyectos_list.getSelectionModel().getSelectedItem();
            if (!verProyectoDialog.dialogIsShowing())
            verProyectoDialog.setProyectoObject(selected);
            verProyectoDialog.show();
            System.out.println("clicked");
        });


        proyectos_list.setCellFactory(listView -> new ListCell<ProyectoObjectCreados>() {
            @Override
            protected void updateItem(ProyectoObjectCreados proyecto, boolean empty) {
                super.updateItem(proyecto, empty);

                if (empty || proyecto == null) {
                    setText(null);
                } else {
                    setText(proyecto.getProycto_name()); // Use getter instead of toString
                }
            }

        });


        JSONObject proyectos_creados = prsh.getProyectos_creados();
        Iterator<String> keys = proyectos_creados.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            JSONObject proyecto = proyectos_creados.getJSONObject(key);
            ProyectoObjectCreados proyectoObject = ProyectoObjectCreados.fromJson(proyecto);
            Graph<Object, DefaultEdge> graph = createGraph(proyectoObject);
            projectGraphs.put(proyectoObject.getProycto_name(), graph);
        }
        updateData();
    }
    private void updateData() {

        data.clear();
        List<ProyectoObjectCreados> proyectoObjectList =  UtilsGraphs.getAllProyectoObjects(projectGraphs,ProyectoObjectCreados.class);
        System.out.printf("Size of hashmap graph is %d", proyectoObjectList.size());
        if (proyectoObjectList.size() > 0)
            data.addAll(proyectoObjectList);
        proyectos_list.refresh();
    }

    @Override
    public void proyecto_has_been_created(ProyectoObjectCreados proyecto) {
        System.out.println("project created hre in verproyectos");
        Graph<Object, DefaultEdge> graph = createGraph(proyecto);
        projectGraphs.put(proyecto.getProycto_name(), graph);
        updateData();
    }



    private Graph<Object, DefaultEdge> createGraph(ProyectoObjectCreados proyecto) {
        Graph<Object, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);

        graph.addVertex(proyecto);

        for (TareaObject tarea : proyecto.getTareas_proyecto()) {
            graph.addVertex(tarea);
            graph.addEdge(proyecto, tarea); // Project -> Task relationship
        }
        return graph;
    }

    @Override
    public void proyecto_has_been_assigned(ProyectoObjectCreados proyectoObject, ProyectoObjectAsignado proyectoObjectAsignado) {
        Set<String> names =  UtilsGraphs.getAllProjectNames(projectGraphs);
        System.out.println("current names in graph");
        for (String name:names) {

            System.out.println("name " +name);
        }


        if (proyectoObject == null) {
            System.out.println("proyect es null inver proyectos has been assigned");
        }

        System.out.println("removing object here in verproyectos");
        System.out.println("proyecto name is " + proyectoObject.getProycto_name());
        org.jgrapht.Graph<Object,DefaultEdge> graph = projectGraphs.remove(proyectoObject.getProycto_name());
        if (graph == null) {
            System.out.println("graph es null what");
        }
        updateData();

    }
}
