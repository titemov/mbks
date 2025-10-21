import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.shape.Line;

public class Interface extends Application {

    @Override
    public void start(Stage stage){

        Group group = new Group();

        Scene scene = new Scene(group, Color.LIGHTGRAY);
        stage.setScene(scene);
        stage.setTitle("Employee");
        stage.setWidth(800);
        stage.setHeight(600);
        stage.setResizable(false);
        stage.show();
    }

    public static void show(){
        Application.launch();
    }
}
