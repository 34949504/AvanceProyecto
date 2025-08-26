package org.example.avanceproyecto;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AgregarTarea implements Observer {

    private JSONObject tareas_json;
    private ArrayList<Observer> observers = new ArrayList<>();


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
    public void initialize() {
        departamentos_choicebox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                System.out.printf("s is %s and t1 is %s\n",s,t1);;
                if (tarea_choicebox.isDisable()) {
                    tarea_choicebox.setDisable(false);
                }
                populateTareaBox(t1);
            }
        });


        urgencia_choicebox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                System.out.printf("s is %s and t1 is %s\n",s,t1);;
            }
        });

        regresar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                for (Observer observer: observers){
                    observer.go_mainLayout();
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
        }

    }

    public void setTareas_json(JSONObject tareas_json) {
        this.tareas_json = tareas_json;
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }
}
