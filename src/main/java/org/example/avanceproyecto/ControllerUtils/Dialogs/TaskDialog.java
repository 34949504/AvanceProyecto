package org.example.avanceproyecto.ControllerUtils.Dialogs;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Setter;


/**
 * Dialogo para agregar tareas al creador de proyectos
 * Titulo de tarea y descripcion
 */
public class TaskDialog extends BaseDialog<TaskDialog.TaskResult> {
    private TextField titleField;
    private TextArea descriptionArea;
    
    public TaskDialog(Stage parentStage) {
        super(parentStage, "Create Task");
    }
    
    @Override
    protected GridPane createContent() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        // Title row
        Label titleLabel = new Label("Title:");
        titleField = new TextField();
        titleField.setPrefWidth(300);
        grid.add(titleLabel, 0, 0);
        grid.add(titleField, 1, 0);
        
        // Description row
        Label descLabel = new Label("Description:");
        descriptionArea = new TextArea();
        descriptionArea.setPrefRowCount(4);
        descriptionArea.setPrefWidth(300);
        descriptionArea.setWrapText(true);
        grid.add(descLabel, 0, 1);
        grid.add(descriptionArea, 1, 1);
        
        // Enter key on title field submits form
        titleField.setOnAction(e -> okButton.fire());
        
        // Focus on title field when dialog opens
        Platform.runLater(() -> titleField.requestFocus());
        
        return grid;
    }
    
    @Override
    protected boolean validateInput() {
        if (titleField.getText() == null || titleField.getText().trim().isEmpty()) {
            showError("Title is required!");
            return false;
        }
        return true;
    }
    
    @Override
    protected TaskResult getResult() {
        return new TaskResult(titleField.getText().trim(), descriptionArea.getText().trim());
    }

    @Override
    protected void clearValues() {
        titleField.clear();
        descriptionArea.clear();
    }

    // Methods for pre-filling the dialog
    public void setTitle(String title) {
        titleField.setText(title);
    }
    
    public void setDescription(String description) {
        descriptionArea.setText(description);
    }
    
    public void clear() {
        titleField.clear();
        descriptionArea.clear();
    }

    public static class TaskResult {
        private final String title;
        private final String description;

        public TaskResult(String title, String description) {
            this.title = title;
            this.description = description;
        }

        public String getTitle() { return title; }
        public String getDescription() { return description; }
    }
}

