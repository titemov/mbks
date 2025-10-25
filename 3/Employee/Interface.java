import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Interface extends Application {

    @Override
    public void start(Stage stage){

        Group mainGroup = new Group();
        Group textFieldGroup = new Group();
        Group buttonGroup = new Group();

        TextField employeeNameTF = new TextField();
        employeeNameTF.setLayoutX(10);
        employeeNameTF.setLayoutY(10+15);
        employeeNameTF.setPrefSize(120,20);
        employeeNameTF.setPromptText("Enter name here...");
        employeeNameTF.setFocusTraversable(false);
        mainGroup.getChildren().add(employeeNameTF);

        TextField filesTF = new TextField();
        filesTF.setLayoutX(10+120+10);
        filesTF.setLayoutY(10+15);
        filesTF.setPrefSize(120,20);
        filesTF.setPromptText("Enter files here...");
        filesTF.setFocusTraversable(false);
        mainGroup.getChildren().add(filesTF);

        TextArea outputTextArea = new TextArea();
        outputTextArea.setLayoutX(10);
        outputTextArea.setLayoutY(70);
        outputTextArea.setMinHeight(400);
        outputTextArea.setMaxHeight(400);
        outputTextArea.setMinWidth(765);
        outputTextArea.setMaxWidth(765);
        outputTextArea.setWrapText(true);
        outputTextArea.setEditable(false);
        outputTextArea.setFont(Font.font("Consolas", 28));
        outputTextArea.end();
        mainGroup.getChildren().add(outputTextArea);

        try {
            new Thread(new Runnable() {
                public void run() {
                    while(true) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            System.out.println("Thread error "+e);
                        }
                        Backend a = new Backend();
                        a.importMatrix("Matrix.txt");
                        outputTextArea.setText(a.getUserInfo(employeeNameTF.getText(), filesTF.getText()));
                    }
                }
            }).start();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Unknown error occurred.");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
        }


        Image gifImage = new Image("file:user2.gif");//or file:doc2.gif
        ImageView imageView = new ImageView(gifImage);
        imageView.setLayoutX(375);
        imageView.setLayoutY(5);
        mainGroup.getChildren().add(imageView);

        mainGroup.getChildren().addAll(buttonGroup,textFieldGroup);
        Scene scene = new Scene(mainGroup, Color.rgb(245,245,245));
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
