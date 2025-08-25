package org.example.avanceproyecto;

import javafx.geometry.Rectangle2D;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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
}
