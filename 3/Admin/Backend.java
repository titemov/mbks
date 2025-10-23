import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.lang.reflect.Array;
import java.util.*;

public class Backend extends Interface {
    //private String result="";
    private static final Matrix matrix = new Matrix();
    private static final FileNames allFileNames = new FileNames();

    private void addButtonHandler(int mode,TextField textField, Stage newStage){
        FileWorker fw = new FileWorker();
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

            //проверка на уже существующие субъекты
            ArrayList<String> subjects = new ArrayList<>();
            //System.out.println(matrix.matrixLength());
            if(matrix.matrixLength()!=0) {
                for (int i = 0; i < splitted.length; i++) {
                    int k = 0;
                    for (int n = 0; n < matrix.matrixLength(); n++) {
                        if (Objects.equals(matrix.getEmployees().get(n).getName(), splitted[i])) {
                            k += 1;
                        }
                        System.out.println("_____");
                        System.out.println(splitted[i]);
                        System.out.println(matrix.getEmployees().get(n).getName());
                        System.out.println(k);
                        System.out.println("_____");
                    }
                    if (k == 0) {
                        subjects.add(splitted[i]);
                    }
                }
            }else{
                Collections.addAll(subjects,splitted);
            }

            for(int i=0;i<subjects.size();i++){
                matrix.addEmployee(new Employee(subjects.get(i),null));
            }
            //
            fw.writeInFile(matrix);
            String[] abc = matrix.getAllNames();
            for(int i=0;i< abc.length;i++){
                System.out.println("Name: "+abc[i]);
            }
        }else{
            String[] splitted = removeDuplicates(temp.split("(?!^)"));
            allFileNames.addFiles(splitted);
            //
            fw.writeInFile(matrix);
            String[] abc = allFileNames.getAllFileNames();
            for(int i=0;i< abc.length;i++){
                System.out.println("File: "+abc[i]);
            }
        }
    }

    private void removeButtonHandler(ObservableList<String> observableList,int mode, ComboBox choiceCB, Label label){
        FileWorker fw = new FileWorker();
        System.out.println(choiceCB.getValue());
        String value = String.valueOf(choiceCB.getValue());
        if(mode==0){//mode=0 - for subjects
            try {
                matrix.removeEmployee(value);
                observableList.remove(value);
                fw.writeInFile(matrix);
            }catch (Exception e){
                System.out.println(e);
            }
            label.setText("Remove subject(s)"+"   "+value+" was removed");
        }else{
            //при удалении файлов из общего списка удалять этот файл у всех юзеров



            String temp = allFileNames.removeByName(value);
            if(Objects.isNull(temp)){
                label.setText("Remove object(s)"+"   "+"Nothing was removed");
            }else{
                label.setText("Remove object(s)"+"   "+value+" was removed");
                observableList.remove(value);
            }
            fw.writeInFile(matrix);
        }
    }

    private void changeButtonHandler(int mode, String subjectTF, String objectTF){
        FileWorker fw = new FileWorker();
        ArrayList<String> subjects = new ArrayList<>();
        ArrayList<String> objects = new ArrayList<>();

        String[] subjectsFromTF = removeDuplicates(subjectTF.split(" "));
        String[] objectsFromTF = removeDuplicates(objectTF.split("(?!^)"));

        //сделать проверку существования субъектов в целом
        //сделать проверку существования файлов в целом
        //вызвать функцию addFilesToEmployee

        for(int i=0;i<matrix.matrixLength();i++){
            for(int n=0;n<subjectsFromTF.length;n++){
                if(Objects.equals(matrix.getEmployees().get(i).getName(), subjectsFromTF[n])){
                    subjects.add(subjectsFromTF[n]);
                    break;
                }
            }
        }

        for(int i=0;i< allFileNames.size();i++){
            for(int n=0;n<objectsFromTF.length;n++){
                if(Objects.equals(allFileNames.get(i), objectsFromTF[n])){
                    objects.add(objectsFromTF[n]);
                    break;
                }
            }
        }

        if(subjects.size()==0 || objects.size()==0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No subject/object to change");
            alert.setContentText("There is no subject/object needed to change");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
            return;
        }

        if(mode==0) {
            for (int i = 0; i < subjects.size(); i++) {
                int a = matrix.addFilesToEmployee(subjects.get(i), objects);
                if (a == -1) {
                    System.out.println("No name " + subjects.get(i));
                }
                fw.writeInFile(matrix);
            }
        }else{
            for (int i = 0; i < subjects.size(); i++) {
                int a = matrix.removeFilesFromEmployee(subjects.get(i), objects);
                if (a == -1) {
                    System.out.println("No name " + subjects.get(i));
                }
                fw.writeInFile(matrix);
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Success");
        alert.setContentText("All changes are done.");
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                System.out.println("Pressed OK.");
            }
        });
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

        Label label = new Label();
        label.setLayoutX(10);
        label.setLayoutY(10);
        group.getChildren().add(label);

        for(int i=0;i<matrix.matrixLength();i++){
            label.setText(label.getText()+matrix.getEmployees().get(i).getName()+" "
                    +Arrays.toString(matrix.getEmployees().get(i).getFileNames().getAllFileNames())+"\n");
        }

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
        newStage.setWidth(400);
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

        ObservableList<String> observableList = FXCollections.observableArrayList("Grant","Take");
        ComboBox<String> choiceCB = new ComboBox<>(observableList);
        choiceCB.setLayoutX(210);
        choiceCB.setLayoutY(30);
        choiceCB.setPrefWidth(80);
        choiceCB.setVisibleRowCount(5);
        group.getChildren().add(choiceCB);


        Button enterBtn = new Button("Change");
        enterBtn.setPrefSize(80,15);
        enterBtn.setLayoutX(300);
        enterBtn.setLayoutY(30);
        enterBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int mode;//mode=0 - "Grant"; mode=1 - "Take";
                String userChoice = choiceCB.getValue();
                if(Objects.equals(userChoice,"Grant")){
                    mode=0;
                }else{
                    mode=1;
                }
                changeButtonHandler(mode,subjectTF.getText(),objectTF.getText());
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

    public static ArrayList removeArrayListDuplicates(ArrayList array){
        Collections.sort(array);

        String current = (String) array.get(0);
        boolean found = false;
        String str="";
        for(int i = 0; i < array.size(); i++) {
            if (Objects.equals(current, array.get(i)) && !found) {
                found = true;
            } else if (!Objects.equals(current, array.get(i))) {
                str+=current+" ";
                current = (String) array.get(i);
                found = false;
            }
        }
        str+=current+" ";

        ArrayList<String> result = new ArrayList<>();
        Collections.addAll(result,str.split(" "));
        return result;
    }

    public void initialParse(){
        //парсить текстовый файл, заносить содержимое в объекты matrix и allFileNames
        FileWorker fw = new FileWorker();
        ArrayList<String> subjects = fw.parseSubjects();
        ArrayList<String> objects = fw.parseObjects();

        for(int i=0;i<subjects.size();i++){
            matrix.addEmployee(new Employee(subjects.get(i),objects.get(i).split("(?!^)")));
        }

        String str = "";
        for(int i=0; i<objects.size(); i++){
            str+=objects.get(i);
        }

        allFileNames.addFiles(removeDuplicates(str.split("(?!^)")));
    }
}