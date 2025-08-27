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
import org.example.avanceproyecto.Tarea.TareaNodo;
import org.example.avanceproyecto.Tarea.TipoTarea;

import java.util.ArrayList;
import java.util.Arrays;

public class VerTareas extends BaseController implements Observer {

    private ObservableList<TareaNodo> data = FXCollections.observableArrayList();
    private TipoTarea current_tipoTarea_state = TipoTarea.Urgente;

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
    private TableView<TareaNodo> table;



    public VerTareas(String fxmlFile) {
        initilize_fxml(fxmlFile);

    }

    @FXML
    public void initialize() {
        Utils.set_action_regresar_main_menu(regresar,getObservers());

        urgentes_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                titulo_nombre_tarea_label.setText(urgentes_button.getText());
                changedaTable(TipoTarea.Urgente);
            }
        });
        no_urgentes_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                titulo_nombre_tarea_label.setText(no_urgentes_button.getText());
                changedaTable(TipoTarea.No_Urgente);
            }
        });

        lista_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                titulo_nombre_tarea_label.setText(lista_button.getText());
                changedaTable(TipoTarea.Lista);
            }
        });
        createTable();
    }

    private void createTable() {
        // Create columns
        TableColumn<TareaNodo, String> departamentoCol = new TableColumn<>("Departamento");
        departamentoCol.setCellValueFactory(new PropertyValueFactory<>("departamento"));
        departamentoCol.setPrefWidth(150);

        TableColumn<TareaNodo, String> tareaCol = new TableColumn<>("Tarea");
        tareaCol.setCellValueFactory(new PropertyValueFactory<>("nombreTarea")); // Use camelCase
        tareaCol.setPrefWidth(150);

        TableColumn<TareaNodo, Integer> milisecondsCol = new TableColumn<>("Milisegundos");
        milisecondsCol.setCellValueFactory(new PropertyValueFactory<>("milisegundos"));
        milisecondsCol.setPrefWidth(150);

        table.getColumns().addAll(departamentoCol,tareaCol,milisecondsCol);

        // Set table width to match columns
        table.setPrefWidth(450);
        table.setMaxWidth(450);

        table.setEditable(false);
        table.setItems(this.data);

    }

    private void changedaTable(TipoTarea tipoTarea) {
        this.current_tipoTarea_state = tipoTarea;
        ArrayList<TareaNodo> tareaNodoArrayList = new ArrayList<>();

        for (Observer observer : getObservers()) {
            if (observer instanceof AgregarTarea agregarTarea){

            ArrayList<TareaNodo> result = observer.get_node_tarea_array(tipoTarea);
            if (result != null) {
                tareaNodoArrayList = result;
                System.out.println(tareaNodoArrayList.size());
                break;
            }
            }
        }
        for (TareaNodo tareaNodo:tareaNodoArrayList) {
            System.out.println(tareaNodo.getValues());
        }
        data.clear();
        data.addAll(tareaNodoArrayList);
    }

    @Override
    public void updateTable(TipoTarea tipoTarea) {

        System.out.println("Updating?");
        if (this.current_tipoTarea_state == tipoTarea) {
            changedaTable(tipoTarea);
        }

    }
}


