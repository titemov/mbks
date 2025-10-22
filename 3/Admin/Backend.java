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
    private static final Matrix matrix = new Matrix();
    private static final FileNames allFileNames = new FileNames();

    private void addButtonHandler(int mode,TextField textField, Stage newStage){
        InFileWriter wif = new InFileWriter();
        String text = textField.getText();
        String temp;

        boolean engOnly = text.matches("^[a-zA-Z][a-zA-Z\\s]+$");

        if(!Objects.equals(text,"") && text.length()<256 && engOnly){
            temp = textField.getText();
            newStage.close();
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Empty text field (Probably already exist) or too much symbols");
            alert.setContentText("Please enter unique text containing only english characters in the text field " +
                    "or short it down to 255 characters long");
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
                matrix.addEmployee(new Employee(splitted[i],null));
            }
            //
            wif.writeInFile(matrix);
            String[] abc = matrix.getAllNames();
            for(int i=0;i< abc.length;i++){
                System.out.println("Name: "+abc[i]);
            }
        }else{
            String[] splitted = removeDuplicates(temp.split("(?!^)"));
            allFileNames.addFiles(splitted);
            //
            wif.writeInFile(matrix);
            String[] abc = allFileNames.getAllFileNames();
            for(int i=0;i< abc.length;i++){
                System.out.println("File: "+abc[i]);
            }
        }
    }

    private void removeButtonHandler(ObservableList<String> observableList,int mode, ComboBox choiceCB, Label label){
        InFileWriter wif = new InFileWriter();
        System.out.println(choiceCB.getValue());
        String value = String.valueOf(choiceCB.getValue());
        if(mode==0){//mode=0 - for subjects
            try {
                matrix.removeEmployee(value);
                observableList.remove(value);
                wif.writeInFile(matrix);
            }catch (Exception e){
                System.out.println(e);
            }
            label.setText("Remove subject(s)"+"   "+value+" was removed");
        }else{
            String temp = allFileNames.remove(value);
            if(Objects.isNull(temp)){
                label.setText("Remove object(s)"+"   "+"Nothing was removed");
            }else{
                label.setText("Remove object(s)"+"   "+value+" was removed");
                observableList.remove(value);
            }
            wif.writeInFile(matrix);
        }
    }

    private void changeButtonHandler(TextField subjectTF, TextField objectTF){

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
        ObservableList<String> observableList;
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
            observableList = FXCollections.observableArrayList(matrix.getAllNames());
        }else{
            newStage.setTitle("Remove object(s)");
            label.setText("Remove object(s)");
            observableList = FXCollections.observableArrayList(allFileNames.getAllFileNames());
        }
        group.getChildren().add(label);

//        System.out.println(matrix.matrixLength());
//        System.out.println((matrix.getAllNames())[0]);
//        System.out.println(allFileNames.size());

        ComboBox<String> choiceCB = new ComboBox<>(observableList);
        choiceCB.setLayoutX(10);
        choiceCB.setLayoutY(30);
        choiceCB.setPrefWidth(190);
        choiceCB.setVisibleRowCount(5);
        group.getChildren().add(choiceCB);


        Button enterBtn = new Button("Remove");
        enterBtn.setPrefSize(60,15);
        enterBtn.setLayoutX(215);
        enterBtn.setLayoutY(30);
        //после выбора и нажатия "Remove" не закрывать окно, а просто указать, что именно было удалено.
        // Почему нельзя удалять несколько сразу? потому что нет функции возврата (для предостережения в общем говоря)
        enterBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                removeButtonHandler(observableList,mode,choiceCB,label);
            }
        });

        group.getChildren().add(enterBtn);
        Scene newScene = new Scene(group, Color.rgb(245,245,245));
        newStage.setScene(newScene);
        newStage.setResizable(false);
        newStage.show();

        return newStage;
    }

    public Stage showMatrix(){
        Stage newStage = new Stage();
        newStage.setWidth(300);
        newStage.setHeight(100);

        Group group = new Group();
        newStage.setTitle("Matrix");

        // матрица - это gridpane внутри scrollpane.
        // В каждой ячейке сделать комбобокс(none,+,-) соответствующие доступам

        Scene newScene = new Scene(group, Color.rgb(245,245,245));
        newStage.setScene(newScene);
        newStage.setResizable(false);
        newStage.show();
        return newStage;
    }

    public Stage changeAccess(){
        // два текстовых поля: имена и объекты, а также комбобокс, содержащий (grant,take) режимы
        // программа меняет доступы для всех существующих перечисленных имен.
        // Если имени не существует (или файла) - игнор
        Stage newStage = new Stage();
        newStage.setWidth(300);
        newStage.setHeight(100);

        Group group = new Group();
        newStage.setTitle("ChangeAccess");

        Label label = new Label("Change Access:");
        label.setLayoutX(10);
        label.setLayoutY(10);
        group.getChildren().add(label);

        TextField subjectTF = new TextField();
        subjectTF.setLayoutX(10);
        subjectTF.setLayoutY(30);
        subjectTF.setPromptText("Subject...");
        subjectTF.setPrefSize(90,20);
        subjectTF.setFocusTraversable(false);
        group.getChildren().add(subjectTF);

        TextField objectTF = new TextField();
        objectTF.setLayoutX(110);
        objectTF.setLayoutY(30);
        objectTF.setPromptText("Object...");
        objectTF.setPrefSize(90,20);
        objectTF.setFocusTraversable(false);
        group.getChildren().add(objectTF);


        Button enterBtn = new Button("Change");
        enterBtn.setPrefSize(60,15);
        enterBtn.setLayoutX(215);
        enterBtn.setLayoutY(30);
        enterBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                changeButtonHandler(subjectTF,objectTF);
            }
        });
        group.getChildren().add(enterBtn);

        Scene newScene = new Scene(group, Color.rgb(245,245,245));
        newStage.setScene(newScene);
        newStage.setResizable(false);
        newStage.show();

        return newStage;
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