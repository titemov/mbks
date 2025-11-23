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
        final Stage[] secondWindowStage = new Stage[1];

        Button createSecrecyButton = new Button("Create secrecy");
        createSecrecyButton.setLayoutX(10);
        createSecrecyButton.setLayoutY(10);
        createSecrecyButton.setPrefSize(120,20);
        createSecrecyButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    secondWindowStage[0].close();
                } catch (Exception e) {
                    System.out.println("No secondary window to close (Interface.java)");
                }
                try {
                    Backend b = new Backend();
                    secondWindowStage[0] = b.createSecrecy();
                }catch (Exception e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error!");
                    alert.setHeaderText("Error!");
                    alert.setContentText(String.valueOf(e));
                    alert.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {
                            System.out.println("Pressed OK.");
                        }
                    });
                }
            }
        });
        group.getChildren().add(createSecrecyButton);

        Button changeSecrecyButton = new Button("Change secrecy");
        changeSecrecyButton.setLayoutX(10);
        changeSecrecyButton.setLayoutY(10+20+10);
        changeSecrecyButton.setPrefSize(120,20);
        changeSecrecyButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    secondWindowStage[0].close();
                } catch (Exception e) {
                    System.out.println("No secondary window to close (Interface.java)");
                }
                try {
                    Backend b = new Backend();
                    secondWindowStage[0] = b.changeSecrecy();
                }catch (Exception e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error!");
                    alert.setHeaderText("Error!");
                    alert.setContentText(String.valueOf(e));
                    alert.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {
                            System.out.println("Pressed OK.");
                        }
                    });
                }
            }
        });
        group.getChildren().add(changeSecrecyButton);

        Button removeSecrecyButton = new Button("Remove secrecy");
        removeSecrecyButton.setLayoutX(10);
        removeSecrecyButton.setLayoutY(40+20+10);
        removeSecrecyButton.setPrefSize(120,20);
        removeSecrecyButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    secondWindowStage[0].close();
                } catch (Exception e) {
                    System.out.println("No secondary window to close (Interface.java)");
                }
                try {
                    Backend b = new Backend();
                    secondWindowStage[0] = b.removeSecrecy();
                }catch (Exception e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error!");
                    alert.setHeaderText("Error!");
                    alert.setContentText(String.valueOf(e));
                    alert.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {
                            System.out.println("Pressed OK.");
                        }
                    });
                }
            }
        });
        group.getChildren().add(removeSecrecyButton);

        Button createFolderButton = new Button("Create folder");
        createFolderButton.setLayoutX(150);
        createFolderButton.setLayoutY(10);
        createFolderButton.setPrefSize(120,20);
        createFolderButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    secondWindowStage[0].close();
                } catch (Exception e) {
                    System.out.println("No secondary window to close (Interface.java)");
                }
                try {
                    Backend b = new Backend();
                    secondWindowStage[0] = b.createFolder();
                }catch (Exception e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error!");
                    alert.setHeaderText("Error!");
                    alert.setContentText(String.valueOf(e));
                    alert.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {
                            System.out.println("Pressed OK.");
                        }
                    });
                }
            }
        });
        group.getChildren().add(createFolderButton);

        Button changeFolderButton = new Button("Change folder");
        changeFolderButton.setLayoutX(150);
        changeFolderButton.setLayoutY(40);
        changeFolderButton.setPrefSize(120,20);
        changeFolderButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    secondWindowStage[0].close();
                } catch (Exception e) {
                    System.out.println("No secondary window to close (Interface.java)");
                }
                try {
                    Backend b = new Backend();
                    secondWindowStage[0] = b.changeFolder();
                }catch (Exception e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error!");
                    alert.setHeaderText("Error!");
                    alert.setContentText(String.valueOf(e));
                    alert.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {
                            System.out.println("Pressed OK.");
                        }
                    });
                }
            }
        });
        group.getChildren().add(changeFolderButton);

        Button removeFolderButton = new Button("Remove folder");
        removeFolderButton.setLayoutX(150);
        removeFolderButton.setLayoutY(70);
        removeFolderButton.setPrefSize(120,20);
        removeFolderButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    secondWindowStage[0].close();
                } catch (Exception e) {
                    System.out.println("No secondary window to close (Interface.java)");
                }
                try {
                    Backend b = new Backend();
                    secondWindowStage[0] = b.removeFolder();
                }catch (Exception e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error!");
                    alert.setHeaderText("Error!");
                    alert.setContentText(String.valueOf(e));
                    alert.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {
                            System.out.println("Pressed OK.");
                        }
                    });
                }
            }
        });
        group.getChildren().add(removeFolderButton);

        Button changeFolderLevelButton = new Button("Change folder secrecy level");
        changeFolderLevelButton.setLayoutX(290);
        changeFolderLevelButton.setLayoutY(10);
        changeFolderLevelButton.setPrefSize(200,20);
        changeFolderLevelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    secondWindowStage[0].close();
                } catch (Exception e) {
                    System.out.println("No secondary window to close (Interface.java)");
                }
                try {
                    Backend b = new Backend();
                    secondWindowStage[0] = b.changeFolderLevel();
                }catch (Exception e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error!");
                    alert.setHeaderText("Error!");
                    alert.setContentText(String.valueOf(e));
                    alert.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {
                            System.out.println("Pressed OK.");
                        }
                    });
                }
            }
        });
        group.getChildren().add(changeFolderLevelButton);

        Button showInfoButton = new Button("Show info");
        showInfoButton.setLayoutX(10);
        showInfoButton.setLayoutY(100);
        showInfoButton.setPrefSize(480,20);
        showInfoButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    secondWindowStage[0].close();
                } catch (Exception e) {
                    System.out.println("No secondary window to close (Interface.java)");
                }
                try {
                    Backend b = new Backend();
                    secondWindowStage[0] = b.showInfo();
                }catch (Exception e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error!");
                    alert.setHeaderText("Error!");
                    alert.setContentText(String.valueOf(e));
                    alert.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {
                            System.out.println("Pressed OK.");
                        }
                    });
                }
            }
        });
        group.getChildren().add(showInfoButton);

        Button copyContentsButton = new Button("Copy");
        copyContentsButton.setLayoutX(290);
        copyContentsButton.setLayoutY(40);
        copyContentsButton.setPrefSize(200,55);
        copyContentsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    secondWindowStage[0].close();
                } catch (Exception e) {
                    System.out.println("No secondary window to close (Interface.java)");
                }
                try {
                    Backend b = new Backend();
                    secondWindowStage[0] = b.copyContents();
                }catch (Exception e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error!");
                    alert.setHeaderText("Error!");
                    alert.setContentText(String.valueOf(e));
                    alert.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {
                            System.out.println("Pressed OK.");
                        }
                    });
                }
            }
        });
        group.getChildren().add(copyContentsButton);

        Backend init = new Backend();
        init.initialParse();

        Scene scene = new Scene(group, Color.rgb(245,245,245));
        stage.setScene(scene);
        stage.setTitle("MBKS4");
        stage.setWidth(800);
        stage.setHeight(600);
        stage.setResizable(false);
        stage.show();
    }

    public static void show(){
        Application.launch();
    }
}
