package org.example.avanceproyecto.Controllers.Proyecto;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.example.avanceproyecto.ControllerUtils.*;
import org.example.avanceproyecto.ControllerUtils.Observer;
import org.example.avanceproyecto.Controllers.Proyecto.cells.EmpleadoCell;
import org.example.avanceproyecto.Controllers.Proyecto.cells.TareaCell;
import org.example.avanceproyecto.Controllers.Proyecto.objects.EmpleadoTarea;
import org.example.avanceproyecto.Controllers.Proyecto.objects.ProyectoObjectAsignado;
import org.example.avanceproyecto.Controllers.Proyecto.objects.ProyectoObjectCreados;
import org.example.avanceproyecto.Controllers.Proyecto.objects.TareaObject;
import org.example.avanceproyecto.Controllers.SharedStates;
import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AsignadorProyecto implements Observer {

    @FXML
    ChoiceBox<ProyectoObjectCreados> proyecto_seleccionar_choicebox;
    @FXML Label tareas_asignadas_label;
    @FXML
    TableView<EmpleadoTarea> asignados_table;
    @FXML
    Button crear_equipo_proyecto;

    @FXML
    private TableColumn<EmpleadoTarea, String> column_empleado;
    @FXML
    private TableColumn<EmpleadoTarea, String> column_tarea;

    private ObservableList<EmpleadoTarea> data = FXCollections.observableArrayList(); //Fata de todos los tareas nodos
    AtomicInteger tareas_asignadas = new AtomicInteger(0);
    private ObjectProperty<ProyectoObjectCreados> current_proyecto = new SimpleObjectProperty<>();
    Parent layout;
    private SharedStates sharedStates;
    private ProyectoShared prsh;

    ArrayList<Observer> observers = new ArrayList<>();

    public ArrayList<Observer> getObservers() {
        return observers;
    }

    public AsignadorProyecto(SharedStates sharedStates, ProyectoShared proyectoShared) {
        this.prsh = proyectoShared;
        this.sharedStates = sharedStates;
        this.layout = Utils.load_fxml("/FXML/Proyecto/AsignarProyecto.fxml", this);
        initialize_data_elements();
        choicebox_proyecto_seleccionar_listener();
        setupLabelBinding();
        settingUpTableColumns();

        EmpleadoTarea empleadoTarea = new EmpleadoTarea();
        data.add(empleadoTarea);
        asignados_table.setItems(data);
        asignados_table.setDisable(true);

        crear_equipo_proyecto_onAction();
    }


    private void settingUpTableColumns() {

        asignados_table.setStyle("-fx-background-color: white; -fx-text-fill: black;");

        asignados_table.setRowFactory(tv -> {
            TableRow<EmpleadoTarea> row = new TableRow<EmpleadoTarea>() {
                @Override
                protected void updateItem(EmpleadoTarea item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        // No border for empty rows
                        setStyle("-fx-background-color: white;");
                    } else {
                        // Border only for rows with data
                        setStyle("-fx-background-color: white; " +
                                "-fx-text-fill: black; " +
                                "-fx-border-color: #d3d3d3; " +
                                "-fx-border-width: 0 0 1 0;");
                    }
                }
            };
            return row;
        });


        asignados_table.setEditable(true);
        column_empleado.setEditable(true);
        column_tarea.setEditable(true);

        column_empleado.setCellFactory(column -> new EmpleadoCell(sharedStates));
        column_tarea.setCellFactory(c -> new TareaCell(current_proyecto, sharedStates, tareas_asignadas));


        column_tarea.addEventHandler(TableColumn.CellEditEvent.ANY, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                if (event.getEventType() == TableColumn.editCommitEvent()) {
                    TableColumn.CellEditEvent<EmpleadoTarea, String> commitEvent =
                            (TableColumn.CellEditEvent<EmpleadoTarea, String>) event;

                    System.out.println("Cell committed: " + commitEvent.getNewValue());
                }
            }
        });
    }

    private void crear_equipo_proyecto_onAction() {
        crear_equipo_proyecto.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                ProyectoObjectCreados proyectoObject = current_proyecto.get();
                if (isTableDataValid(proyectoObject)) {
                    ArrayList<EmpleadoTarea> empleadoTareas = new ArrayList<>(data);

                    JSONObject proyectoJson = ProyectoObjectAsignado.createJson(proyectoObject, empleadoTareas); //Proyecto object con datos de proyectom , empleados y tareas
                    ProyectoObjectAsignado proyectoObjectAsignado = ProyectoObjectAsignado.fromJson(proyectoJson);

                    prsh.getProyectos_asignados().put(proyectoJson.getString("proyecto_nombre"), proyectoJson);

                    Utils.writeJson(prsh.getProyectos_asignados().toString(4), "data", "proyectos_asignados.json");
                    proyecto_seleccionar_choicebox.getItems().remove(proyectoObject);

                    prsh.getProyectos_creados().remove(proyectoJson.getString("proyecto_nombre"));
                    Utils.writeJson(prsh.getProyectos_creados().toString(4), "data", "proyectos_creados.json");

                    clear_elements();
                    Toast.show(sharedStates.getStage(),"Asignacion de Proyecto exitoso");

                    for (Observer observer: observers) {
                        observer.proyecto_has_been_assigned(proyectoObject,proyectoObjectAsignado);
                    }
                }
            }
        });
    }

    private void clear_elements() {
        proyecto_seleccionar_choicebox.setValue(null);
        current_proyecto.set(null);
        asignados_table.setDisable(true);
        data.clear();
    }

    /**
     * Validar que rows == tareas size
     * Columna empleados not empty
     * Columna empleados different values
     * Columna tareas not empty
     * Columna tareas different values
     * Proyecto seleccionado
     */
    private boolean isTableDataValid(ProyectoObjectCreados proyectoObject) {

        if (current_proyecto.get() == null) {
            Alert alert = Utils.get_alert_position_centered(
                    sharedStates.getStage(),
                    Alert.AlertType.WARNING,
                    "Advertencia",
                    "Proyecto no seleccionado",
                    "Tiene que seleccionar un proyecto, o crear previamente"
            );
            alert.showAndWait();
            return false;
        }

        // Validation 1: Check if rows == tareas size
        if (proyectoObject.getTareasSize() != data.size()) {
            Alert alert = Utils.get_alert_position_centered(
                    sharedStates.getStage(),
                    Alert.AlertType.WARNING,
                    "Advertencia",
                    "Cantidad incorrecta",
                    "El número de filas (" + data.size() + ") debe ser igual al número de tareas (" + proyectoObject.getTareasSize() + ")"
            );
            alert.showAndWait();
            return false;
        }

        // Collections to track assigned values
        Set<String> empleadosAsignados = new HashSet<>();
        Set<String> tareasAsignadas = new HashSet<>();

        // Validation 2-5: Check each row
        for (int i = 0; i < data.size(); i++) {
            EmpleadoTarea empleadoTarea = data.get(i);

            // Validation 2: Columna empleados not empty
            if (empleadoTarea.getEmpleado() == null || empleadoTarea.getEmpleado().getFullName().trim().isEmpty()) {
                showValidationAlert("Campo vacío", "El empleado en la fila " + (i + 1) + " no puede estar vacío");
                return false;
            }

            // Validation 4: Columna tareas not empty
            TareaObject tareaObject = empleadoTarea.getTareaObject();
            if (tareaObject == null || tareaObject.getTitle().trim().isEmpty()) {
                showValidationAlert("Campo vacío", "La tarea en la fila " + (i + 1) + " no puede estar vacía");
                return false;
            }

            // Validation 3: Columna empleados different values (no duplicates)
            String empleado = empleadoTarea.getEmpleado().getFullName().trim();
            if (empleadosAsignados.contains(empleado)) {
                showValidationAlert("Empleado duplicado", "El empleado '" + empleado + "' ya está asignado en otra fila");
                return false;
            }
            empleadosAsignados.add(empleado);

            // Validation 5: Columna tareas different values (no duplicates)
            String tarea = tareaObject.getTitle().trim();
            if (tareasAsignadas.contains(tarea)) {
                showValidationAlert("Tarea duplicada", "La tarea '" + tarea + "' ya está asignada en otra fila");
                return false;
            }
            tareasAsignadas.add(tarea);
        }
        return true;
    }

    private void showValidationAlert(String title, String message) {
        Alert alert = Utils.get_alert_position_centered(
                sharedStates.getStage(),
                Alert.AlertType.WARNING,
                "Validación",
                title,
                message
        );
        alert.showAndWait();
    }


    public void setupLabelBinding() {
        current_proyecto.addListener((obs, oldProject, newProject) -> {
            if (newProject == null) {
                tareas_asignadas_label.setText("Proyecto no seleccionado");
            } else {
                tareas_asignadas_label.setText(String.format("Tareas totales : %d",
                        newProject.getTareasSize()));
            }
        });
        tareas_asignadas_label.setText("Proyecto no seleccionado");

    }


    private void initialize_data_elements() {

        Iterator<String> proyectos_keys = prsh.getProyectos_creados().keys();
        while (proyectos_keys.hasNext()) {
            String pro_name = proyectos_keys.next();
            JSONObject proyecto_json = prsh.getProyectos_creados().getJSONObject(pro_name);
            ProyectoObjectCreados proyectoObject = ProyectoObjectCreados.fromJson(proyecto_json);
            proyecto_seleccionar_choicebox.getItems().add(proyectoObject);
        }
    }

    private void choicebox_proyecto_seleccionar_listener() {
        proyecto_seleccionar_choicebox.setValue(null);
        proyecto_seleccionar_choicebox.setConverter(new StringConverter<ProyectoObjectCreados>() {
            @Override
            public String toString(ProyectoObjectCreados proyectoObject) {
                if (proyectoObject == null) return "Seleccionar";
                return proyectoObject.getProycto_name();
            }

            @Override
            public ProyectoObjectCreados fromString(String s) {
                return null;
            }
        });


        proyecto_seleccionar_choicebox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ProyectoObjectCreados>() {
            @Override
            public void changed(ObservableValue<? extends ProyectoObjectCreados> observableValue, ProyectoObjectCreados old, ProyectoObjectCreados new_obj) {
                tareas_asignadas.set(0);
                current_proyecto.set(new_obj);
                asignados_table.setDisable(false);

                adjust_rows_to_match_target_size();
//                    if (adjust_rows_to_match_target_size()){
//                        asignados_table.refresh();
//                    }
            }

        });
    }

    private boolean adjust_rows_to_match_target_size() {
        if (current_proyecto.get() == null) {
            data.clear();
            return false;
        }

        boolean changed = false;
        int targetSize = current_proyecto.get().getTareas_proyecto().size();

        // Add rows if we need more
        while (data.size() < targetSize) {
            EmpleadoTarea newRow = new EmpleadoTarea();
            data.add(newRow);
            changed = true;
        }

        // Remove rows if we have too many
        while (data.size() > targetSize) {
            data.remove(data.size() - 1);
            changed = true;
        }

        return changed;
    }


    public Parent getLayout() {
        return this.layout;
    }

    @Override
    public void init() {

    }

    @Override
    public void proyecto_has_been_created(ProyectoObjectCreados proyecto) {
        proyecto_seleccionar_choicebox.getItems().add(proyecto);
    }
}