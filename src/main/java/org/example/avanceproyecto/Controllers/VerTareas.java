package org.example.avanceproyecto.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.avanceproyecto.ControllerUtils.BaseController;
import org.example.avanceproyecto.ControllerUtils.Observer;
import org.example.avanceproyecto.ControllerUtils.Utils;

public class VerTareas extends BaseController implements Observer {


    @FXML
    private Button urgentes_button;

    @FXML
    private Button no_urgentes_button;

    @FXML
    private Button lista_button;

    @FXML
    private Label titulo_nombre_tarea_label;


    @FXML
    private Button regresar;

    @FXML
    private TableView<Persona> table;

    public VerTareas(String fxmlFile) {
        super(fxmlFile);
    }



    @FXML
    public void initialize() {
        Utils.set_action_regresar_main_menu(regresar,getObservers());

        urgentes_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                titulo_nombre_tarea_label.setText(urgentes_button.getText());
            }
        });
        no_urgentes_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                titulo_nombre_tarea_label.setText(no_urgentes_button.getText());
            }
        });

        lista_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                titulo_nombre_tarea_label.setText(lista_button.getText());
            }
        });

        createTable();

    }

    private void createTable() {
        // Create columns
        TableColumn<Persona, String> nombreCol = new TableColumn<>("Nombre");
        nombreCol.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        nombreCol.setPrefWidth(150);

        TableColumn<Persona, Integer> edadCol = new TableColumn<>("Edad");
        edadCol.setCellValueFactory(new PropertyValueFactory<>("edad"));
        edadCol.setPrefWidth(150);

        table.getColumns().addAll(nombreCol, edadCol);

        // Set table width to match columns
        table.setPrefWidth(300);
        table.setMaxWidth(300);


        // Create 5 Persona objects
        ObservableList<Persona> data = FXCollections.observableArrayList(
                new Persona(25, "Gerardo"),
                new Persona(30, "Alice"),
                new Persona(22, "Bob"),
                new Persona(28, "Carol"),
                new Persona(35, "David")
        );

        table.setEditable(false);
        // Add to the table
        table.setItems(data);
    }


    @Getter @Setter @AllArgsConstructor
    public class Persona {
        int edad;
        String nombre;
    }



}


