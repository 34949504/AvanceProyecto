package org.example.avanceproyecto.ControllerUtils.Dialogs;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.example.avanceproyecto.Controllers.Proyecto.objects.TareaObject;

import java.util.ArrayList;

public class TareaAsignacionProyectoDialog extends BaseDialog {
    private ChoiceBox<TareaObject> tareaObjectChoiceBox;

    public TareaAsignacionProyectoDialog(Stage parentStage) {
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
        tareaObjectChoiceBox = new ChoiceBox<>();
        tareaObjectChoiceBox.setPrefWidth(300);
        grid.add(titleLabel, 0, 0);
        grid.add(tareaObjectChoiceBox, 1, 0);

        tareaObjectChoiceBox.setConverter(new StringConverter<TareaObject>() {
            @Override
            public String toString(TareaObject tareaObject) {
                if (tareaObject == null) {
                    return "Seleccionar";
                }
                return  tareaObject.getTitle();
            }

            @Override
            public TareaObject fromString(String s) {
                return null;
            }
        });
//        tareaObjectChoiceBox.setOnAction(e -> {
//            PauseTransition pause = new PauseTransition(Duration.millis(500));
//            pause.setOnFinished(ev -> okButton.fire());
//            pause.play();
//        });

        Platform.runLater(() -> tareaObjectChoiceBox.requestFocus());

        return grid;
    }

    @Override
    protected boolean validateInput() {
        if (tareaObjectChoiceBox.getValue() == null) {
            showError("Porfa selecccionar dato");
            return false;
        }
        return true;
    }

    @Override
    protected TareaObject getResult() {
        return  tareaObjectChoiceBox.getValue();
    }

    @Override
    protected void clearValues() {
        tareaObjectChoiceBox.setValue(null);
    }
    public void populate_choicebox(ArrayList<TareaObject> tareaObjects) {
        ObservableList<TareaObject> observableList =
            FXCollections.observableArrayList(tareaObjects);
        tareaObjectChoiceBox.setItems(observableList);
    }
    public void setChoiceBoxValue(TareaObject tareaObject) {
        tareaObjectChoiceBox.setValue(tareaObject);
    }
}
