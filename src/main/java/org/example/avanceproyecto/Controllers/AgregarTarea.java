/*
Es el controlador que te permite crear tareas
 */


package org.example.avanceproyecto.Controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.Getter;
import lombok.Setter;
import org.example.avanceproyecto.ControllerUtils.*;
import org.example.avanceproyecto.Tarea.TareaNodo;
import org.example.avanceproyecto.Tarea.TaskAdministrator;
import org.example.avanceproyecto.Tarea.TipoTarea;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

@Setter @Getter
public class AgregarTarea extends BaseController implements Observer {

    private JSONObject tareas_json;
    private JSONObject empleados_json;
    private TaskAdministrator taskAdministrator;
    private TareaNodo currentNode = new TareaNodo();


    @FXML
    private ChoiceBox<String> departamentos_choicebox;
    @FXML
    private ChoiceBox<String> urgencia_choicebox;
    @FXML
    private ChoiceBox<String> tarea_choicebox;
    @FXML
    private ChoiceBox<String> empleado_choicebox;

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

    public AgregarTarea(String fxmlFile) {
        initilize_fxml(fxmlFile);
    }

    @Override
    public void init() {


        taskAdministrator =  new TaskAdministrator(getObservers(),getSharedStates());
    }

    @FXML
    public void initialize() {

        departamentos_choicebox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (tarea_choicebox.isDisable()) {
                    tarea_choicebox.setDisable(false);
                    empleado_choicebox.setDisable(false);
                }
                else if (tarea_choicebox.getValue().compareTo("Seleccionar") !=0) {
                    choice_block_one_time_ignore = true;
                    set_choice_label_values("Seleccionar","Tarea: ",tarea_choicebox,tarea_label);
                    set_choice_label_values("Seleccionar","Tarea: ",empleado_choicebox,empleado_label);
                    milisegundos_label.setText(String.format("Segundos:"));
                }

                departamento_label.setText(String.format("Departamento: %s",t1));
                currentNode.setDepartamento(t1);
                populateTareaBox(t1);
                populateEmpleados(t1);
            }
        });
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
            }
        }));

        empleado_choicebox.getSelectionModel().selectedItemProperty().addListener((new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String string, String t1) {
                if (choice_block_one_time_ignore) {
                    choice_block_one_time_ignore = false;
                    return;
                }

                empleado_label.setText(String.format("Empleado: %s",t1));
                String empleado_selected  = empleado_choicebox.getSelectionModel().getSelectedItem();
                currentNode.setEmpleado_asignado(empleado_selected);


            }
        }));


        urgencia_choicebox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                System.out.printf("s is %s and t1 is %s\n",s,t1);;
                currentNode.setTipoTarea(TipoTarea.get_enum_by_string_comparison(t1));
                tipoTarea_label.setText(String.format("Tipo de tarea: %s",t1));
            }
        });


        enviar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String departamento = departamentos_choicebox.getValue();
                String urgencia = urgencia_choicebox.getValue();
                String tarea = tarea_choicebox.getValue();
                String empleado = tarea_choicebox.getValue();

                StringBuilder  datos_faltantes = new StringBuilder();
                check_datos_faltantes(departamento,"Departamento",datos_faltantes);
                check_datos_faltantes(urgencia,"Urgencia",datos_faltantes);
                check_datos_faltantes(tarea,"Tarea",datos_faltantes);
                check_datos_faltantes(empleado,"Empleado",datos_faltantes);
                boolean all_selected = datos_faltantes.length() <= 0 ? true:false;

                if (all_selected){
                    taskAdministrator.add_task(currentNode);

                    for (Observer observer:getObservers()) {

                        if (observer instanceof VerTareas verTareas) {
                            verTareas.updateTable(currentNode.getTipoTarea());
                        }
                        observer.tarea_creada(currentNode);
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
    private void check_datos_faltantes(String dato,String value,StringBuilder message) {
        if (dato.compareTo("Seleccionar") == 0) {
            message.append(String.format("%s\n",value));
        }
    }


    private void populateTareaBox(String departamento) {
        JSONObject departamento_json = tareas_json.getJSONObject(departamento.toLowerCase());
        JSONArray departamento_tareas = departamento_json.getJSONArray("tareas_ordenadas");
        tarea_choicebox.getItems().clear();
        for (int i = 0; i < departamento_tareas.length(); i++) {
            String tarea = departamento_tareas.getString(i);
            tarea_choicebox.getItems().add(tarea);
        }
        System.out.println(tarea_choicebox.getItems().size());
    }

    private void populateEmpleados(String departamento) {
        JSONObject departamento_json = empleados_json.getJSONObject(departamento.toLowerCase());
        JSONArray empleads_array = departamento_json.getJSONArray("empleados_ordenados");
        int len = empleads_array.length();

        ArrayList<String> empleados = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            int empleado_id = empleads_array.getInt(i);
            String emplead_id_string = Integer.toString(empleado_id);
            JSONObject empleado = departamento_json.getJSONObject(emplead_id_string);
            String nombre = empleado.getString("nombre");
            String apellidos = empleado.getString("apellidos");
            String nombre_completo = String.format("%s %s",nombre,apellidos);
            empleados.add(nombre_completo);
        }
        empleado_choicebox.getItems().clear();
        for (int i = 0; i < empleados.size(); i++) {
            String empleado = empleados.get(i);
            System.out.println("nombre empleado "+empleado);
            empleado_choicebox.getItems().add(empleado);
        }
        System.out.println(tarea_choicebox.getItems().size());
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

}
