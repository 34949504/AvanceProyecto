/*
Funciones de utilidad
 */
package org.example.avanceproyecto.ControllerUtils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.example.avanceproyecto.Controllers.MainController;
import org.example.avanceproyecto.Controllers.SharedStates;
import org.example.avanceproyecto.Controllers.TaskDoer;
import org.example.avanceproyecto.LinkedList.LinkedlistFuncs;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javafx.stage.Screen;
import java.time.LocalDate; // import the LocalDate class

public class Utils {
    private static final Random random = new Random();

    /*
    Reads data from a read-only files from the resources folders
     */
    public static JSONObject readJson_READONLY(String filename) {
        InputStream inputStream = Utils.class.getResourceAsStream(filename);
        if (inputStream == null) {
            throw new RuntimeException(String.format("filename %s does not exist", filename));
        }
        byte[] bytes = null;
        try {
            bytes = inputStream.readAllBytes();
        } catch (IOException e) {
            System.out.println();
            throw new RuntimeException(e);
        }
        String jeson = new String(bytes, StandardCharsets.UTF_8);
        JSONObject jsonObject = new JSONObject(jeson);
        return jsonObject;
    }
    public static JSONObject readJsonAbs(String filename) {
         String filePath = filename; // Replace with your file path
        StringBuilder stringBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return new JSONObject(stringBuilder.toString());
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return null;
        }
    }

    public static Rectangle2D getScreenDimsHalfed() {
        Rectangle2D rectangle2D = Screen.getPrimary().getVisualBounds();
        double width = rectangle2D.getWidth() / 2;
        double height = rectangle2D.getHeight() / 2;
        return new Rectangle2D(width / 2, height / 2, width, height);
    }

    public static void callObserver_show_layout(ArrayList<Observer> observers, BorderPane centerPane, Class<?> clazz) {
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

    public static TaskDoer createTaskDoer(LinkedlistFuncs linkedlistFuncs, ArrayList<Observer> observers, SharedStates sharedStates) {
        TaskDoer taskDoer = new TaskDoer(linkedlistFuncs,observers,sharedStates);
        Thread thread = new Thread(taskDoer);  // wrap the task in a thread
        thread.setDaemon(true);                 // optional, allows app to exit if thread is running
        thread.start();
        return  taskDoer;
    }

    public static int getRandomIntFromList(JSONArray jsonArray) {
        if (jsonArray == null || jsonArray.length() == 0) {
            throw new IllegalArgumentException("JSONArray is null or empty");
        }
        int index = random.nextInt(jsonArray.length()); // pick a random index
        return jsonArray.getInt(index); // return the value at that index
    }

    public static String getTodaysDate() {
        LocalDate myObj = LocalDate.now(); // Create a date object
        System.out.println(myObj); // Display the current date
        return myObj.toString();
    }

    public static void writeJson(String jsonObject, String ... filepath) {
        String currentDirectory = System.getProperty("user.dir");
        Path path = Paths.get(currentDirectory,filepath);
        String fileName =  path.toString();
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            writer.write(jsonObject);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static String getDepartamentoById(JSONObject departamentos_id,Integer id) {

        Iterator<String> keys = departamentos_id.keys();

        while (keys.hasNext()) {
            String next = keys.next();
            Integer cur_id = departamentos_id.getInt(next);

            if (cur_id == id) {
                return next;
            }

        }
        return null;

    }
    public static String readFile(String ... path ) {
        String currentDirectory = System.getProperty("user.dir");
        Path filePath = Paths.get(currentDirectory,path);
        String content = null;
        try {
            content = Files.readString(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return content;

    }

    public static <T,G>  TableColumn<T,G> createColumn(String tablaNombre,String referencia_value_class,int width) {
        TableColumn<T,G> columna = new TableColumn<>(tablaNombre);
        columna.setCellValueFactory(new PropertyValueFactory<>(referencia_value_class));
        columna.setPrefWidth(width);
        return columna;
    }


}
