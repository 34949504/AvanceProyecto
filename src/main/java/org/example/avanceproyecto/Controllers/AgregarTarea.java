package org.example.avanceproyecto.Controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
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
    private Label milisegundos_label;

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
                currentNode.setDepartamento(t1);
                populateTareaBox(t1);
            }
        });
        tarea_choicebox.getSelectionModel().selectedItemProperty().addListener((new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String string, String t1) {
                String departamento = departamentos_choicebox.getSelectionModel().getSelectedItem();
                JSONObject departamento_json = tareas_json.getJSONObject(departamento.toLowerCase());
                String tarea_firs  = tarea_choicebox.getSelectionModel().getSelectedItem();
                int milisecond =departamento_json.getInt(tarea_firs);
                milisegundos_label.setText(String.format("Milisegundos:%s",String.valueOf(milisecond)));

                currentNode.setNombreTarea(tarea_firs);
                currentNode.setMilisegundos(milisecond);
            }
        }));


        urgencia_choicebox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                System.out.printf("s is %s and t1 is %s\n",s,t1);;
                currentNode.setTipoTarea(TipoTarea.get_enum_by_string_comparison(t1));
            }
        });

        Utils.set_action_regresar_main_menu(regresar,getObservers());

        enviar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String departamento = departamentos_choicebox.getValue();
                String urgencia = urgencia_choicebox.getValue();
                String tarea = tarea_choicebox.getValue();
                String miliseconds = milisegundos_label.getText();

                boolean all_selected = true;

                if (departamento.compareTo("Seleccionar") == 0) {
                    all_selected = false;
                }
                if (urgencia.compareTo("Seleccionar") == 0) {
                    all_selected = false;
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
        if (!tarea_choicebox.getItems().isEmpty()) {
            tarea_choicebox.getSelectionModel().selectFirst();
            String tarea_firs  = tarea_choicebox.getSelectionModel().getSelectedItem();
            int miliseconds =departamento_json.getInt(tarea_firs);
            milisegundos_label.setText(String.format("Milisegundos:%s",String.valueOf(miliseconds)));

            currentNode.setNombreTarea(tarea_firs);
            currentNode.setMilisegundos(miliseconds);

        }

    }

    public void setTareas_json(JSONObject tareas_json) {
        this.tareas_json = tareas_json;
    }


    @Override
    public ArrayList<TareaNodo> get_node_tarea_array(TipoTarea tipoTarea) {
        System.out.println("bitch");
        return taskAdministrator.get_arraylist_tarea_nodo(tipoTarea);
    }
}
