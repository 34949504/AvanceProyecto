package org.example.avanceproyecto.Controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.example.avanceproyecto.ControllerUtils.BaseController;
import org.example.avanceproyecto.ControllerUtils.Observer;
import org.example.avanceproyecto.ControllerUtils.Utils;
import org.example.avanceproyecto.Tarea.TareaNodo;
import org.example.avanceproyecto.Tarea.TaskAdministrator;
import org.example.avanceproyecto.Tarea.TipoTarea;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AgregarTarea extends BaseController implements Observer {

    private JSONObject tareas_json;
    private TaskAdministrator taskAdministrator = new TaskAdministrator();
    private TareaNodo currentNode = new TareaNodo();


    @FXML
    private ChoiceBox<String> departamentos_choicebox;
    @FXML
    private ChoiceBox<String> urgencia_choicebox;
    @FXML
    private ChoiceBox<String> tarea_choicebox;

    @FXML
    private Button enviar;
    @FXML
    private Button regresar;

    @FXML
    private Label departamento_label;
    @FXML
    private Label tarea_label;
    private boolean tarea_label_boolean_block_one_time_listener = false;
    @FXML
    private Label milisegundos_label;
    @FXML
    private Label tipoTarea_label;

    public AgregarTarea(String fxmlFile) {
        initilize_fxml(fxmlFile);
    }

    @FXML
    public void initialize() {
        departamentos_choicebox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                System.out.printf("s is %s and t1 is %s\n",s,t1);;
                if (tarea_choicebox.isDisable()) {
                    tarea_choicebox.setDisable(false);
                }
                else if (tarea_choicebox.getValue().compareTo("Seleccionar") !=0) {
                    tarea_label_boolean_block_one_time_listener = true;
                    tarea_choicebox.setValue("Seleccionar");
                    tarea_label.setText(String.format("Tarea: "));
                    milisegundos_label.setText(String.format("Milisegundos:"));
                }
                currentNode.setDepartamento(t1);
                populateTareaBox(t1);
                departamento_label.setText(String.format("Departamento: %s",t1));
            }
        });
        tarea_choicebox.getSelectionModel().selectedItemProperty().addListener((new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String string, String t1) {
                if (tarea_label_boolean_block_one_time_listener) {
                    tarea_label_boolean_block_one_time_listener = false;
                    return;
                }

                String departamento = departamentos_choicebox.getSelectionModel().getSelectedItem();
                JSONObject departamento_json = tareas_json.getJSONObject(departamento.toLowerCase());
                String tarea_firs  = tarea_choicebox.getSelectionModel().getSelectedItem();
                System.out.printf(String.format("Tarea john cenea: %s",t1));
                int milisecond =departamento_json.getInt(tarea_firs);
                milisegundos_label.setText(String.format("Milisegundos:%s",String.valueOf(milisecond)));
                tarea_label.setText(String.format("Tarea: %s",t1));

                currentNode.setNombreTarea(tarea_firs);
                currentNode.setMilisegundos(milisecond);
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

        Utils.set_action_regresar_main_menu(regresar,getObservers());

        enviar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String departamento = departamentos_choicebox.getValue();
                String urgencia = urgencia_choicebox.getValue();
                String tarea = tarea_choicebox.getValue();

                StringBuilder  datos_faltantes = new StringBuilder();
                boolean all_selected = true;

                if (departamento.compareTo("Seleccionar") == 0) {
                    all_selected = false;
                    datos_faltantes.append("Departamento\n");
                }
                if (urgencia.compareTo("Seleccionar") == 0) {
                    all_selected = false;
                    datos_faltantes.append("Tipo de Tarea\n");
                }
                if (tarea.compareTo("Seleccionar") == 0) {
                    all_selected = false;
                    datos_faltantes.append("Tarea");
                }

                if (all_selected){
                    taskAdministrator.add_task(
                            currentNode.getDepartamento(),
                            currentNode.getNombreTarea(),
                            currentNode.getTipoTarea(),
                            currentNode.getMilisegundos());

                    for (Observer observer:getObservers()) {
                        if (observer instanceof VerTareas verTareas) {
                            verTareas.updateTable(currentNode.getTipoTarea());
                            break;
                        }
                    }

                } else {
                    Alert alert = Utils.get_alert_position_centered(getStage(),Alert.AlertType.WARNING,"Advertencia","Datos faltantes:",datos_faltantes.toString());
                    alert.showAndWait();

                }
            }
        });

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
//        if (!tarea_choicebox.getItems().isEmpty()) {
//            tarea_choicebox.getSelectionModel().selectFirst();
//            String tarea_firs  = tarea_choicebox.getSelectionModel().getSelectedItem();
//            System.out.println(String.format("Selected item bom %s ",tarea_firs));
//            int miliseconds =departamento_json.getInt(tarea_firs);
//            milisegundos_label.setText(String.format("Milisegundos:%s",String.valueOf(miliseconds)));
//
//            currentNode.setNombreTarea(tarea_firs);
//            currentNode.setMilisegundos(miliseconds);
//
//        }
//        else {
//            System.out.println("DUmbass reson tarea choicebox is empty");
//        }

    }

    public void setTareas_json(JSONObject tareas_json) {
        this.tareas_json = tareas_json;
    }


    @Override
    public ArrayList<TareaNodo> get_node_tarea_array(TipoTarea tipoTarea) {
        return taskAdministrator.get_arraylist_tarea_nodo(tipoTarea);
    }
}
