package org.example.avanceproyecto.ControllerUtils.Dialogs;

import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.avanceproyecto.Controllers.Proyecto.objects.EmpleadoTarea;
import org.example.avanceproyecto.Controllers.Proyecto.objects.ProyectoObjectAsignado;

/**
 * Dialog que temuestra informacion sobre un proyecto asignado
 * Proyecto de datos, empleados con sus responsabilidadess
 */
public class VerProyectosAsignadosDialog extends BaseDialog {

    private ProyectoObjectAsignado proyectoObject; // Changed to ProyectoObjectAsignado
    private TextField nameField;
    private TextField fechaField;
    private TextArea descripcionArea;
    private ListView<EmpleadoTarea> tareasListView;
    private GridPane mainGrid;

    public VerProyectosAsignadosDialog(Stage parentStage, String title, Rectangle2D dialog_dims) {
        super(parentStage, title, dialog_dims);
    }

    // Simplified method - now only needs the ProyectoObjectAsignado
    public void setProyectoData(ProyectoObjectAsignado proyecto) {
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

        // ListView for EmpleadoTarea with custom cell factory
        tareasListView = new ListView<>();
        tareasListView.getStyleClass().add("vpd-tareas-list");
        tareasListView.setPrefHeight(300);

        // Set fixed cell size to -1 for variable height
        tareasListView.setFixedCellSize(-1);

        tareasListView.setCellFactory(listView -> new ListCell<EmpleadoTarea>() {

            private VBox content;

            {
                // Initialization block
                setPrefHeight(Region.USE_COMPUTED_SIZE);
                setMinHeight(Region.USE_PREF_SIZE);
                setMaxHeight(Double.MAX_VALUE);
            }

            @Override
            protected void updateItem(EmpleadoTarea empleadoTarea, boolean empty) {
                super.updateItem(empleadoTarea, empty);

                if (empty || empleadoTarea == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(null);

                    // Main container
                    content = new VBox(12);
                    content.getStyleClass().add("vpd-empleado-tarea-content");
                    content.setPrefWidth(Region.USE_COMPUTED_SIZE);
                    content.setMaxWidth(Region.USE_PREF_SIZE);
                    content.setPadding(new Insets(12));

                    // Task Section Header
                    Label taskHeader = new Label("📋 Tarea Asignada");
                    taskHeader.getStyleClass().add("vpd-section-header");
                    content.getChildren().add(taskHeader);

                    // Task info container
                    VBox taskInfo = new VBox(6);
                    taskInfo.getStyleClass().add("vpd-task-info");
                    taskInfo.setPadding(new Insets(8, 0, 8, 16));

                    // Task title
                    Label taskTitleLabel = new Label("Tarea:");
                    taskTitleLabel.getStyleClass().add("vpd-field-label");
                    Label taskTitle = new Label(empleadoTarea.getTareaObject().getTitle());
                    taskTitle.getStyleClass().add("vpd-task-title");
                    taskTitle.setWrapText(true);

                    taskInfo.getChildren().addAll(taskTitleLabel, taskTitle);

                    // Task description (if exists)
                    if (empleadoTarea.getTareaObject().getDesc() != null &&
                            !empleadoTarea.getTareaObject().getDesc().trim().isEmpty()) {

                        Label taskDescLabel = new Label("Descripción:");
                        taskDescLabel.getStyleClass().add("vpd-field-label");
                        Label taskDesc = new Label(empleadoTarea.getTareaObject().getDesc());
                        taskDesc.getStyleClass().add("vpd-task-desc");
                        taskDesc.setWrapText(true);
                        taskDesc.setMaxWidth(400);

                        taskInfo.getChildren().addAll(taskDescLabel, taskDesc);
                    }

                    content.getChildren().add(taskInfo);

                    // Separator line
                    Separator separator = new Separator();
                    separator.getStyleClass().add("vpd-separator");
                    content.getChildren().add(separator);

                    // Employee Section Header
                    Label employeeHeader = new Label("👤 Empleado Asignado");
                    employeeHeader.getStyleClass().add("vpd-section-header");
                    content.getChildren().add(employeeHeader);

                    // Employee info container
                    VBox employeeInfo = new VBox(6);
                    employeeInfo.getStyleClass().add("vpd-employee-info");
                    employeeInfo.setPadding(new Insets(8, 0, 8, 16));

                    // Employee name
                    Label empNameLabel = new Label("Empleado Asignado:");
                    empNameLabel.getStyleClass().add("vpd-field-label");
                    Label empName = new Label(empleadoTarea.getEmpleado().getFullName());
                    empName.getStyleClass().add("vpd-employee-name");

                    // Department
                    Label deptLabel = new Label("Departamento:");
                    deptLabel.getStyleClass().add("vpd-field-label");
                    Label deptInfo = new Label(String.format("%s (ID: %d)",
                            empleadoTarea.getEmpleado().getDepartamentoNombre(),
                            empleadoTarea.getEmpleado().getDepartamentoId()));
                    deptInfo.getStyleClass().add("vpd-department-info");


                    // Add status-specific styling

                    employeeInfo.getChildren().addAll(
                            empNameLabel, empName,
                            deptLabel, deptInfo
                    );

                    content.getChildren().add(employeeInfo);

                    setGraphic(content);
                }

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

            // Load EmpleadoTarea objects directly from the project
            tareasListView.getItems().clear();
            if (proyectoObject.getEmpleadoTareas() != null && !proyectoObject.getEmpleadoTareas().isEmpty()) {
                tareasListView.getItems().addAll(proyectoObject.getEmpleadoTareas());
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
        Label tareasLabel = new Label("📋 Tareas Asignadas del Proyecto");
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
