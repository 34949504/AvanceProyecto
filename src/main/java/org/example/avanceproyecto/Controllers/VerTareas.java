/*
Se encarga de controlar el gui cuando ves las tareas
 */

package org.example.avanceproyecto.Controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.avanceproyecto.ControllerUtils.BaseController;
import org.example.avanceproyecto.ControllerUtils.Observer;
import org.example.avanceproyecto.ControllerUtils.Utils;
import org.example.avanceproyecto.Tarea.TareaNodo;
import org.example.avanceproyecto.Tarea.TipoTarea;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class VerTareas extends BaseController implements Observer {

    private ObservableList<TareaNodo> data = FXCollections.observableArrayList();
    private ObservableList<TareaNodo> filtered_data = FXCollections.observableArrayList();
    private TipoTarea current_tipoTarea_state = TipoTarea.Urgente;
    @FXML
    private Spinner<Integer> mySpinner;

    @FXML
    private Button pausar_thread;

    @FXML
    private ChoiceBox<String> filtro;

    @FXML
    private Button urgentes_button;

    @FXML
    private Button no_urgentes_button;

    @FXML
    private Button lista_button;

    @FXML
    private Label titulo_nombre_tarea_label;


//    @FXML
//    private Button regresar;

    @FXML
    private TableView<TareaNodo> table;


    public VerTareas(String fxmlFile) {
        initilize_fxml(fxmlFile);

    }

    @Override
    public void init() {

    }

    @FXML
    public void initialize() {
//        Utils.set_action_regresar_main_menu(regresar, getObservers());
        change_color_state(urgentes_button,no_urgentes_button,lista_button);

        mySpinner.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observableValue, Integer integer, Integer t1) {
                SharedStates sharedStates = getSharedStates();
                AtomicInteger currentDelay = sharedStates.getSpeed();
                int baseSpeed = 1000;
                if (t1 == 0) {
                    currentDelay.set(baseSpeed); // Normal speed: 1000ms
                } else if (t1 > 0) {
                    // Positive = faster (reduce delay)
                    // Speed 1 = 900ms, Speed 10 = 100ms
                    currentDelay.set(baseSpeed - (t1 * 90));
                    if (currentDelay.get() < 100) currentDelay.set(100); // Minimum delay
                } else {
                    currentDelay.set(baseSpeed + (Math.abs(t1) * 100));
                }

            }
        });


        urgentes_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                titulo_nombre_tarea_label.setText(urgentes_button.getText());
                changedaTable(TipoTarea.Urgente);
                change_color_state(urgentes_button,no_urgentes_button,lista_button);
            }
        });
        no_urgentes_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                titulo_nombre_tarea_label.setText(no_urgentes_button.getText());
                changedaTable(TipoTarea.No_Urgente);
                change_color_state(no_urgentes_button,urgentes_button,lista_button);
            }
        });

        lista_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                titulo_nombre_tarea_label.setText(lista_button.getText());
                changedaTable(TipoTarea.Lista);
                change_color_state(lista_button,urgentes_button,no_urgentes_button);
            }
        });
        filtro.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String string, String t1) {

                if (t1.compareTo("Ninguno") == 0) {
                  table.setItems(data);
                } else {
                    filter_data(t1);
                    table.setItems(filtered_data);
                }


            }
        });

        pausar_thread.setStyle("-fx-background-color: green;");

        pausar_thread.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                SharedStates sharedStates = getSharedStates();
                boolean new_value = !sharedStates.getThread_active().get();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        sharedStates.getThread_active().set(new_value);
                    }
                });

                System.out.printf("Value of thread active in vertareas %b\n",sharedStates.getThread_active().get());
                if (new_value) {
                    pausar_thread.setStyle("-fx-background-color: green;");
                } else{
                    pausar_thread.setStyle("-fx-background-color: red;");

                }
            }
        });

        createTable();
    }

    private void filter_data(String departamento) {
        filtered_data.clear();
        for (int i = 0; i < data.size(); i++) {
            TareaNodo tareaNodo = data.get(i);
            if (tareaNodo.getDepartamento().compareToIgnoreCase(departamento) == 0) {
                filtered_data.add(tareaNodo);
            }
        }
    }


    private void createTable() {
        table.setStyle("-fx-background-color: white; -fx-text-fill: black;");

        // Optional: Style the table cells specifically
        table.setRowFactory(tv -> {
            TableRow<TareaNodo> row = new TableRow<>();
            row.setStyle("-fx-background-color: white; -fx-text-fill: black;");
            return row;
        });


        // Create columns
        TableColumn<TareaNodo, String> departamentoCol = new TableColumn<>("Departamento");
        departamentoCol.setCellValueFactory(new PropertyValueFactory<>("departamento"));
        departamentoCol.setPrefWidth(150);

        TableColumn<TareaNodo, String> tareaCol = new TableColumn<>("Tarea");
        tareaCol.setCellValueFactory(new PropertyValueFactory<>("nombreTarea")); // Use camelCase
        tareaCol.setPrefWidth(200);

        TableColumn<TareaNodo, Integer> milisecondsCol = new TableColumn<>("Segundos");
        milisecondsCol.setCellValueFactory(new PropertyValueFactory<>("remainingSeconds"));
        milisecondsCol.setPrefWidth(150);

        table.getColumns().addAll(departamentoCol, tareaCol, milisecondsCol);

        // Set table width to match columns
        table.setPrefWidth(500);
        table.setMaxWidth(500);

        table.setItems(this.data);

    }

    private void changedaTable(TipoTarea tipoTarea) {
        this.current_tipoTarea_state = tipoTarea;
        ArrayList<TareaNodo> tareaNodoArrayList = new ArrayList<>();

        for (Observer observer : getObservers()) {
            if (observer instanceof AgregarTarea agregarTarea) {

                ArrayList<TareaNodo> result = observer.get_node_tarea_array(tipoTarea);
                if (result != null) {
                    tareaNodoArrayList = result;
                    System.out.println(tareaNodoArrayList.size());
                    break;
                }
            }
        }
        for (TareaNodo tareaNodo : tareaNodoArrayList) {
            System.out.println(tareaNodo.getValues());
        }
        data.clear();
        data.addAll(tareaNodoArrayList);
        filter_data(filtro.getValue());
    }

    @Override
    public void updateTable(TipoTarea tipoTarea) {
        System.out.println("Updating?");
        if (this.current_tipoTarea_state == tipoTarea) {
            changedaTable(tipoTarea);
        }

    }

    private void change_color_state(Button button_pressed,Button ... buttons_unpressed) {
       button_pressed.setStyle("-fx-background-color:orange");
       for (Button button:buttons_unpressed) {
           button.setStyle("-fx-background-color:white");
       }

    }

    @Override
    public void updateSecondsInTable(int seconds) {
        System.out.println("Actualizando segundos "+seconds);
        table.refresh();

    }
}


