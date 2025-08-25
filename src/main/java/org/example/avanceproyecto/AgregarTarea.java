package org.example.avanceproyecto;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import org.json.JSONArray;
import org.json.JSONObject;

public class AgregarTarea {

    private JSONObject tareas_json;

    @FXML
    private ChoiceBox<String> departamentos_choicebox;
    @FXML
    private ChoiceBox<String> urgencia_choicebox;
    @FXML
    private ChoiceBox<String> tarea_choicebox;

    @FXML
    public void initialize() {
        departamentos_choicebox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                System.out.printf("s is %s and t1 is %s\n",s,t1);;
                if (tarea_choicebox.isDisable()) {
                    tarea_choicebox.setDisable(false);
                }
            }
        });


        urgencia_choicebox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                System.out.printf("s is %s and t1 is %s\n",s,t1);;
            }
        });

    }


    private void populateTareaBox(String departamento) {
        JSONObject departamento_json = tareas_json.getJSONObject(departamento.toLowerCase());
        JSONArray departamento_tareas = departamento_json.getJSONArray("tareas_ordenadas");

        tarea_choicebox.getItems().addAll();

    }

    public void setTareas_json(JSONObject tareas_json) {
        this.tareas_json = tareas_json;
    }
}
