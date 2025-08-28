package org.example.avanceproyecto.ControllerUtils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.example.avanceproyecto.Controllers.AgregarTarea;
import org.example.avanceproyecto.Controllers.MainController;
import org.example.avanceproyecto.Controllers.TaskDoer;
import org.example.avanceproyecto.LinkedList.LinkedlistFuncs;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javafx.stage.Screen;

public class Utils {

    public static JSONObject readJson(String filename) {
        InputStream inputStream = Utils.class.getResourceAsStream(filename);
        if (inputStream == null) {
            throw new RuntimeException(String.format("filename %s does not exist", filename));
        }
        byte[] bytes = null;
        try {
            bytes = inputStream.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String jeson = new String(bytes, StandardCharsets.UTF_8);
        JSONObject jsonObject = new JSONObject(jeson);
        return jsonObject;
    }

    public static Rectangle2D getScreenDimsHalfed() {
        Rectangle2D rectangle2D = Screen.getPrimary().getVisualBounds();
        double width = rectangle2D.getWidth() / 2;
        double height = rectangle2D.getHeight() / 2;
        return new Rectangle2D(width / 2, height / 2, width, height);
    }

    public static void callObserver(ArrayList<Observer> observers, BorderPane centerPane, Class<?> clazz) {
        for (Observer observer : observers) {
            boolean sucess = observer.show_layout(clazz);
            if (sucess) break;
        }
    }

    public static void set_action_regresar_main_menu(Button regresar, ArrayList<Observer> observers) {
        regresar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                for (Observer observer : observers) {
                    boolean sucess = observer.show_layout(MainController.class);
                    if (sucess) break;
                }
            }
        });
    }

    public static Alert get_alert_position_centered(Stage big_window, Alert.AlertType type, String title,String header, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(msg);
        alert.getDialogPane().setStyle("-fx-font-family: 'Arial'; -fx-font-size: 25px;");
        alert.initOwner(big_window);

        alert.setOnShown(e -> {
            // Dimensions of parent
            double parentX = big_window.getX();
            double parentY = big_window.getY();
            double parentWidth = big_window.getWidth();
            double parentHeight = big_window.getHeight();

            // Dimensions of alert
            Window alertWindow = alert.getDialogPane().getScene().getWindow();
            double alertWidth = alertWindow.getWidth();
            double alertHeight = alertWindow.getHeight();

            // Center position
            double centerX = parentX + (parentWidth - alertWidth) / 2;
            double centerY = parentY + (parentHeight - alertHeight) / 2;

            alertWindow.setX(centerX);
            alertWindow.setY(centerY);
        });
        return alert;
    }

    public static void createTaskDoer(LinkedlistFuncs linkedlistFuncs,ArrayList<Observer> observers) {
        TaskDoer taskDoer = new TaskDoer(linkedlistFuncs,observers);
        Thread thread = new Thread(taskDoer);  // wrap the task in a thread
        thread.setDaemon(true);                 // optional, allows app to exit if thread is running
        thread.start();
    }


}
