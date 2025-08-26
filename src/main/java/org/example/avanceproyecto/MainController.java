package org.example.avanceproyecto;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;

@Getter @Setter
public class MainController extends BaseController implements Observer {

    private Stage stage;
    private Parent main_layout;
    private ArrayList<Observer> observers = new ArrayList<>();

    @FXML
    private BorderPane mainPane_borderpane;

    public MainController(String fxmlFile) {
        super(fxmlFile);
        this.mainPane_borderpane = (((BorderPane)getLayout()));
        Parent center = ((Parent) ((BorderPane)getLayout()).getCenter());
        main_layout = center;
        //Change the layout to the center and not the whole borderpane

    }




    @FXML
    private void displayAgregarTarea() throws IOException {
        for (Observer observer: observers) {
            observer.show_agregarTarea( mainPane_borderpane);
        }

    }

    @Override
    public void show_mainlayout() {
        mainPane_borderpane.setCenter(getMain_layout());
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

}
