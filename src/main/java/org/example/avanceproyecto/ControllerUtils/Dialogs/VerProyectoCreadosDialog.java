package org.example.avanceproyecto.ControllerUtils.Dialogs;

import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.avanceproyecto.Controllers.Proyecto.objects.ProyectoObjectCreados;
import org.example.avanceproyecto.Controllers.Proyecto.objects.TareaObject;

public class VerProyectoCreadosDialog extends BaseDialog {

    private ProyectoObjectCreados proyectoObject;
    private TextField nameField;
    private TextField fechaField;
    private TextArea descripcionArea;
    private ListView<TareaObject> tareasListView;
    private GridPane mainGrid;

    public VerProyectoCreadosDialog(Stage parentStage, String title, Rectangle2D dialog_dims) {
        super(parentStage, title, dialog_dims);
    }

    public void setProyectoObject(ProyectoObjectCreados proyecto) {
        this.proyectoObject = proyecto;
        loadProjectData();
    }


    private void initializeComponents() {
        // Project info fields - read only with custom styling
        nameField = new TextField();
        nameField.setEditable(false);
        nameField.getStyleClass().add("vpd-readonly-field");

        fechaField = new TextField();
        fechaField.setEditable(false);
        fechaField.getStyleClass().add("vpd-readonly-field");

        descripcionArea = new TextArea();
        descripcionArea.setEditable(false);
        descripcionArea.getStyleClass().add("vpd-description-area");
        descripcionArea.setPrefRowCount(4);
        descripcionArea.setWrapText(true);

        // ListView for tareas with custom cell factory and styling
// ListView for tareas with custom cell factory and styling
// ListView for tareas with custom cell factory and styling
// ListView for tareas with custom cell factory and styling
        tareasListView = new ListView<>();
        tareasListView.getStyleClass().add("vpd-tareas-list");
        tareasListView.setPrefHeight(220);

// Set fixed cell size to -1 for variable height
        tareasListView.setFixedCellSize(-1);

        tareasListView.setCellFactory(listView -> new ListCell<TareaObject>() {

            private VBox content;

            {
                // Initialization block instead of constructor
                setPrefHeight(Region.USE_COMPUTED_SIZE);
                setMinHeight(Region.USE_PREF_SIZE);
                setMaxHeight(Double.MAX_VALUE);
            }

            @Override
            protected void updateItem(TareaObject tarea, boolean empty) {
                super.updateItem(tarea, empty);

                if (empty || tarea == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(null);

                    // Create VBox for better layout
                    content = new VBox(8);
                    content.getStyleClass().add("vpd-tarea-content");
                    content.setPrefWidth(Region.USE_COMPUTED_SIZE);
                    content.setMaxWidth(Region.USE_PREF_SIZE);

                    // Title label (bold)
                    Label titleLabel = new Label(tarea.getTitle());
                    titleLabel.getStyleClass().add("vpd-tarea-title");
                    titleLabel.setWrapText(true);

                    content.getChildren().add(titleLabel);

                    // Description label (if exists)
                    if (tarea.getDesc() != null && !tarea.getDesc().trim().isEmpty()) {
                        Label descLabel = new Label(tarea.getDesc());
                        descLabel.getStyleClass().add("vpd-tarea-desc");
                        descLabel.setWrapText(true);
                        descLabel.setMaxWidth(340);
                        // Make sure the label can expand vertically
                        descLabel.setPrefHeight(Region.USE_COMPUTED_SIZE);
                        descLabel.setMinHeight(Region.USE_PREF_SIZE);
                        content.getChildren().add(descLabel);
                    }

                    setGraphic(content);
                }

                // Force layout update
                requestLayout();
            }

            @Override
            protected double computePrefHeight(double width) {
                if (getGraphic() == null) {
                    return super.computePrefHeight(width);
                }

                getGraphic().autosize();
                return getGraphic().prefHeight(width) + getPadding().getTop() + getPadding().getBottom();
            }
        });
    }

    private void loadProjectData() {
        if (proyectoObject != null) {
            nameField.setText(proyectoObject.getProycto_name());
            fechaField.setText(proyectoObject.getFecha_de_entrega());
            descripcionArea.setText(proyectoObject.getDescripcion());

            // Load tareas into ListView
            tareasListView.getItems().clear();
            if (proyectoObject.getTareas_proyecto() != null) {
                tareasListView.getItems().addAll(proyectoObject.getTareas_proyecto());
            }
        }
    }

    @Override
    protected GridPane createContent() {
        initializeComponents();

        mainGrid = new GridPane();
        mainGrid.getStyleClass().add("vpd-grid-pane");

        int row = 0;

        // Project Name
        Label nameLabel = new Label("Nombre del Proyecto:");
        nameLabel.getStyleClass().add("vpd-main-label");
        mainGrid.add(nameLabel, 0, row);
        mainGrid.add(nameField, 1, row++);

        // Fecha de Entrega
        Label fechaLabel = new Label("Fecha de Entrega:");
        fechaLabel.getStyleClass().add("vpd-main-label");
        mainGrid.add(fechaLabel, 0, row);
        mainGrid.add(fechaField, 1, row++);

        // Descripción
        Label descripcionLabel = new Label("Descripción:");
        descripcionLabel.getStyleClass().add("vpd-main-label");
        mainGrid.add(descripcionLabel, 0, row);
        mainGrid.add(descripcionArea, 1, row++);

        // Add spacing before tareas section
        mainGrid.add(new Label(""), 0, row++); // Spacer

        // Tareas Section
        Label tareasLabel = new Label("📋 Tareas del Proyecto");
        tareasLabel.getStyleClass().add("vpd-section-label");
        mainGrid.add(tareasLabel, 0, row, 2, 1); // Span 2 columns
        row++;

        // ScrollPane for tareas with custom styling
        ScrollPane tareasScrollPane = new ScrollPane(tareasListView);
        tareasScrollPane.getStyleClass().add("vpd-scroll-pane");
        tareasScrollPane.setFitToWidth(true);
        tareasScrollPane.setPrefHeight(220);

        // Container for the scroll pane with styling
        VBox tareasContainer = new VBox(tareasScrollPane);
        tareasContainer.getStyleClass().add("vpd-tareas-container");
        tareasContainer.setPadding(new Insets(5));

        mainGrid.add(tareasContainer, 0, row, 2, 1); // Span 2 columns

        // Set column constraints for better layout
        mainGrid.getColumnConstraints().clear();
        javafx.scene.layout.ColumnConstraints col1 = new javafx.scene.layout.ColumnConstraints();
        col1.setMinWidth(180);
        col1.setPrefWidth(180);
        col1.setMaxWidth(180);

        javafx.scene.layout.ColumnConstraints col2 = new javafx.scene.layout.ColumnConstraints();
        col2.setMinWidth(400);
        col2.setPrefWidth(450);

        mainGrid.getColumnConstraints().addAll(col1, col2);

        // Apply root styling to the main grid
        mainGrid.getStyleClass().add("vpd-root");

        return mainGrid;
    }

    @Override
    protected boolean validateInput() {
        return true; // No validation needed for view-only dialog
    }

    @Override
    protected Object getResult() {
        return null; // No result to return
    }

    @Override
    protected void clearValues() {
        // No need to clear since it's view-only
    }
}
