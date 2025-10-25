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
import javafx.scene.shape.Line;

import java.util.Objects;

public class Interface extends Application {
    /*
    добавить messagebox на импорт/экспорт
    */
    @Override
    public void start(Stage stage){

        Group mainGroup = new Group();
        Group buttonGroup = new Group();
        final Stage[] secondWindowStage = new Stage[1];//костыль

        Button addSubjectBtn = new Button("Add subject(s)");
        addSubjectBtn.setLayoutX(10);
        addSubjectBtn.setLayoutY(10);
        addSubjectBtn.setPrefSize(120,15);
        addSubjectBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    secondWindowStage[0].close();
                } catch (Exception e) {
                    System.out.println("No secondary window to close (Interface.java)");
                }

                try {
                    Backend a = new Backend();
                    //a.initialParse();
                    secondWindowStage[0] = a.add(0);//mode=0
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
        buttonGroup.getChildren().add(addSubjectBtn);

        Button addObjectBtn = new Button("Add object(s)");
        addObjectBtn.setLayoutX(10);
        addObjectBtn.setLayoutY(10+15+15);
        addObjectBtn.setPrefSize(120,15);
        addObjectBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    secondWindowStage[0].close();
                } catch (Exception e) {
                    System.out.println("No secondary window to close (Interface.java)");
                }

                try {
                    Backend a = new Backend();
                    //a.initialParse();
                    secondWindowStage[0] = a.add(1);//mode=1
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
        buttonGroup.getChildren().add(addObjectBtn);

        Button removeSubjectBtn = new Button("Remove subject");
        removeSubjectBtn.setLayoutX(10+120+10);
        removeSubjectBtn.setLayoutY(10);
        removeSubjectBtn.setPrefSize(120,15);
        removeSubjectBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    secondWindowStage[0].close();
                } catch (Exception e) {
                    System.out.println("No secondary window to close (Interface.java)");
                }

                try {
                    Backend a = new Backend();
                    //a.initialParse();
                    secondWindowStage[0] = a.remove(0);//mode=0
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
        buttonGroup.getChildren().add(removeSubjectBtn);

        Button removeObjectBtn = new Button("Remove object");
        removeObjectBtn.setLayoutX(10+120+10);
        removeObjectBtn.setLayoutY(10+15+15);
        removeObjectBtn.setPrefSize(120,15);
        removeObjectBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    secondWindowStage[0].close();
                } catch (Exception e) {
                    System.out.println("No secondary window to close (Interface.java)");
                }

                try {
                    Backend a = new Backend();
                    //a.initialParse();
                    secondWindowStage[0] = a.remove(1);//mode=1
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
        buttonGroup.getChildren().add(removeObjectBtn);

        Button changeAccessBtn = new Button("Change access");
        changeAccessBtn.setLayoutX(10+120+10+120+10);
        changeAccessBtn.setLayoutY(10);
        changeAccessBtn.setPrefSize(120,15);
        changeAccessBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    secondWindowStage[0].close();
                } catch (Exception e) {
                    System.out.println("No secondary window to close (Interface.java)");
                }

                Backend a = new Backend();
                //a.initialParse();
                secondWindowStage[0] = a.changeAccess();
            }
        });
        buttonGroup.getChildren().add(changeAccessBtn);

        Button showMatrixBtn = new Button("Show Matrix");
        showMatrixBtn.setLayoutX(10+120+10+120+10);
        showMatrixBtn.setLayoutY(10+15+15);
        showMatrixBtn.setPrefSize(120,15);
        showMatrixBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    secondWindowStage[0].close();
                } catch (Exception e) {
                    System.out.println("No secondary window to close (Interface.java)");
                }

                Backend a = new Backend();
                //a.initialParse();
                secondWindowStage[0] = a.showMatrix();
            }
        });
        buttonGroup.getChildren().add(showMatrixBtn);

        TextArea consoleTextArea = new TextArea();
        consoleTextArea.setText(">>>");
        consoleTextArea.setLayoutX(10);
        consoleTextArea.setLayoutY(70);
        consoleTextArea.setMinHeight(400);
        consoleTextArea.setMaxHeight(400);
        consoleTextArea.setMinWidth(650);
        consoleTextArea.setMaxWidth(650);
        consoleTextArea.setWrapText(true);
        //consoleTextArea.requestFocus();
        consoleTextArea.setFont(Font.font("Consolas", 28));
        consoleTextArea.end();
        mainGroup.getChildren().add(consoleTextArea);

        Button enterCommandBtn = new Button("Enter");
        enterCommandBtn.setLayoutX(650+20);
        enterCommandBtn.setLayoutY(70);
        enterCommandBtn.setPrefSize(105,350);
        enterCommandBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Backend a = new Backend();
                String output = a.consoleEnter(consoleTextArea);
                consoleTextArea.setText(consoleTextArea.getText()+"\n"+output+"\n\n>>>");
                consoleTextArea.requestFocus();
                consoleTextArea.end();
            }
        });
        buttonGroup.getChildren().add(enterCommandBtn);

        Button clearCLIBtn = new Button("Clear");
        clearCLIBtn.setLayoutX(650+20);
        clearCLIBtn.setLayoutY(420+5);
        clearCLIBtn.setPrefSize(105,45);
        clearCLIBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                consoleTextArea.setText(">>>");
                consoleTextArea.requestFocus();
                consoleTextArea.end();
            }
        });
        buttonGroup.getChildren().add(clearCLIBtn);

        Button exportMatrixBtn = new Button("Export matrix to file");
        exportMatrixBtn.setLayoutX(10);
        exportMatrixBtn.setLayoutY(500);
        exportMatrixBtn.setPrefSize(150,15);
        exportMatrixBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Backend a = new Backend();
                a.exportMatrix();
            }
        });
        buttonGroup.getChildren().add(exportMatrixBtn);

        Label pathLabel = new Label("Path:");
        pathLabel.setLayoutX(335);
        pathLabel.setLayoutY(505);
        mainGroup.getChildren().add(pathLabel);

        TextField pathTF = new TextField();
        pathTF.setLayoutX(365);
        pathTF.setLayoutY(500);
        pathTF.setPrefSize(200,20);
        pathTF.setPromptText("Enter path here...");
        mainGroup.getChildren().add(pathTF);

        Button importMatrixBtn = new Button("Import matrix from file");
        importMatrixBtn.setLayoutX(175);
        importMatrixBtn.setLayoutY(500);
        importMatrixBtn.setPrefSize(150,15);
        importMatrixBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Backend a = new Backend();
                a.importMatrix(pathTF.getText());
            }
        });
        buttonGroup.getChildren().add(importMatrixBtn);

        Image gifImage = new Image("file:admin2.gif");//or file:doc2.gif
        ImageView imageView = new ImageView(gifImage);
        imageView.setLayoutX(375);
        imageView.setLayoutY(-25);
        mainGroup.getChildren().add(imageView);

        //админская программа после перезапуска подтягивает значения в таблице
        Backend init = new Backend();
        init.parseMatrix("Matrix.txt");
        //на любое действие в программе - сначала получить данные с общей таблицы, а потом что-либо делать
        //то же самое относится и к программе работника
        //зачем парсить в админской программе, если работник ничего не может менять?
        //получается, что данное замечание относится только к программе работника.

        mainGroup.getChildren().add(buttonGroup);
        Scene scene = new Scene(mainGroup, Color.rgb(245,245,245));
        stage.setScene(scene);
        stage.setTitle("Admin");
        stage.setWidth(800);
        stage.setHeight(600);
        stage.setResizable(false);
        stage.show();
    }

    public static void show(){
        Application.launch();
    }
}
