package org.example.avanceproyecto.ControllerUtils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import org.example.avanceproyecto.Controllers.AgregarTarea;
import org.example.avanceproyecto.Controllers.MainController;
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
        Rectangle2D rectangle2D =Screen.getPrimary().getVisualBounds();
        double width = rectangle2D.getWidth() / 2;
        double height = rectangle2D.getHeight() / 2;
        return new Rectangle2D(width / 2, height / 2, width, height);
    }

    public static void callObserver(ArrayList<Observer> observers, BorderPane centerPane, Class<?>clazz){
        for (Observer observer: observers) {
            boolean sucess = observer.show_layout( clazz);
            if (sucess)break;
        }
    }
    public static void set_action_regresar_main_menu(Button regresar,ArrayList<Observer> observers) {
        regresar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                for (Observer observer: observers) {
                    boolean sucess = observer.show_layout(MainController.class);
                    if (sucess)break;
                }
            }
        });
    }
    

}
