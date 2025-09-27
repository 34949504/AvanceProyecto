package org.example.avanceproyecto.Controllers.Proyecto;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lombok.Getter;
import lombok.Setter;
import org.example.avanceproyecto.ControllerUtils.*;
import org.example.avanceproyecto.ControllerUtils.Dialogs.EmpleadoSelectionDialog;
import org.example.avanceproyecto.ControllerUtils.Dialogs.TaskDialog;
import org.example.avanceproyecto.Controllers.SharedStates;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

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
    public CreadorProyecto creadorProyecto;
    public AsignadorProyecto asignadorProyecto;


    private ButtonColorManager buttonColorManager;

    @Override
    public void init() {
        onActionButtons();
        buttonColorManager = new ButtonColorManager(ver_proyecto_button, asignar_proyecto_button, crear_proyecto_button);
        buttonColorManager.change_color_state(crear_proyecto_button);

        creadorProyecto = new CreadorProyecto();
        asignadorProyecto = new AsignadorProyecto();

        this.addObservers(asignadorProyecto);

        border_pane.setCenter(creadorProyecto.getCreadorProyectoBorderPane());
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


    /**
     * Objecto tarea: Titulo de tarea, descripcion de tarea
     */

    @Getter
    public class CreadorProyecto {

        @FXML
        private Label fecha_creacion_label;
        @FXML
        private Button datos_proyecto_button;
        @FXML
        private Button tareas_proyecto_button;
        @FXML
        private TextField nombre_proyecto_textfield;
        @FXML
        private DatePicker fecha_de_entraga_datefield;
        @FXML
        private Button borrar_proyecto_button;
        @FXML
        private Button crear_proyecto_button;
        @FXML
        private TextArea descripcion_textarea;


        BorderPane creadorProyectoBorderPane;
        Parent datos_proyecto_layout;
        Parent tareas_proyecto_layout;
        ButtonColorManager buttonColorManager;

        CreadorTarea creardorTarea = new CreadorTarea();

        public CreadorProyecto() {
            creadorProyectoBorderPane = (BorderPane) Utils.load_fxml("/FXML/Proyecto/CrearProyecto.fxml", this);
            tareas_proyecto_layout = creardorTarea.getLayout();
            datos_proyecto_layout = (Parent) creadorProyectoBorderPane.getCenter();

            if (datos_proyecto_layout instanceof BorderPane) {
                System.out.println("WOw its borderpane");
            }

            buttonColorManager = new ButtonColorManager(tareas_proyecto_button, datos_proyecto_button);
            buttonColorManager.change_color_state(datos_proyecto_button);
            onActionButtons();
            fecha_creacion_proyecto();

        }

        private void onActionButtons() {
            onAction_datos_proyecto();
            onAction_tareas_proyecto();
            onAction_borrar_proyecto();
            onAction_crear_proyecto_enviar();
        }
//        private void

        private void onAction_crear_proyecto_enviar() {

            crear_proyecto_button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    JSONObject nuevo_proyecto_json = validar_datos();
                    if (nuevo_proyecto_json != null) {
                        String proyecto_nombre = nuevo_proyecto_json.getString("proyecto_nombre");
                        proyectos_creados.put(proyecto_nombre, nuevo_proyecto_json);
                        Utils.writeJson(proyectos_creados.toString(4), "data", "proyectos_creados.json");
                        Toast.show(getStage(), "Proyecto creado exitosamente");
                        creardorTarea.clearEverything();

                        for (Observer observer: getObservers()) {
                            observer.proyecto_has_been_created(nuevo_proyecto_json);
                        }
                    }

                }
            });
        }

        private JSONObject validar_datos() {
            StringBuilder datos_faltantes = new StringBuilder();
            String nombre_proyecto_value = nombre_proyecto_textfield.getText();
            String fecha_de_entraga_datefield_value = fecha_de_entraga_datefield.getValue() == null ? "" : fecha_de_entraga_datefield.getValue().toString();
            String descripcion_textarea_value = descripcion_textarea.getText();

            isDatoEmpty(datos_faltantes, nombre_proyecto_value, "Nombre de proyecto");
            isDatoEmpty(datos_faltantes, fecha_de_entraga_datefield_value, "Fecha de entrega");
            isDatoEmpty(datos_faltantes, descripcion_textarea_value, "Descripcion de proyecto");

            if (creardorTarea.isTaskSizeCero()) {
                datos_faltantes.append("EL proyecto tiene 0 tareas");
            }
            System.out.println(datos_faltantes.toString());
            if (!datos_faltantes.isEmpty()) {
                Alert alert = Utils.get_alert_position_centered(getStage(), Alert.AlertType.WARNING, "Advertencia", "Datos faltantes:", datos_faltantes.toString());
                alert.showAndWait();
                return null;
            }

            JSONObject nuevo_proyecto_json = new JSONObject();
            JSONArray tareas_array = new JSONArray();

            for (CardTarea cardTarea : creardorTarea.cardTareaArrayList) {
                JSONObject tarea_json = new JSONObject();
                String tarea_title = cardTarea.title.get();
                String tarea_desc = cardTarea.desc.get();
                tarea_json.put("title", tarea_title);
                tarea_json.put("desc", tarea_desc);
                tareas_array.put(tarea_json);
            }
            nuevo_proyecto_json.put("proyecto_nombre", nombre_proyecto_value);
            nuevo_proyecto_json.put("descripcion", descripcion_textarea_value);
            nuevo_proyecto_json.put("fecha_de_entrega", fecha_de_entraga_datefield_value);
            nuevo_proyecto_json.put("tareas_proyecto", tareas_array);
            return nuevo_proyecto_json;
        }

        private void isDatoEmpty(StringBuilder datos_faltantes, String dato, String message) {
            if (dato.isEmpty()) {
                datos_faltantes.append(message + "\n");
            }
        }


        private void onAction_datos_proyecto() {
            datos_proyecto_button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    buttonColorManager.change_color_state((Button) actionEvent.getSource());
                    creadorProyectoBorderPane.setCenter(datos_proyecto_layout);

                }
            });
        }

        private void onAction_tareas_proyecto() {
            tareas_proyecto_button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    buttonColorManager.change_color_state((Button) actionEvent.getSource());
                    creadorProyectoBorderPane.setCenter(tareas_proyecto_layout);

                }
            });
        }

        private void onAction_borrar_proyecto() {

            borrar_proyecto_button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {

                    boolean descartar = Utils.showConfirmation("Borrar proyecto en proceso", "Descartar proyecto", "Al descartar el proyecto se perdera los datos y las tareas creadas");
                    if (descartar) {
                        creardorTarea.clearEverything();
                    }
                }
            });

        }

        private void fecha_creacion_proyecto() {
            fecha_creacion_label.setText(fecha_creacion_label.getText() + Utils.getTodaysDate());
        }

        public class CreadorTarea {
            @FXML
            private GridPane grid_tareas;
            @FXML
            private VBox left_vbox_grid;
            @FXML
            private VBox right_vbox_grid;
            @FXML
            private Button agregar_tarea_button;

            //            private TaskDialog taskDialog = new TaskDialog(getStage());
//            public TaskDialog taskDialog;
            public org.example.avanceproyecto.ControllerUtils.Dialogs.TaskDialog taskDialog;
            CreadorTarea clazz = this;

            ArrayList<CardTarea> cardTareaArrayList = new ArrayList<>();
            Parent layout;
            int card_count = 0;

            public void clearEverything() {
                card_count = 0;
                cardTareaArrayList.clear();
                left_vbox_grid.getChildren().clear();
                right_vbox_grid.getChildren().clear();
                nombre_proyecto_textfield.setText("");
                fecha_de_entraga_datefield.setValue(null);
                descripcion_textarea.setText("");
            }

            public boolean isTaskSizeCero() {
                return cardTareaArrayList.size() == 0;
            }

            public CreadorTarea() {
                layout = Utils.load_fxml("/FXML/Proyecto/CrearProyecto_tarea_menu.fxml", this);

                agregar_tarea_button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        if (taskDialog == null) {
                            Stage stage = (Stage) agregar_tarea_button.getScene().getWindow();
                            taskDialog = new TaskDialog(stage);
                        }
                        if (taskDialog.show()) {

                            TaskDialog.TaskResult taskResult = taskDialog.getDialogResult();
                            String title = taskResult.getTitle();
                            String description = taskResult.getDescription();
                            taskDialog.clear();

                            CardTarea cardTarea = new CardTarea(title, description, card_count++, clazz);
                            cardTareaArrayList.add(cardTarea);
                            redistributeAllCards();
                        }

                    }
                });
            }

            public void removeCard(CardTarea cardTarea) {
                // Remove from the list
                cardTareaArrayList.remove(cardTarea);

                // Redistribute all remaining cards
                redistributeAllCards();
            }

            private void redistributeAllCards() {
                // Clear both VBoxes
                left_vbox_grid.getChildren().clear();
                right_vbox_grid.getChildren().clear();

                // Add cards back in alternating pattern
                for (int i = 0; i < cardTareaArrayList.size(); i++) {
                    if (i % 2 == 0) {
                        left_vbox_grid.getChildren().add(cardTareaArrayList.get(i));
                    } else {
                        right_vbox_grid.getChildren().add(cardTareaArrayList.get(i));
                    }
                }
            }

            public Parent getLayout() {
                return layout;
            }
        }


        public class CardTarea extends VBox {
            StringProperty title = new SimpleStringProperty();
            StringProperty desc = new SimpleStringProperty();
            int count;
            CardTarea clazz = this;
            CreadorTarea creardorTarea;

            public CardTarea(String title, String description, int card_count, CreadorTarea creardorTarea) {
                this.title.set(title);
                this.desc.set(description);
                this.creardorTarea = creardorTarea;
                this.count = card_count;

                this.setId("bubula");
                this.setMaxHeight(Region.USE_COMPUTED_SIZE);
                this.setMaxWidth(Region.USE_COMPUTED_SIZE);
                this.setMinHeight(Region.USE_COMPUTED_SIZE);
                this.setPrefHeight(151.0);
                this.setPrefWidth(394.0);
                this.setStyle("-fx-border-color: black;");

                // AnchorPane
                AnchorPane anchorPane = new AnchorPane();
                anchorPane.setMaxHeight(Double.MAX_VALUE);
                anchorPane.setMaxWidth(Region.USE_COMPUTED_SIZE);
                anchorPane.setMinWidth(Region.USE_COMPUTED_SIZE);
                anchorPane.setPrefHeight(500.0);
                anchorPane.setPrefWidth(450.0);

                // ImageView
                ImageView imageView = new ImageView();
                imageView.setFitHeight(40.0);
                imageView.setFitWidth(40.0);
                imageView.setPickOnBounds(true);
                imageView.setPreserveRatio(true);
                Image image = new Image(getClass().getResourceAsStream("/images/red_x.png"));
                imageView.setImage(image);
                AnchorPane.setRightAnchor(imageView, 5.0);

                imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        creardorTarea.removeCard(CardTarea.this);
                        mouseEvent.consume();
                    }
                });

                // Label - use the actual title
                Label label = new Label(title);
                label.setAlignment(Pos.CENTER);
                label.setLayoutY(53.0);
                label.setMaxWidth(Double.MAX_VALUE);
                label.setMinWidth(Region.USE_COMPUTED_SIZE);
                label.setPrefWidth(450.0);
                label.setTextAlignment(TextAlignment.CENTER);
                label.setWrapText(true);
                label.setFont(Font.font(33.0));
                AnchorPane.setBottomAnchor(label, 0.0);

                label.textProperty().bind(this.title);
//                descripcion_textarea.textProperty().bind(this.desc);

                // Add children
                anchorPane.getChildren().addAll(imageView, label);
                this.getChildren().add(anchorPane);

                this.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        creardorTarea.taskDialog.setTitle(clazz.title.get());
                        creardorTarea.taskDialog.setDescription(clazz.desc.get());

                        if (creardorTarea.taskDialog.show()) {
                            TaskDialog.TaskResult result = creardorTarea.taskDialog.getDialogResult();
                            clazz.title.set(result.getTitle());
                            clazz.desc.set(result.getDescription());
                        }
                    }
                });
            }
        }


    }

    public class AsignadorProyecto implements Observer {

        @FXML
        ChoiceBox<ProyectoObject> proyecto_seleccionar_choicebox;
        @FXML
        Label tareas_asignadas_label;
        @FXML
        TableView<EmpleadoTarea> asignados_table;
        @FXML
        Button crear_equipo_proyecto;
        @FXML
        Button agregar_row_button;

        private ObservableList<EmpleadoTarea> data = FXCollections.observableArrayList(); //Fata de todos los tareas nodos
        @FXML
        private TableColumn<EmpleadoTarea, String> column_empleado;
        @FXML
        private TableColumn<EmpleadoTarea, String> column_tarea;

        Integer tareas_asignadas = 0;
        private ObjectProperty<ProyectoObject> current_proyecto = new SimpleObjectProperty<>();


        Parent layout;

        public AsignadorProyecto() {
            this.layout = Utils.load_fxml("/FXML/Proyecto/AsignarProyecto.fxml", this);
            initialize_data_elements();
            choicebox_proyecto_seleccionar_listener();
            setupLabelBinding();
            settingUpTableColumns();
            addRow_listener();

            EmpleadoTarea empleadoTarea = new EmpleadoTarea();
            data.add(empleadoTarea);
            asignados_table.setItems(data);
            asignados_table.setDisable(true);
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

            column_empleado.setCellFactory(column -> new EmpleadoCell(getStage(), getSharedStates()));
            column_tarea.setCellFactory(c -> new TareaCell(current_proyecto,column_tarea));
        }

        private void addRow_listener() {
            agregar_row_button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    data.add(new EmpleadoTarea());
                }
            });
        }


        public void setupLabelBinding() {
            current_proyecto.addListener((obs, oldProject, newProject) -> {
                if (newProject == null) {
                    tareas_asignadas_label.setText("Proyecto no seleccionado");
                } else {
                    tareas_asignadas_label.setText(String.format("Tareas asignadas : %d/%d",
                            tareas_asignadas, newProject.getTareasSize()));
                }
            });
            tareas_asignadas_label.setText("Proyecto no seleccionado");

        }


        private void initialize_data_elements() {

            Iterator<String> proyectos_keys = proyectos_creados.keys();
            while (proyectos_keys.hasNext()) {
                String pro_name = proyectos_keys.next();
                JSONObject proyecto_json = proyectos_creados.getJSONObject(pro_name);
                ProyectoObject proyectoObject = ProyectoObject.createProyectoObjectFromJson(proyecto_json);
                proyecto_seleccionar_choicebox.getItems().add(proyectoObject);
            }
        }

        private void choicebox_proyecto_seleccionar_listener() {
            proyecto_seleccionar_choicebox.setValue(null);
            proyecto_seleccionar_choicebox.setConverter(new StringConverter<ProyectoObject>() {
                @Override
                public String toString(ProyectoObject proyectoObject) {
                    if (proyectoObject == null) return "Seleccionar";
                    return proyectoObject.getProycto_name();
                }

                @Override
                public ProyectoObject fromString(String s) {
                    return null;
                }
            });


            proyecto_seleccionar_choicebox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ProyectoObject>() {
                @Override
                public void changed(ObservableValue<? extends ProyectoObject> observableValue, ProyectoObject old, ProyectoObject new_obj) {
                    tareas_asignadas = 0;
                    current_proyecto.set(new_obj);
                    asignados_table.setDisable(false);

                    if (adjust_rows_to_match_target_size()){
                        asignados_table.refresh();
                    }
                }

            });
        }

        private boolean adjust_rows_to_match_target_size() {
            boolean changed = false;
            int targetSize = current_proyecto.get().tareas_proyecto.size();

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
        public void proyecto_has_been_created(JSONObject proyecto) {
            ProyectoObject proyectoObject = ProyectoObject.createProyectoObjectFromJson(proyecto);
            proyecto_seleccionar_choicebox.getItems().add(proyectoObject);
        }
    }

    public class VerProyecto {

    }

}


@Getter
@Setter
class ProyectoObject {

    String proycto_name;
    String descripcion;
    String fecha_de_entrega;
    ArrayList<TareaObject> tareas_proyecto = new ArrayList<>();

    public static ProyectoObject createProyectoObjectFromJson(JSONObject jsonObject) {
        ProyectoObject proyecto = new ProyectoObject();

        proyecto.setProycto_name(jsonObject.getString("proyecto_nombre"));
        proyecto.setDescripcion(jsonObject.getString("descripcion"));
        proyecto.setFecha_de_entrega(jsonObject.getString("fecha_de_entrega"));

        JSONArray tareasArray = jsonObject.getJSONArray("tareas_proyecto");
        for (int i = 0; i < tareasArray.length(); i++) {
            JSONObject tareaJson = tareasArray.getJSONObject(i);

            TareaObject tarea = new TareaObject();
            tarea.setTitle(tareaJson.getString("title"));
            tarea.setDesc(tareaJson.getString("desc"));

            proyecto.getTareas_proyecto().add(tarea);
        }
        return proyecto;
    }

    public int getTareasSize() {
        return tareas_proyecto.size();
    }
}

@Getter
@Setter
class TareaObject {
    String title;
    String desc;
}


@Getter
@Setter
class EmpleadoTarea {
    Empleado empleado;
    TareaObject tareaObject;
}


//Cells
class EmpleadoCell extends TableCell<EmpleadoTarea, String> {
    private TextField textField;
    private static EmpleadoSelectionDialog employeeSelectionDialog;


    public EmpleadoCell(Stage stage, SharedStates sharedStates) {
        employeeSelectionDialog = new EmpleadoSelectionDialog(stage, sharedStates);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        Empleado empleado = getValue();
        if (empty || empleado == null) {
            setText(null);
            setGraphic(null);
        } else {
            setText(empleado.getFullName());

        }
    }

    @Override
    public void startEdit() {
        super.startEdit();

        Empleado currentValue = getValue();
        if (currentValue != null) {
            employeeSelectionDialog.set_choice_boxes_to_current_value(currentValue); //Does
            setText(currentValue.getFullName());
        }

        if (employeeSelectionDialog.show()) {
            EmpleadoSelectionDialog.EmployeeSelectionResult employeeSelectionResult = employeeSelectionDialog.getDialogResult();
            Empleado empleado = employeeSelectionResult.getEmpleado();
            setValue(empleado);
            commitEdit(empleado.getFullName());
        }
        cancelEdit();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        if (getValue() != null)
            setText(getValue().getFullName());
    }

    @Override
    public void commitEdit(String newValue) {
        super.commitEdit(newValue);
        setText(newValue);
    }


    private Empleado getValue() {
        TableRow<EmpleadoTarea> tableRow = getTableRow();
        if (tableRow != null && tableRow.getItem() != null) {
            EmpleadoTarea empleadoTarea = tableRow.getItem();
            return  empleadoTarea.getEmpleado();
        }
        System.out.println("get value is null");
        return null;
    }
    private void setValue(Empleado empleado) {
        TableRow<EmpleadoTarea> tableRow = getTableRow();
        if (tableRow != null && tableRow.getItem() != null) {
            EmpleadoTarea empleadoTarea = tableRow.getItem();
            empleadoTarea.setEmpleado(empleado);
        }
    }
}



class TareaCell extends TableCell<EmpleadoTarea, String> {
    private ChoiceBox<TareaObject> choiceBox = new ChoiceBox<>();
    private ArrayList<TareaObject> tareas_escogidas = new ArrayList<>();
    private static TableColumn<EmpleadoTarea,String> column;

    public TareaCell(ObjectProperty<ProyectoObject> current_proyecto,TableColumn<EmpleadoTarea,String> empleadoTareaTareaObjectTableColumn) {
        column = empleadoTareaTareaObjectTableColumn;

       current_proyecto.addListener(new ChangeListener<ProyectoObject>() {
           @Override
           public void changed(ObservableValue<? extends ProyectoObject> observableValue, ProyectoObject proyectoObject, ProyectoObject t1) {
               tareas_escogidas.clear();
               choiceBox.getItems().clear();
               choiceBox.setValue(null);
               for (TareaObject s: t1.getTareas_proyecto()) {
                  choiceBox.getItems().add(s);
               }
               if (proyectoObject != null)
                   clearTareaColumn();
           }
       });

       choiceBox.setConverter(new StringConverter<TareaObject>() {
           @Override
           public String toString(TareaObject tareaObject) {
               if (tareaObject != null) {
                   return  tareaObject.getTitle();
               }
               return  null;
           }

           @Override
           public TareaObject fromString(String s) {
               return null;
           }
       });

       choiceBox.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent actionEvent) {
               System.out.println("this was called when i cleared?");
               if (choiceBox.getValue() != null) {

               tareas_escogidas.add(choiceBox.getValue());
               setValue(choiceBox.getValue());
               commitEdit(choiceBox.getValue().title);
               }
           }
       });

       choiceBox.focusedProperty().addListener(new ChangeListener<Boolean>() {
           @Override
           public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
               if (t1 == false) {
                   setValue(choiceBox.getValue());
                   commitEdit(choiceBox.getValue().title);
               }

           }
       });
        setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !isEmpty() && !isEditing()) {
                startEdit();
                // Show dropdown immediately after starting edit
                Platform.runLater(() -> choiceBox.show());
            }
        });
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            System.out.println("item is "+item);
            setText(item);
        }
    }

    @Override
    public void startEdit() {
        super.startEdit();
        setGraphic(choiceBox);
        Platform.runLater(() -> {
        choiceBox.requestFocus();
        choiceBox.show();
    });
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        System.out.println("cancelling edit");
        if (getValue() !=null) {
            System.out.println("value is "+getValue().getTitle());
            setText(getValue().getTitle());
            setValue(getValue());
        }
        setGraphic(null);
    }

    @Override
    public void commitEdit(String newValue) {
        super.commitEdit(newValue);
        setText(newValue);
        setGraphic(null);
    }

    private void setValue(TareaObject tareaObject) {
        TableRow<EmpleadoTarea> tableRow = getTableRow();
        if (tableRow != null && tableRow.getItem() != null) {
            EmpleadoTarea empleadoTarea = tableRow.getItem();
            empleadoTarea.setTareaObject(tareaObject);
        }
    }

    private TareaObject  getValue() {
        TableRow<EmpleadoTarea> tableRow = getTableRow();
        if (tableRow != null && tableRow.getItem() != null) {
            EmpleadoTarea empleadoTarea = tableRow.getItem();
            return  empleadoTarea.getTareaObject();
        }
        System.out.println("get value is null");
        return null;
    }
    private void clearTareaColumn() {
        TableView<EmpleadoTarea> table = getTableView();

        // Clear all data first
        for (EmpleadoTarea empleadoTarea : table.getItems()) {
            empleadoTarea.setTareaObject(null);
        }

        // Then update all cells in ONE Platform.runLater call
        Platform.runLater(() -> {
            Set<Node> cells = table.lookupAll(".table-cell");
            for (Node node : cells) {
                if (node instanceof TareaCell) {
                    TareaCell tareaCell = (TareaCell) node;
                    if (tareaCell.getTableRow() != null &&
                            tareaCell.getTableRow().getIndex() >= 0 &&
                            tareaCell.getTableRow().getIndex() < table.getItems().size()) {

                        // Clear both the cell display AND the choicebox value
                        tareaCell.updateItem(null, false);
                        tareaCell.choiceBox.setValue(null); // Clear the choicebox selection
                    }
                }
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
