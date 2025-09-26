package org.example.avanceproyecto.Controllers;

import javafx.beans.binding.Bindings;
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
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import kotlin.random.AbstractPlatformRandom;
import lombok.Getter;
import lombok.Setter;
import org.example.avanceproyecto.ControllerUtils.*;
import org.example.avanceproyecto.Tarea.TareaNodo;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Idea
 * Esta la seccion para crear proyecto, poner descripcion, nombre del proyecto, tareas a realizar, fecha de entrega  (textfields)
 * Seccion de asignar a empleados a proyectos, seleccionar departamento y una lista  de los empleados
 * Seccion explorar los proyectos (ver sus datos, tareas y los que estan trabajando en el
 * Seccion para marcar proyectos terminados (se quita el proyecto de json)
 * Grafos
 * Json donde se guarde  Nombre de proyecto -> datos de proyecto (key {}),empleados ({}) -> empleado: [tareas asignadas]
 */
@Getter @Setter
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
                        proyectos_creados.put(proyecto_nombre,nuevo_proyecto_json);
                        Utils.writeJson(proyectos_creados.toString(4),"data","proyectos_creados.json");
                        Toast.show(getStage(),"Proyecto creado exitosamente");
                        creardorTarea.clearEverything();
                    }

                }
            });
        }

        private JSONObject validar_datos() {
            StringBuilder datos_faltantes = new StringBuilder();
            String nombre_proyecto_value =  nombre_proyecto_textfield.getText();
            String fecha_de_entraga_datefield_value =  fecha_de_entraga_datefield.getValue() == null ? "":fecha_de_entraga_datefield.getValue().toString();
            String descripcion_textarea_value =  descripcion_textarea.getText();

            isDatoEmpty(datos_faltantes,nombre_proyecto_value,"Nombre de proyecto");
            isDatoEmpty(datos_faltantes,fecha_de_entraga_datefield_value,"Fecha de entrega");
            isDatoEmpty(datos_faltantes,descripcion_textarea_value,"Descripcion de proyecto");

            if (creardorTarea.isTaskSizeCero()) {
                datos_faltantes.append("EL proyecto tiene 0 tareas");
            }
            System.out.println(datos_faltantes.toString());
            if (!datos_faltantes.isEmpty()) {
                Alert alert = Utils.get_alert_position_centered(getStage(),Alert.AlertType.WARNING,"Advertencia","Datos faltantes:",datos_faltantes.toString());
                alert.showAndWait();
                return null;
            }

            JSONObject nuevo_proyecto_json = new JSONObject();
            JSONArray tareas_array = new JSONArray();

            for (CardTarea cardTarea: creardorTarea.cardTareaArrayList) {
                JSONObject tarea_json = new JSONObject();
                String tarea_title = cardTarea.title.get();
                String tarea_desc =  cardTarea.desc.get();
                tarea_json.put("title",tarea_title);
                tarea_json.put("desc",tarea_desc);
                tareas_array.put(tarea_json);
            }
            nuevo_proyecto_json.put("proyecto_nombre",nombre_proyecto_value);
            nuevo_proyecto_json.put("descripcion",descripcion_textarea_value);
            nuevo_proyecto_json.put("fecha_de_entrega",fecha_de_entraga_datefield_value);
            nuevo_proyecto_json.put("tareas_proyecto",tareas_array);
            return  nuevo_proyecto_json;
        }
        private void isDatoEmpty(StringBuilder datos_faltantes,String dato,String message) {
            if (dato.isEmpty()) {
                datos_faltantes.append(message +"\n");
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
            public TaskDialog taskDialog;
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
                        if (taskDialog.showDialog(true)) {
                            String title = taskDialog.getTitle();
                            String description = taskDialog.getDescription();

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

                        if (creardorTarea.taskDialog.showDialog(false)) {
                            clazz.title.set(creardorTarea.taskDialog.getTitle());
                            clazz.desc.set(creardorTarea.taskDialog.getDescription());
                        }
                    }
                });
            }
        }


    }

    public class AsignadorProyecto {

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
        @FXML private TableColumn<EmpleadoTarea,String> column_empleado;
        @FXML private TableColumn<EmpleadoTarea,String> column_tarea;

        int tareas_asignadas = 0;
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
        }

        private void settingUpTableColumns() {

            asignados_table.setStyle("-fx-background-color: white; -fx-text-fill: black;");
            asignados_table.setRowFactory(tv -> {
                TableRow<EmpleadoTarea> row = new TableRow<>();
                row.setStyle("-fx-background-color: white; -fx-text-fill: black;");
                return row;
            });

            asignados_table.setEditable(true);
            column_empleado.setEditable(true);
            column_tarea.setEditable(true);

            column_empleado.setCellFactory(column -> new EmpleadoCell());
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
                    if (proyectoObject == null)return "Seleccionar";
                    return  proyectoObject.getProycto_name();
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
                }

            });
        }


        public Parent getLayout() {
            return this.layout;
        }
    }

    public class VerProyecto {

    }

}


@Getter @Setter
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

@Getter @Setter
class TareaObject {
    String title;
    String desc;
}


@Getter @Setter
class EmpleadoTarea {
    Empleado empleado = new Empleado("","",0);
    String tarea;
}



//Cells
class EmpleadoCell extends TableCell<EmpleadoTarea, String> {
    private TextField textField;

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            // Check if we're currently editing THIS cell
            if (isEditing()) {
                setText(null);
                setGraphic(textField);
                if (textField != null) {
                    textField.setText(item);
                }
            } else {
                setText(item);
                setGraphic(null);
            }
        }
    }

    @Override
    public void startEdit() {
        super.startEdit();
        if (textField == null) {
            createTextField();
        }

        String currentValue = getValue();
        TableRow<EmpleadoTarea> tableRow = getTableRow(); if (tableRow != null && tableRow.getItem() != null) {
            EmpleadoTarea empleado = tableRow.getItem();
            currentValue = empleado.empleado.getEmpleadoName(); // or whatever getter method you have

        }

        textField.setText(currentValue);
        setGraphic(textField);
        setText(null);
        textField.requestFocus();
        textField.selectAll();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getValue());
        setGraphic(null);
    }

    @Override
    public void commitEdit(String newValue) {
        super.commitEdit(newValue);

        TableRow<EmpleadoTarea> tableRow = getTableRow();
        if (tableRow != null && tableRow.getItem() != null) {
            EmpleadoTarea empleado = tableRow.getItem();
            empleado.empleado.setEmpleadoName(newValue); // or whatever setter matches this column
        }

        setText(newValue);
        setGraphic(null);
    }

    private void createTextField() {
        textField = new TextField();
        textField.setOnAction(e -> {
            commitEdit(textField.getText());
        });
        textField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        });
    }

    private String getValue() {
        TableRow<EmpleadoTarea> tableRow = getTableRow();
        if (tableRow != null && tableRow.getItem() != null) {
            String currentValue = "";
            EmpleadoTarea empleado = tableRow.getItem();
            currentValue = empleado.empleado.getEmpleadoName(); // or whatever getter method you have
            return currentValue;
        }
        System.out.println("get value is null");
        return null;
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
