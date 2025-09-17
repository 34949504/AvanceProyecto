/*
Es el controlador que te permite crear tareas
 */


package org.example.avanceproyecto.Controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import lombok.Getter;
import lombok.Setter;
import org.example.avanceproyecto.ControllerUtils.*;
import org.example.avanceproyecto.Tarea.TareaNodo; import org.example.avanceproyecto.Tarea.TaskAdministrator; import org.example.avanceproyecto.Tarea.TipoTarea; import org.json.JSONArray; import org.json.JSONObject;

import java.util.ArrayList;

@Setter @Getter
public class AgregarTarea extends BaseController implements Observer {

    private JSONObject tareas_json;
    private JSONObject empleados_json;
    private TaskAdministrator taskAdministrator;
    private TareaNodo currentNode = new TareaNodo();


    private String dato_faltante_color ="#737814";
    private String dato_activo_color = "black";


    @FXML
    private ChoiceBox<String> departamentos_choicebox;
    @FXML
    private ChoiceBox<String> urgencia_choicebox;
    @FXML
    private ChoiceBox<String> tarea_choicebox;
    @FXML
    private ChoiceBox<Empleado> empleado_choicebox;

    @FXML
    private Button enviar;
//    @FXML
//    private Button regresar;

    @FXML
    private Label departamento_label;
    @FXML
    private Label tarea_label;
    private boolean choice_block_one_time_ignore = false;
    @FXML
    private Label milisegundos_label;
    @FXML
    private Label tipoTarea_label;
    @FXML
    private Label empleado_label;


    @FXML
    private VBox urgencia_choicebox_container;
    @FXML
    private VBox labels_output_container;



    @FXML
    private Button tareas_prioritarias_button;
    boolean tareas_prioritarias_active =false;


    private ChoiceBox<String> prioridad_choicebox = new ChoiceBox<>();

    public AgregarTarea(String fxmlFile) {
        initilize_fxml(fxmlFile);
    }

    @Override
    public void init() {


        taskAdministrator =  new TaskAdministrator(getObservers(),getSharedStates());

    }

    @FXML
    public void initialize() {

        prioridad_choicebox.getItems().addAll("Baja","Media","Alta");
        prioridad_choicebox.getStyleClass().add("medium-size-choicebox");
        prioridad_choicebox.setValue("Seleccionar");

        setLabelColor(departamento_label,dato_faltante_color);
        setLabelColor(empleado_label,dato_faltante_color);
        setLabelColor(tarea_label,dato_faltante_color);
        setLabelColor(tipoTarea_label,dato_faltante_color);
        setLabelColor(milisegundos_label,dato_faltante_color);


        empleado_choicebox.setValue(null);
        setupEmpleadoChoiceBox();
        enviarOnAction();
        urgenciaChoiceboxListener();
        empleadoChoiceboxListener();
        tareaChoiceboxListener();
        departamentosChoiceboxListener();
        tareasPrioritariasOnAction();
        prioridadChoiceboxListener();


    }
    private void check_datos_faltantes(String dato,String value,StringBuilder message) {
        if (dato.compareTo("Seleccionar") == 0) {
            message.append(String.format("%s\n",value));
        }
    }

    private void populateChoiceBox(JSONArray jsonArray,ChoiceBox<?> choiceBox) {

    }


    private void populateTareaBox(String departamento) {
        JSONObject departamento_json = tareas_json.getJSONObject(departamento.toLowerCase());
        JSONArray departamento_tareas = departamento_json.getJSONArray("tareas_ordenadas");

        tarea_choicebox.getItems().clear();
        for (int i = 0; i < departamento_tareas.length(); i++) {
            String tarea = departamento_tareas.getString(i);
            tarea_choicebox.getItems().add(tarea);
        }
    }

    private void populateEmpleados(String departamento) {
        SharedStates sharedStates = getSharedStates();
        ArrayList<Empleado> empleadoArrayList = sharedStates.getEmpleadosArray(departamento.toLowerCase());

        empleado_choicebox.getItems().clear();
        for (Empleado empleado:empleadoArrayList) {
                String empleado_fullName = empleado.getFullName();
                empleado_choicebox.getItems().add(empleado);
            }
    }


    public void setTareas_json(JSONObject tareas_json) {
        this.tareas_json = tareas_json;
    }


    @Override
    public ArrayList<TareaNodo> get_node_tarea_array(TipoTarea tipoTarea) {
        return taskAdministrator.get_arraylist_tarea_nodo(tipoTarea);
    }

    /*
    choice_box_text  label_of_choice_box_text choiceboxObject labelObject
     */
    private void set_choice_label_values (Object ... objects) {
        String choice_box_value = (String)objects[0];
        String label_value = (String)objects[1];
        ChoiceBox<String> choicebox = (ChoiceBox<String>) objects[2];
        Label label = (Label) objects[3];

        choicebox.setValue(choice_box_value);
        label.setText(label_value);
    }

    private void setupEmpleadoChoiceBox() {
        empleado_choicebox.setConverter(new StringConverter<Empleado>() {
            @Override
            public String toString(Empleado empleado) {
                if (empleado == null) return "Seleccionar";
                return empleado.getFullName();
            }

            @Override
            public Empleado fromString(String string) {
                // Optional: used if user types into editable ComboBox
                // For ChoiceBox, you can just return null or implement lookup
                return null;
            }
        });

    }

    private void setLabelColor(Label label, String color) {
        label.setStyle(String.format("-fx-text-fill: %s;", color));
    }

    private void prioridadChoiceboxListener() {

        prioridad_choicebox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String string, String t1) {
                System.out.println("This was called bruv");

                if (choice_block_one_time_ignore) {
                    choice_block_one_time_ignore = false;
                    return;
                }
                System.out.println("fuck why");
                tipoTarea_label.setText(String.format("Prioridad: %s",t1));
                setLabelColor(tipoTarea_label,dato_activo_color);
                currentNode.setPrioridad(Prioridad.getPrioridad(t1));


            }
        });
    }
    private void tareasPrioritariasOnAction() {

        tareas_prioritarias_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                String light_yellow = SharedStates.Colores.AMARILLO;
                String white = SharedStates.Colores.BLANCO;

                choice_block_one_time_ignore = true;
                if (tareas_prioritarias_active) {
                    setLabelColor(tipoTarea_label,dato_faltante_color);
                    tareas_prioritarias_button.setStyle(String.format("-fx-background-color: %s;",white));
                    tareas_prioritarias_active = false;
                    ObservableList<Node> nodes = urgencia_choicebox_container.getChildren();

                    nodes.removeLast();
                    nodes.addLast(urgencia_choicebox);
                    urgencia_choicebox.setValue("Seleccionar");

                    Label label = (Label) nodes.getFirst();
                    label.setText("Urgencia");
                    tipoTarea_label.setText("Tipo de tarea:");
                    currentNode.setPrioridad(Prioridad.none);


                }
                else {
                    setLabelColor(tipoTarea_label,dato_faltante_color);
                    tareas_prioritarias_button.setStyle(String.format("-fx-background-color: %s;",light_yellow));
                    tareas_prioritarias_active = true;

                    ObservableList<Node> nodes = urgencia_choicebox_container.getChildren();
                    nodes.removeLast();
                    tipoTarea_label.setText("Prioridad:");
                    Label label = (Label) nodes.getFirst();
                    label.setText("Prioridad");
                    nodes.addLast(prioridad_choicebox);
                    prioridad_choicebox.setValue("Seleccionar");


                }

            }
        });
    }
    private void departamentosChoiceboxListener() {

        departamentos_choicebox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {

                setLabelColor(departamento_label,dato_activo_color);

                Empleado empleado =  empleado_choicebox.getValue();
                if (tarea_choicebox.isDisable()) {
                    tarea_choicebox.setDisable(false);
                    empleado_choicebox.setDisable(false);
                }

                else if (tarea_choicebox.getValue().compareTo("Seleccionar") !=0 || empleado_choicebox.getValue() != null ) {
                    choice_block_one_time_ignore = true;

                    tarea_choicebox.setValue("Seleccionar");
                    tarea_label.setText("Tarea:");

                    empleado_choicebox.setValue(null);
                    empleado_label.setText("Empleado:");
                    milisegundos_label.setText(String.format("Segundos:"));

                    setLabelColor(empleado_label,dato_faltante_color);
                    setLabelColor(tarea_label,dato_faltante_color);
                    setLabelColor(milisegundos_label,dato_faltante_color);

                }

                departamento_label.setText(String.format("Departamento: %s",t1));
                currentNode.setDepartamento(t1);
                populateTareaBox(t1);
                populateEmpleados(t1);
            }
        });
    }
    private void tareaChoiceboxListener() {

        tarea_choicebox.getSelectionModel().selectedItemProperty().addListener((new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String string, String t1) {
                if (choice_block_one_time_ignore) {
                    choice_block_one_time_ignore = false;
                    return;
                }
                String departamento = departamentos_choicebox.getSelectionModel().getSelectedItem();
                JSONObject departamento_json = tareas_json.getJSONObject(departamento.toLowerCase());
                String tarea_seleccionada  = tarea_choicebox.getSelectionModel().getSelectedItem();
                int milisecond = Utils.getRandomIntFromList(departamento_json.getJSONArray(tarea_seleccionada));

                milisegundos_label.setText(String.format("Segundos:%s",String.valueOf(milisecond)));
                tarea_label.setText(String.format("Tarea: %s",t1));

                currentNode.setNombreTarea(tarea_seleccionada);
                currentNode.setSegundos(milisecond);


                setLabelColor(tarea_label,dato_activo_color);
                setLabelColor(milisegundos_label,dato_activo_color);
            }
        }));
    }
    private void empleadoChoiceboxListener() {

        empleado_choicebox.getSelectionModel().selectedItemProperty().addListener((new ChangeListener<Empleado>() {
            @Override
            public void changed(ObservableValue<? extends Empleado> observableValue, Empleado empleado, Empleado t1) {

                if (choice_block_one_time_ignore) {
                    choice_block_one_time_ignore = false;
                    return;
                }
                if (t1 == null) {
                    return;
                }

                String full_name = t1.getFullName();
                empleado_label.setText(String.format("Empleado: %s",full_name));
                Empleado empleado_selected  = empleado_choicebox.getSelectionModel().getSelectedItem();
                currentNode.setEmpleadoAsignado(empleado_selected);


                setLabelColor(empleado_label,dato_activo_color);
            }

        }));
    }
    private void urgenciaChoiceboxListener() {

        urgencia_choicebox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (choice_block_one_time_ignore) {
                    choice_block_one_time_ignore = false;
                    return;
                }

                currentNode.setTipoTarea(TipoTarea.get_enum_by_string_comparison(t1));
                tipoTarea_label.setText(String.format("Tipo de tarea: %s",t1));


                setLabelColor(tipoTarea_label,dato_activo_color);
            }
        });
    }
    private void enviarOnAction() {

        enviar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String departamento = departamentos_choicebox.getValue();
                String urgencia = urgencia_choicebox.getValue();
                String tarea = tarea_choicebox.getValue();
                String prioridad = prioridad_choicebox.getValue();
                Empleado empleado = empleado_choicebox.getValue();

                StringBuilder  datos_faltantes = new StringBuilder();
                check_datos_faltantes(departamento,"Departamento",datos_faltantes);
                check_datos_faltantes(tarea,"Tarea",datos_faltantes);

                if (tareas_prioritarias_active) {
                    check_datos_faltantes(prioridad,"Prioridad",datos_faltantes);
                } else {
                    check_datos_faltantes(urgencia,"Urgencia",datos_faltantes);
                }
                if (empleado == null) {
                    datos_faltantes.append("Empleado");
                }
                boolean all_selected = datos_faltantes.length() <= 0 ? true:false;

                if (all_selected){
                    TareaNodo tareaNodo = TareaNodo.getNodeFromOtherNodeValues(currentNode);
                    taskAdministrator.add_task(tareaNodo);
                    tareaNodo.getEmpleadoAsignado().setActividadStatus(Empleado.ActividadStatus.Activo);
                    quitar_empleado_realizando_tarea_de_checkbox(departamento);
                    for (Observer observer:getObservers()) {
                        if (observer instanceof VerTareas verTareas) {
                            verTareas.tareaTerminada(currentNode.getTipoTarea());
                        }
                        observer.tarea_creada(tareaNodo);
                    }
                    String message = String.format("Tarea:%s\nDuración:%dms\nTipo de Tarea:%s",currentNode.getNombreTarea(),currentNode.getSegundos(),currentNode.getTipoTarea());
                    Toast.show(getStage(),"Operacion Exitosa");

                } else {
                    Alert alert = Utils.get_alert_position_centered(getStage(),Alert.AlertType.WARNING,"Advertencia","Datos faltantes:",datos_faltantes.toString());
                    alert.showAndWait();

                }
            }
        });
    }

    private void quitar_empleado_realizando_tarea_de_checkbox(String departamento) {

        SharedStates sharedStates = getSharedStates();
        ArrayList<Empleado> empleadoArrayList = sharedStates.getEmpleadosArray(departamento.toLowerCase());
        empleado_choicebox.getItems().clear();
        for (Empleado empleado:empleadoArrayList) {
            if (empleado.getActividadStatus() == Empleado.ActividadStatus.No_activo) {
                empleado_choicebox.getItems().add(empleado);
            }
        }
        empleado_choicebox.setValue(null);
        empleado_label.setText("Empleado:");
        setLabelColor(empleado_label,dato_faltante_color);


    }









}
