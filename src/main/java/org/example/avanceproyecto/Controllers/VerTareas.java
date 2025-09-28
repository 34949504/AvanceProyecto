/*
Se encarga de controlar el gui cuando ves las tareas
 */

package org.example.avanceproyecto.Controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.avanceproyecto.ControllerUtils.*;
import org.example.avanceproyecto.Tarea.TareaNodo;
import org.example.avanceproyecto.Tarea.TipoTarea;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class VerTareas extends BaseController implements Observer {

    private ObservableList<TareaNodo> data = FXCollections.observableArrayList(); //Fata de todos los tareas nodos
    private ObservableList<TareaNodo> filtered_data = FXCollections.observableArrayList(); //Data filtrada por departamento para poner en la tabla
    private TipoTarea current_tipoTarea_state = TipoTarea.Urgente; //Urgenete, No urgenete, Lista
    @FXML
    private Spinner<Integer> spinner_velocidad;

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
    private Button prioridad_button;

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
        change_color_state(urgentes_button,no_urgentes_button,lista_button,prioridad_button);
        Action_listaButton();
        Action_pausarThread();
        Action_UrgentesButton();
        Action_noUrgentesBUtton();
        Action_prioridadButton();
        Listener_filtro();
        Listener_spinnerVelocidad();


        createTable();
    }

    /**
     * Filtra la lista de data y pasa los valores filtrados a filtered data
     * @param departamento
     */
    private void filter_data(String departamento) {
        filtered_data.clear();
        for (int i = 0; i < data.size(); i++) {
            TareaNodo tareaNodo = data.get(i);
            if (tareaNodo.getDepartamento().compareToIgnoreCase(departamento) == 0) {
                filtered_data.add(tareaNodo);
            }
        }
    }


    /**
     * Codigo para crear la tabla
     */
    private void createTable() {
        table.setStyle("-fx-background-color: white; -fx-text-fill: black;");

        // Optional: Style the table cells specifically
        table.setRowFactory(tv -> {
            TableRow<TareaNodo> row = new TableRow<>();
            row.setStyle("-fx-background-color: white; -fx-text-fill: black;");
            return row;
        });

        // Create columns

        TableColumn<TareaNodo, String> departamentoCol = Utils.createColumn("Departamento","departamento",150);;
        TableColumn<TareaNodo, String> tareaCol = Utils.createColumn("Tarea","nombreTarea",150);;
        TableColumn<TareaNodo, String> milisecondsCol = Utils.createColumn("Segundos","remainingSeconds",150);;

        TableColumn<TareaNodo, String> empleado = new TableColumn<>("Responsable");
        empleado.setCellValueFactory(cellData -> {
            Empleado e = cellData.getValue().getEmpleadoAsignado();
            if (e == null) {
                return new SimpleStringProperty("—"); // or "Seleccionar"
            }
            return new SimpleStringProperty(e.getFullName()); // or whatever field you want
        });
        empleado.setPrefWidth(300);


        table.getColumns().addAll(departamentoCol, tareaCol,empleado ,milisecondsCol);

        // Set table width to match columns
        table.setPrefWidth(800);
        table.setMaxWidth(800);

        table.setItems(this.data);

    }

    /**
     * Actualiza los datos de la tabla
     * cuando una tarea es creada, eliminada, o cunado se cambia la vista de la tablas de urgentes, no urgenes y listas
     * @param tipoTarea
     */
    private void changedaTable(TipoTarea tipoTarea, Prioridad prioridad) {
        this.current_tipoTarea_state = tipoTarea;
        ArrayList<TareaNodo> tareaNodoArrayList = new ArrayList<>();

        for (Observer observer : getObservers()) {
            if (observer instanceof AgregarTarea agregarTarea) {

                ArrayList<TareaNodo> result = observer.get_node_tarea_array(tipoTarea,prioridad);
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

    /**
     * Es llamada por taskdoer cuando una tarea es terminada
     * @param tareaNodo
     */
    @Override
    public void tareaTerminada(TareaNodo tareaNodo) {
        TipoTarea tipoTarea = tareaNodo.getTipoTarea();
        Prioridad prioridad = tareaNodo.getPrioridad();
        System.out.println("Updating?");
        if (this.current_tipoTarea_state == tipoTarea) {
            changedaTable(tipoTarea,prioridad);
        }

        /**
         * TaskDoer, la clase apesta, primero la hize yo, pero no funciono, entonces despues de varias
         * iteraciones con chatgpt y claude, me devolvio eso, pero no se entienede nada (Se necesita volver hacer esa clase xd)
         * Necesito  llamar a otros observadores de aqui por TaskDoer sucks
         */
        for (Observer observer:getObservers()) {
            observer.tareaTerminada(tareaNodo);
        }

    }

    /**
     * Es llamado por agregarTareas despues de haber presionado el boton enviar
     * @param tareaNodo
     */
    @Override
    public void tarea_creada(TareaNodo tareaNodo) {
        TipoTarea tipoTarea = tareaNodo.getTipoTarea();
        Prioridad prioridad = tareaNodo.getPrioridad();
        System.out.println("Updating?");
        if (this.current_tipoTarea_state == tipoTarea) {
            changedaTable(tipoTarea,prioridad);
        }
    }

    /**
     * Se encarga de cambiar el color de los botones urgenest, no urgentes y listas
     * @param button_pressed
     * @param buttons_unpressed
     */
    private void change_color_state(Button button_pressed, Button ... buttons_unpressed) {
       button_pressed.setStyle("-fx-background-color:orange");
       for (Button button:buttons_unpressed) {
           button.setStyle("-fx-background-color:white");
       }

    }

    /**
     * Es llamada por taskdoer, en taskdoer se actualizan los segundos en los objectos y aqui solo llamamos refresh
     * @param seconds
     */
    @Override
    public void updateSecondsInTable(int seconds) {
        System.out.println("Actualizando segundos "+seconds);
        table.refresh();
    }

    /**
     * Se encarga de actualizar la duracion de sleep  de sharedStates que utiliza taskDOer
     */
    private void Listener_spinnerVelocidad() {

        spinner_velocidad.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observableValue, Integer integer, Integer t1) {
                SharedStates sharedStates = getSharedStates();
                AtomicInteger currentDelay = sharedStates.getSpeed();
                int baseSpeed = 1000;
                if (t1 == 0) {
                    currentDelay.set(baseSpeed); // Normal speed: 1000ms
                } else if (t1 > 0) {
                    currentDelay.set(baseSpeed - (t1 * 90));
                    if (currentDelay.get() < 100) currentDelay.set(100); // Minimum delay
                } else {
                    currentDelay.set(baseSpeed + (Math.abs(t1) * 100));
                }
            }
        });
    }

    /**
     * La vista de tareas urgentes activando
     */
    private void Action_UrgentesButton() {

        urgentes_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                titulo_nombre_tarea_label.setText(urgentes_button.getText());
                changedaTable(TipoTarea.Urgente,Prioridad.none);
                change_color_state(urgentes_button,no_urgentes_button,lista_button);
            }
        });
    }

    /**
     *La vista de tareas no urgentes activando
     */
    private void Action_noUrgentesBUtton() {

        no_urgentes_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                titulo_nombre_tarea_label.setText(no_urgentes_button.getText());
                changedaTable(TipoTarea.No_Urgente,Prioridad.none);
                change_color_state(no_urgentes_button,urgentes_button,lista_button);
            }
        });
    }
    private void Action_prioridadButton() {

        prioridad_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                titulo_nombre_tarea_label.setText(prioridad_button.getText());
                changedaTable(null,Prioridad.alta);
                change_color_state(prioridad_button,no_urgentes_button,urgentes_button,lista_button);
            }
        });
    }

    /**
     * La vista de tareas listas activando
     */
    private void Action_listaButton() {

        lista_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                titulo_nombre_tarea_label.setText(lista_button.getText());
                changedaTable(TipoTarea.Lista,Prioridad.none);
                change_color_state(lista_button,urgentes_button,no_urgentes_button);
            }
        });
    }

    /**
     * Escucha cambios en el filtro checkbox, filtra o regresa data normal
     */
    private void Listener_filtro() {

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
    }
    /**
     * Funcion de pausar la realizacion de tareas, cambia el estado atomico de sharedShared THreadActive
     * que es utilizado por taskdoer
     */
    private void Action_pausarThread() {

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
    }
}


