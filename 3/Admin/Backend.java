import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.*;

public class Backend extends Interface {
    //private String result="";
    private final Matrix matrix = new Matrix();
    private final FileNames allFileNames = new FileNames();

    private void addButtonHandler(int mode,TextField textField, Stage newStage){
        String text = textField.getText();
        String temp;

        if(!Objects.equals(text,"") && text.length()<256){
            temp = textField.getText();
            newStage.close();
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Empty text field or too much symbols");
            alert.setContentText("Please enter text in the text field or short it down to 255");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
            return;
        }

        if(mode==0) {//mode=0 from subject, mode=1 from object
            String[] splitted = removeDuplicates(temp.split(" "));
            for(int i=0;i<splitted.length;i++){
                this.matrix.addEmployee(new Employee(splitted[i],null));
            }
            //
            String[] abc = this.matrix.getAllNames();
            for(int i=0;i< abc.length;i++){
                System.out.println("Name: "+abc[i]);
            }
        }else{
            String[] splitted = removeDuplicates(temp.split("(?!^)"));
            this.allFileNames.addFiles(splitted);
            //
            String[] abc = allFileNames.getAllFileNames();
            for(int i=0;i< abc.length;i++){
                System.out.println("File: "+abc[i]);
            }
        }
    }

    public Stage add(int mode){
        Stage newStage = new Stage();
        newStage.setWidth(300);
        newStage.setHeight(100);

        Group group = new Group();
        Label label = new Label();
        label.setLayoutX(10);
        label.setLayoutY(10);
        if(mode==0) {//mode=0 from subject, mode=1 from object
            newStage.setTitle("Add subject(s)");
            label.setText("Add subject(s)");
        }else{
            newStage.setTitle("Add object(s)");
            label.setText("Add object(s)");
        }
        group.getChildren().add(label);

        TextField textField = new TextField();
        textField.setLayoutX(10);
        textField.setLayoutY(30);
        textField.setPromptText("Enter here...");
        textField.setPrefSize(190,20);
        textField.setFocusTraversable(false);
        group.getChildren().add(textField);

        Button enterBtn = new Button("Enter");
        enterBtn.setPrefSize(60,15);
        enterBtn.setLayoutX(215);
        enterBtn.setLayoutY(30);
        enterBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addButtonHandler(mode,textField,newStage);
            }
        });

        group.getChildren().add(enterBtn);
        Scene newScene = new Scene(group, Color.rgb(245,245,245));
        newStage.setScene(newScene);
        newStage.setResizable(false);
        newStage.show();

        return newStage;
    }

    public Stage remove(int mode){
        ObservableList<String> observableList = FXCollections.observableArrayList(Arrays.asList((this.matrix).getAllNames()));
        Stage newStage = new Stage();
        newStage.setWidth(300);
        newStage.setHeight(100);

        Group group = new Group();
        Label label = new Label();
        label.setLayoutX(10);
        label.setLayoutY(10);
        if(mode==0) {//mode=0 from subject, mode=1 from object
            newStage.setTitle("Remove subject(s)");
            label.setText("Remove subject(s)");
            //observableList = FXCollections.observableArrayList(Arrays.asList(this.matrix.getAllNames()));
        }else{
            newStage.setTitle("Remove object(s)");
            label.setText("Remove object(s)");
            //observableList = FXCollections.observableArrayList(this.allFileNames.getAllFileNames());
        }
        group.getChildren().add(label);

        System.out.println(this.matrix.matrixLength());
        System.out.println((this.matrix.getAllNames())[0]);

        ChoiceBox<String> chooseCB = new ChoiceBox<>(observableList);
        chooseCB.setLayoutX(10);
        chooseCB.setLayoutY(30);
        chooseCB.minWidth(190);
        chooseCB.maxWidth(190);
        group.getChildren().add(chooseCB);


        Button enterBtn = new Button("Remove");
        enterBtn.setPrefSize(60,15);
        enterBtn.setLayoutX(215);
        enterBtn.setLayoutY(30);
        final String[] temp = {""};//костыль
        enterBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println(chooseCB.getValue());
            }
        });

        group.getChildren().add(enterBtn);
        Scene newScene = new Scene(group, Color.rgb(245,245,245));
        newStage.setScene(newScene);
        newStage.setResizable(false);
        newStage.show();

        return newStage;
    }

    public Matrix getMatrix(){
        return this.matrix;
    }
    public static int rng(int low, int high) {
        Random r = new Random();
        return r.nextInt(high - low) + low;
    }

    public String[] removeDuplicates(String[] array){
        Arrays.sort(array);

        String current = array[0];
        boolean found = false;
        String str="";
        for(int i = 0; i < array.length; i++) {
            if (Objects.equals(current, array[i]) && !found) {
                found = true;
            } else if (!Objects.equals(current, array[i])) {
                str+=current+" ";
                current = array[i];
                found = false;
            }
        }
        str+=current+" ";

        return str.split(" ");
    }

//    public ArrayList removeDuplicates(ArrayList array){
//        Collections.sort(array);
//
//        String current = (String) array.get(0);
//        boolean found = false;
//        String str="";
//        for(int i = 0; i < array.size(); i++) {
//            if (current == array.get(i) && !found) {
//                found = true;
//            } else if (current != array.get(i)) {
//                str+=current+" ";
//                current = (String) array.get(i);
//                found = false;
//            }
//        }
//        str+=current+" ";
//
//        ArrayList<String> result = new ArrayList<>();
//        Collections.addAll(result,str.split(" "));
//        return result;
//    }
}