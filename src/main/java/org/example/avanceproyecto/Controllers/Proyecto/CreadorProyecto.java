package org.example.avanceproyecto.Controllers.Proyecto;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import lombok.Getter;
import org.example.avanceproyecto.ControllerUtils.*;
import org.example.avanceproyecto.ControllerUtils.Dialogs.TaskDialog;
import org.example.avanceproyecto.Controllers.Proyecto.objects.ProyectoObjectCreados;
import org.example.avanceproyecto.Controllers.SharedStates;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Controlador que te permite asignar datos a un proyecto y agregar las tareas que tiene el proyecto
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


    private SharedStates sharedStates;
    private ProyectoShared prsh;

    BorderPane creadorProyectoBorderPane;
    Parent datos_proyecto_layout;
    Parent tareas_proyecto_layout;
    ButtonColorManager buttonColorManager;




    CreadorTarea creardorTarea = new CreadorTarea();

    ArrayList<Observer> observers = new ArrayList<>();
    public void addObservers(Observer ... observers) {
        for(Observer observer:observers){
            this.observers.add(observer);
        }
    }
    public void addObservers(ArrayList<Observer> observers) {
        for(Observer observer:observers){
            this.observers.add(observer);
        }
    }

    public CreadorProyecto(SharedStates sharedStates, ProyectoShared proyectoShared) {
        this.prsh = proyectoShared;
        this.sharedStates = sharedStates;
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

    /**
     * Llama funcion validar datos
     * se agrega el objeto proyecto al json creados
     * Se escribe el json creados
     * Se notifica a las demas clases que un proyecto ha sido creado
     */
    private void onAction_crear_proyecto_enviar() {

        crear_proyecto_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                JSONObject nuevo_proyecto_json = validar_datos();
                if (nuevo_proyecto_json != null) {
                    String proyecto_nombre = nuevo_proyecto_json.getString("proyecto_nombre");

                    prsh.getProyectos_creados().put(proyecto_nombre, nuevo_proyecto_json);
                    Utils.writeJson(prsh.getProyectos_creados().toString(4), "data", "proyectos_creados.json");
                    Toast.show(sharedStates.getStage(), "Proyecto creado exitosamente");
                    creardorTarea.clearEverything();

                    for (Observer observer : observers) {
                        observer.proyecto_has_been_created(ProyectoObjectCreados.fromJson(nuevo_proyecto_json));
                    }
                }

            }
        });
    }


    /**
     * No puede haber campos vacio y por lo menos 1 tarea debe de ser creada
     * Regresa el objeto proyecto en forma de json
     * @return
     */
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
            Alert alert = Utils.get_alert_position_centered(sharedStates.getStage(), Alert.AlertType.WARNING, "Advertencia", "Datos faltantes:", datos_faltantes.toString());
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


    /**
     * Cambia el layout a datos
     */
    private void onAction_datos_proyecto() {
        datos_proyecto_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                buttonColorManager.change_color_state((Button) actionEvent.getSource());
                creadorProyectoBorderPane.setCenter(datos_proyecto_layout);

            }
        });
    }

    /**
     * Cambia el layout a tareas
     */
    private void onAction_tareas_proyecto() {
        tareas_proyecto_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                buttonColorManager.change_color_state((Button) actionEvent.getSource());
                creadorProyectoBorderPane.setCenter(tareas_proyecto_layout);

            }
        });
    }

    /**
     * Descarta los datos del proyecto y  sus tareas
     */
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

    /**
     * Controlador que te permite agregar tareas
     */
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

        /**
         * 2 vboxes
         * if i have
         *     card1     card2
         *     card3     card4
         *  and i remove card 2
         *     card1     card3
         *     card4
         */
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
            try {
                String css = getClass().getResource("/css/cardtarea.css").toExternalForm();
                this.getStylesheets().add(css);
            } catch (Exception e) {
                System.err.println("Could not load CardTarea CSS: " + e.getMessage());
            }

            this.title.set(title);
            this.desc.set(description);
            this.creardorTarea = creardorTarea;
            this.count = card_count;

            this.setId("bubula");
            this.setMaxHeight(Region.USE_COMPUTED_SIZE);
            this.setMaxWidth(Region.USE_COMPUTED_SIZE);
            this.setMinHeight(Region.USE_COMPUTED_SIZE);
            this.setPrefHeight(151.0);
            this.setPrefWidth(200.0);
            this.setMaxWidth(200);
//            this.setStyle("-fx-border-color: black;");

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

            control_width(anchorPane,label);
        }
        private void control_width(AnchorPane anchorPane,Label label) {

            int width = 300;

            this.setPrefWidth(width);
            this.setMaxWidth(width);
            anchorPane.setPrefWidth(width);
            label.setPrefWidth(width);
        }
    }


}
