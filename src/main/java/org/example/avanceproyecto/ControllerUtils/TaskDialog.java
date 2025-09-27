//package org.example.avanceproyecto.ControllerUtils;
//
//import javafx.application.Platform;
//import javafx.event.EventHandler;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.input.KeyCode;
//import javafx.scene.layout.GridPane;
//import javafx.scene.layout.HBox;
//import javafx.stage.Modality;
//import javafx.stage.Stage;
//import lombok.Setter;
//
//public class TaskDialog {
//    private Stage dialogStage;
//    private TextField titleField;
//    private TextArea descriptionArea;
//    private Button okButton;
//    private Button cancelButton;
//    private boolean okClicked = false;
//
//    // Result fields
//    private String title;
//    private String description;
//    Stage  parentstage;
//
//    public TaskDialog(Stage parentStage) {
//        createDialog(parentStage);
//    }
//
//    private void createDialog(Stage parentStage) {
//        this.parentstage = parentStage;
//        dialogStage = new Stage();
//        dialogStage.setTitle("Create Task");
//        dialogStage.initModality(Modality.WINDOW_MODAL);
//        dialogStage.initOwner(parentStage);
//        dialogStage.setResizable(false);
//
//        // Create layout
//        GridPane grid = new GridPane();
//        grid.setHgap(10);
//        grid.setVgap(10);
//        grid.setPadding(new Insets(20));
//
//        // Title row
//        Label titleLabel = new Label("Title:");
//        titleField = new TextField();
//        titleField.setPrefWidth(300);
//        grid.add(titleLabel, 0, 0);
//        grid.add(titleField, 1, 0);
//
//        // Description row
//        Label descLabel = new Label("Description:");
//        descriptionArea = new TextArea();
//        descriptionArea.setPrefRowCount(4);
//        descriptionArea.setPrefWidth(300);
//        descriptionArea.setWrapText(true);
//        grid.add(descLabel, 0, 1);
//        grid.add(descriptionArea, 1, 1);
//
//        // Button row
//        HBox buttonBox = new HBox(10);
//        buttonBox.setAlignment(Pos.CENTER_RIGHT);
//
//        okButton = new Button("OK");
//        cancelButton = new Button("Cancel");
//
//        buttonBox.getChildren().addAll(cancelButton, okButton);
//        grid.add(buttonBox, 1, 2);
//
//        // Button actions
//        okButton.setOnAction(e -> {
//            if (isInputValid()) {
//                okClicked = true;
//                title = titleField.getText();
//                description = descriptionArea.getText();
//                dialogStage.close();
//            }
//        });
//
//        cancelButton.setOnAction(e -> {
//            okClicked = false;
//            dialogStage.close();
//        });
//
//        // Enter key submits
//        titleField.setOnAction(e -> okButton.fire());
//
//        // Escape key cancels
//        Scene scene = new Scene(grid);
//        scene.setOnKeyPressed(e -> {
//            if (e.getCode() == KeyCode.ESCAPE) {
//                cancelButton.fire();
//            }
//        });
//        dialogStage.setOnShown(e -> {
//            double centerXPosition = parentStage.getX() + (parentStage.getWidth() / 2) - (dialogStage.getWidth() / 2);
//            double centerYPosition = parentStage.getY() + (parentStage.getHeight() / 2) - (dialogStage.getHeight() / 2);
//            dialogStage.setX(centerXPosition);
//            dialogStage.setY(centerYPosition);
//        });
//
//        dialogStage.setScene(scene);
//    }
//
//    private void createEmpleadosCellDialog(Stage parentStage) {
//        ChoiceBox<String> departamento_choicebox = new ChoiceBox<>();
//        ChoiceBox<Empleado> empleado_choicebox = new ChoiceBox<>();
//
//        empleado_choicebox.setDisable(true);
//
//
//        this.parentstage = parentStage;
//        dialogStage = new Stage();
//        dialogStage.setTitle("Create Task");
//        dialogStage.initModality(Modality.WINDOW_MODAL);
//        dialogStage.initOwner(parentStage);
//        dialogStage.setResizable(false);
//
//        // Create layout
//        GridPane grid = new GridPane();
//        grid.setHgap(10);
//        grid.setVgap(10);
//        grid.setPadding(new Insets(20));
//
//        // Title row
//        Label titleLabel = new Label("Departamento:");
//        grid.add(titleLabel, 0, 0);
//        grid.add(departamento_choicebox, 1, 0);
//
//        // Description row
//        Label descLabel = new Label("Empleado:");
//        grid.add(descLabel, 0, 1);
//        grid.add(empleado_choicebox, 1, 1);
//
//        HBox buttonBox = new HBox(10);
//        buttonBox.setAlignment(Pos.CENTER_RIGHT);
//
//        okButton = new Button("OK");
//        cancelButton = new Button("Cancel");
//
//        buttonBox.getChildren().addAll(cancelButton, okButton);
//        grid.add(buttonBox, 1, 2);
//
//        // Button actions
//        okButton.setOnAction(e -> {
//            if (isInputValid()) {
//                okClicked = true;
//                title = titleField.getText();
//                description = descriptionArea.getText();
//                dialogStage.close();
//            }
//        });
//
//        cancelButton.setOnAction(e -> {
//            okClicked = false;
//            dialogStage.close();
//        });
//
//        // Enter key submits
//        titleField.setOnAction(e -> okButton.fire());
//
//        // Escape key cancels
//        Scene scene = new Scene(grid);
//        scene.setOnKeyPressed(e -> {
//            if (e.getCode() == KeyCode.ESCAPE) {
//                cancelButton.fire();
//            }
//        });
//        dialogStage.setOnShown(e -> {
//            double centerXPosition = parentStage.getX() + (parentStage.getWidth() / 2) - (dialogStage.getWidth() / 2);
//            double centerYPosition = parentStage.getY() + (parentStage.getHeight() / 2) - (dialogStage.getHeight() / 2);
//            dialogStage.setX(centerXPosition);
//            dialogStage.setY(centerYPosition);
//        });
//
//        dialogStage.setScene(scene);
//    }
//
//
//    private boolean isInputValid() {
//        String errorMessage = "";
//
//        if (titleField.getText() == null || titleField.getText().trim().isEmpty()) {
//            errorMessage += "Title is required!\n";
//        }
//
//        if (errorMessage.isEmpty()) {
//            return true;
//        } else {
//            // Show error dialog
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Invalid Input");
//            alert.setHeaderText("Please correct the following:");
//            alert.setContentText(errorMessage);
//
//            alert.setOnShown(e -> {
//                // Force the dialog to calculate its size
//                alert.getDialogPane().applyCss();
//                alert.getDialogPane().autosize();
//
//                double centerXPosition = parentstage.getX() + (parentstage.getWidth() / 2) - (alert.getWidth() / 2);
//                double centerYPosition = parentstage.getY() + (parentstage.getHeight() / 2) - (alert.getHeight() / 2);
//                alert.setX(centerXPosition);
//                alert.setY(centerYPosition);
//            });
//
//            alert.showAndWait();
//            return false;
//        }
//    }
//
//    public boolean showDialog(boolean clear) {
//        // Clear previous values
//        if (clear) {
//            titleField.clear();
//            descriptionArea.clear();
//        }
//        okClicked = false;
//        // Focus on title field
//        Platform.runLater(() -> titleField.requestFocus());
//        dialogStage.showAndWait();
//        return okClicked;
//    }
//
//    public boolean showDialog2(Empleado empleado) {
//        okClicked = false;
//        dialogStage.showAndWait();
//        return okClicked;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setTitle(String title) {
//        titleField.setText(title);
//    }
//
//    public void setDescription(String description) {
//        descriptionArea.setText(description);
//    }
//}