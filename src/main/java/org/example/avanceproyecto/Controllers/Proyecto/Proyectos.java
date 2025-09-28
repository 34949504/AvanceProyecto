package org.example.avanceproyecto.Controllers.Proyecto;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import lombok.Getter;
import lombok.Setter;
import org.example.avanceproyecto.ControllerUtils.*;
import org.example.avanceproyecto.Controllers.Proyecto.VerProyectos.BaseVerProyectos;
import org.json.JSONObject;

/**
 * Idea
 * Esta la seccion para crear proyecto, poner descripcion, nombre del proyecto, tareas a realizar, fecha de entrega  (textfields)
 * Seccion de asignar a empleados a proyectos, seleccionar departamento y una lista  de los empleados
 * Seccion explorar los proyectos (ver sus datos, tareas y los que estan trabajando en el
 * Seccion para marcar proyectos terminados (se quita el proyecto de json)
 * Grafos
 * Json donde se guarde  Nombre de proyecto -> datos de proyecto (key {}),empleados ({}) -> empleado: [tareas asignadas]
 */
@Getter
@Setter
public class Proyectos extends BaseController implements Observer {

    @FXML
    private Button crear_proyecto_button;
    @FXML
    private Button asignar_proyecto_button;
    @FXML
    private Button ver_proyecto_button;

    @FXML
    private BorderPane border_pane;


    public JSONObject proyectos_creados;
    public JSONObject proyectos_asignados;
    public ProyectoShared proyectoShared = new ProyectoShared();
    public CreadorProyecto creadorProyecto;
    public AsignadorProyecto asignadorProyecto;
    public BaseVerProyectos baseVerProyectos;


    private ButtonColorManager buttonColorManager;

    @Override
    public void init() {
        onActionButtons();
        buttonColorManager = new ButtonColorManager(ver_proyecto_button, asignar_proyecto_button, crear_proyecto_button);
        buttonColorManager.change_color_state(crear_proyecto_button);

        init_proyectoShared();
        creadorProyecto = new CreadorProyecto(getSharedStates(),proyectoShared);
        asignadorProyecto = new AsignadorProyecto(getSharedStates(),proyectoShared);
        baseVerProyectos = new BaseVerProyectos(getSharedStates(),proyectoShared);

        this.addObservers(asignadorProyecto);
        this.addObservers(baseVerProyectos.getVerProyectos());


        Utils.addObservers(asignadorProyecto.getObservers(),baseVerProyectos.getVerProyectos(),baseVerProyectos.getVerProyectosAsignados());

        creadorProyecto.addObservers(getObservers());





        border_pane.setCenter(creadorProyecto.getCreadorProyectoBorderPane());
    }
    private void init_proyectoShared() {
        proyectoShared.setProyectos_asignados(proyectos_asignados);
        proyectoShared.setProyectos_creados(proyectos_creados);
    }

    public Proyectos(String fxml) {
        initilize_fxml(fxml);
    }

    public void onActionButtons() {


        asignar_proyecto_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                buttonColorManager.change_color_state((Button) actionEvent.getSource());
                border_pane.setCenter(asignadorProyecto.getLayout());

            }
        });
        ver_proyecto_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                buttonColorManager.change_color_state((Button) actionEvent.getSource());
                border_pane.setCenter(baseVerProyectos.getLayout());


            }
        });

        crear_proyecto_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                buttonColorManager.change_color_state((Button) actionEvent.getSource());
                border_pane.setCenter(creadorProyecto.getCreadorProyectoBorderPane());
            }
        });


    }


}






/*

@FXML
ChoiceBox proyecto_seleccionar_choicebox;
@FXML
Label tareas_asignadas_label;
@FXML
TableView asignados_table;
@FXML
Button crear_equipo_proyecto;

Parent layout;
public AsignadorProyecto() {
    layout = Utils.load_fxml("/FXML/Proyecto/AsignarProyecto.fxml ", this);
}
public Parent getLayout() {
    return layout;
}
*/
