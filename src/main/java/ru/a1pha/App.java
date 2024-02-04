package ru.a1pha;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        stage.getIcons().add(new Image(App.class.getResourceAsStream("/Icons/calculator.png")));
        stage.setTitle("Calculator 3000");
        scene = new Scene(loadFXML("MainController"));
        stage.setScene(scene);
        stage.setMinWidth(335);
        stage.setMinHeight(600);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.ROOT);
        launch();
    }
}