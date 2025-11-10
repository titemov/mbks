import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.lang.reflect.Array;
import java.util.*;

public class Backend extends Interface {
    private static final Matrix matrix = new Matrix();
    private static final FileNames allFileNames = new FileNames();

    public int addButtonHandler(int mode,String textField){
        FileWorker fw = new FileWorker();
        String text = textField;
        String temp;
        boolean engOnly=true;

        if(mode==1) {
            engOnly = (text+text).matches("^[a-zA-Z][a-zA-Z\\s]+$");
            //make it longer than 1 symbol to pass "engOnly" check
            //System.out.println(engOnly); somewhy english 1 symbol cannot pass this test
        }

        if(!Objects.equals(text,"") && text.length()<256 && engOnly){
            temp = textField;
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
            return 1;
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
                        System.out.println("_____\nComparing:");
                        System.out.println(splitted[i]+"\nWith:");
                        System.out.println(matrix.getEmployees().get(n).getName());
                        System.out.println("k="+k);
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

            fw.writeInFile(matrix);
            //
            String[] abc = matrix.getAllNames();
            for(int i=0;i< abc.length;i++){
                System.out.println("Name: "+abc[i]);
            }
        }else{
            //getting rid of spaces (" ");
            try{
                String[] abc = temp.split(" ");
                temp="";
                for(int i=0;i<abc.length;i++){
                    temp+=abc[i];
                }
            }catch (Exception e){
                System.out.println("No spaces found");
            }

            String[] splitted = removeDuplicates(temp.split("(?!^)"));

            allFileNames.addFiles(splitted);

            fw.writeInFile(matrix);
            //
            String[] abc = allFileNames.getAllFileNames();
            for(int i=0;i< abc.length;i++){
                System.out.println("File: "+abc[i]);
            }
        }
        return 0;
    }

    private void removeButtonHandler(ObservableList<String> observableList,int mode, ComboBox choiceCB, Label label){
        FileWorker fw = new FileWorker();
        System.out.println("removeButtonHandler "+choiceCB.getValue());
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

            for(int i=0;i<matrix.matrixLength();i++){
                try{
                    matrix.getEmployees().get(i).getFileNames().removeByName(value);
                }catch (Exception e){
                    System.out.println("User "+matrix.getEmployees().get(i).getName()+"have no file do remove");
                }
            }

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

    public int changeButtonHandler(int mode, String subjectTF, String objectTF, boolean fromShowMatrix){
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
            return 1;
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
        if(!fromShowMatrix){
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
        return 0;
    }

    private void saveButtonHandler(Label[] rowNames, Label[] columnNames, ComboBox[][] comboBoxes){
        //собирает фулл инфу из измененных строк/столбцов и коллит функцию changeButtonHandler\
        //после записи всех изменений вывести алерт, что операция прошла успешно
        //mode=0 - "Grant"; mode=1 - "Take";
        for(int i=0;i< comboBoxes.length;i++){
            for(int n=0;n<comboBoxes[0].length;n++){
                if(Objects.equals(comboBoxes[i][n].getValue(),"GRANTED")){
                    changeButtonHandler(0,rowNames[i].getText(),columnNames[n].getText(),true);
                }else{
                    changeButtonHandler(1,rowNames[i].getText(),columnNames[n].getText(),true);
                }
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

    private int changeNameButtonHandler(int mode, String oldName, String newName){
        FileWorker fw = new FileWorker();
        try{
            oldName = oldName.split(" ")[0];
            newName = newName.split(" ")[0];
        }catch (Exception e){
            System.out.println("nothing to split");
        }

        System.out.println(oldName+" "+newName);

        //mode=0 subject, mode=1 object
        if(mode==0){
            for(int i=0;i<matrix.matrixLength();i++){
                if(Objects.equals(matrix.getEmployees().get(i).getName(),oldName)){
                    for(int n=0;n<matrix.matrixLength();n++) {
                        if (Objects.equals(matrix.getEmployees().get(n).getName(), newName)) return 1;
                    }
                    matrix.getEmployees().get(i).setName(newName);
                    fw.writeInFile(matrix);
                    return 0;
                }
            }
        }else{
            if(!(newName+newName).matches("^[a-zA-Z][a-zA-Z\\s]+$")){
                return 1;
            }
            newName=newName.split("(?!^)")[0];
            for(int i =0;i< allFileNames.size();i++){
                if(Objects.equals(allFileNames.get(i),oldName)){
                    for(int n=0;n<allFileNames.size();n++) {
                        if (Objects.equals(allFileNames.get(n), newName)) return 1;
                    }
                    for(int n=0;n<matrix.matrixLength();n++){
                        for(int m=0;m<matrix.getEmployees().get(n).getFileNames().size();m++) {
                            if(Objects.equals(matrix.getEmployees().get(n).getFileNames().get(m),oldName)) {
                                matrix.getEmployees().get(n).getFileNames().removeByName(oldName);
                                matrix.getEmployees().get(n).getFileNames().addFiles(new String[]{newName});
                            }
                        }
                    }
                    allFileNames.set(i,newName);
                    fw.writeInFile(matrix);
                    return 0;
                }
            }
        }
        return 1;
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
                addButtonHandler(mode,textField.getText());
                newStage.close();
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
        choiceCB.setValue(observableList.get(1));
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
                changeButtonHandler(mode,subjectTF.getText(),objectTF.getText(),false);
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
        newStage.setWidth(750);
        newStage.setHeight(550);

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
        // В каждой ячейке сделать комбобокс(+,-) соответствующие доступам

        GridPane gridPane = new GridPane();
        //gridPane.getColumnConstraints().add(new ColumnConstraints(80));
        gridPane.setGridLinesVisible(true);

        Label emptyLabel = new Label(" ");
        Label[] rowNames = new Label[matrix.matrixLength()];
        Label[] columnNames = new Label[allFileNames.size()];

        gridPane.getChildren().add(emptyLabel);
        GridPane.setMargin(emptyLabel,new Insets(15));
        GridPane.setConstraints(emptyLabel,0,0);

        for(int i=0;i<rowNames.length;i++){
            rowNames[i] = new Label(matrix.getEmployees().get(i).getName());
            gridPane.getChildren().add(rowNames[i]);
            GridPane.setConstraints(rowNames[i],0,i+1);
            GridPane.setMargin(rowNames[i],new Insets(15));
        }

        for(int i=0;i<columnNames.length;i++){
            columnNames[i] = new Label(allFileNames.get(i));
            gridPane.getChildren().add(columnNames[i]);
            GridPane.setConstraints(columnNames[i],i+1,0);
            GridPane.setMargin(columnNames[i],new Insets(15));
        }

        ObservableList<String> observableList = FXCollections.observableArrayList("GRANTED","RESTRICTED");
        ComboBox[][] comboBoxes = new ComboBox[matrix.matrixLength()][allFileNames.size()];
        for(int i=0;i<comboBoxes.length;i++) {
            for (int n = 0; n < comboBoxes[0].length; n++) {
                comboBoxes[i][n] = new ComboBox(observableList);
                gridPane.getChildren().add(comboBoxes[i][n]);
                GridPane.setConstraints(comboBoxes[i][n], n + 1, i + 1);
                GridPane.setMargin(comboBoxes[i][n], new Insets(5));
            }
        }
        //переделать (костыль на костыле)
        for(int i=0;i<comboBoxes.length;i++){
            for(int n=0;n<comboBoxes[0].length;n++){
                try{
                    String file = matrix.getEmployees().get(i).getFileNames().get(n);
                    for(int m=0;m<allFileNames.size();m++) {
                        if (Objects.equals(file, allFileNames.get(m))) {
                            comboBoxes[i][m].setValue(observableList.get(0));
                            break;
                        }
                    }
                }catch (IndexOutOfBoundsException e){
                    if(!Objects.equals(comboBoxes[i][n].getValue(),"GRANTED")) {
                        comboBoxes[i][n].setValue(observableList.get(1));
                    }
                }//что-то вроде finally, но не finally XDDDDD
                if(Objects.equals(comboBoxes[i][n].getValue(),null)){
                    comboBoxes[i][n].setValue(observableList.get(1));
                }
            }
        }

        ScrollPane scrollPane = new ScrollPane(gridPane);
        scrollPane.setLayoutX(5);
        scrollPane.setLayoutY(5);
        scrollPane.setPrefSize(630,500);
        scrollPane.setStyle("-fx-background: rgb(255,255,255)");
        group.getChildren().add(scrollPane);

        Button saveButton = new Button("Save");
        saveButton.setLayoutX(645);
        saveButton.setLayoutY(475);
        saveButton.setPrefSize(80,20);
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //System.out.println("SAVE");
                saveButtonHandler(rowNames,columnNames,comboBoxes);
            }
        });
        group.getChildren().add(saveButton);

//        Button resetButton = new Button("Reset");
//        resetButton.setLayoutX(645);
//        resetButton.setLayoutY(440);
//        resetButton.setPrefSize(80,20);
//        resetButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
//                //сохранить значения matrix и allFileNames в локальные переменные и затем подставить сохраненные значения через changeButtonHandler
//                //после записи всех изменений вывести алерт, что операция прошла успешно
//                //КАК СОХРАНИТЬ???????? поэтому и закомментил...
//                System.out.println("Reset");
//
//            }
//        });
//        group.getChildren().add(resetButton);


        Scene newScene = new Scene(group, Color.rgb(245,245,245));
        newStage.setScene(newScene);
        newStage.setResizable(false);
        newStage.show();
        return newStage;
    }

    public String consoleEnter(TextArea textArea){
        String output="";
        try {
            String[] temp = textArea.getText().split(">>>");
            //System.out.println(Arrays.toString(temp));

            String userInput = temp[temp.length - 1];
            if(temp.length==0) throw new Exception("No input");

            //System.out.println("before: "+userInput);
            try {//in case if user entered \n after entering a command
                userInput = userInput.split("\n")[0];
            }catch (Exception e){
                System.out.println(e);
            }
            System.out.println("userInput after: "+userInput);

            CommandLine cl = new CommandLine(userInput,allFileNames);
            output=cl.chooseCommand();
        }catch(Exception e){
            output="Error! No input.";
        }

        return output;
    }

    public void exportMatrix(){
        FileWorker fw = new FileWorker();
        String name = fw.exportMatrix(matrix);
        if(!Objects.equals(name,null)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Success");
            alert.setContentText("Matrix saved as " + name + ".");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
        }else{
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
    }

    public void importMatrix(String path){
        Backend a = new Backend();
        FileWorker fw = new FileWorker();
        int result = fw.importMatrix(path);

        try{
            if(result==0) {
                if(Objects.equals(fw.parseObjects(path),null) || Objects.equals(fw.parseSubjects(path),null)){
                    throw new Exception();
                }
                System.out.println(matrix.matrixLength());
                matrix.clearEmployees();
                System.out.println(matrix.matrixLength());
                allFileNames.clearFileNames();
                a.parseMatrix(path);
                fw.writeInFile(matrix);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Success");
                alert.setContentText("Matrix imported from " + path + ".");
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                        System.out.println("Pressed OK.");
                    }
                });
            }else{
                throw new Exception();
            }
        }catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Cannot read file from given path.");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
        }
    }

    public Stage changeName(){

        Stage newStage = new Stage();
        newStage.setWidth(400);
        newStage.setHeight(100);

        Group group = new Group();
        newStage.setTitle("ChangeName");

        Label label = new Label("Change Name:");
        label.setLayoutX(10);
        label.setLayoutY(10);
        group.getChildren().add(label);

        ObservableList<String> observableList = FXCollections.observableArrayList("Subject","Object");
        ComboBox<String> choiceCB = new ComboBox<>(observableList);
        choiceCB.setLayoutX(10);
        choiceCB.setLayoutY(30);
        choiceCB.setPrefWidth(90);
        choiceCB.setVisibleRowCount(5);
        choiceCB.setValue(observableList.get(0));
        group.getChildren().add(choiceCB);

        TextField oldNameTF = new TextField();
        oldNameTF.setLayoutX(110);
        oldNameTF.setLayoutY(30);
        oldNameTF.setPromptText("Old name...");
        oldNameTF.setPrefSize(90,20);
        oldNameTF.setFocusTraversable(false);
        group.getChildren().add(oldNameTF);

        TextField newNameTF = new TextField();
        newNameTF.setLayoutX(210);
        newNameTF.setLayoutY(30);
        newNameTF.setPromptText("New name...");
        newNameTF.setPrefSize(90,20);
        newNameTF.setFocusTraversable(false);
        group.getChildren().add(newNameTF);


        Button enterBtn = new Button("Change");
        enterBtn.setPrefSize(65,15);
        enterBtn.setLayoutX(310);
        enterBtn.setLayoutY(30);
        enterBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int mode;//mode=0 - "Subject"; mode=1 - "Object";
                String userChoice = choiceCB.getValue();
                if(Objects.equals(userChoice,"Subject")){
                    mode=0;
                }else{
                    mode=1;
                }
                //System.out.println(123);
                if(changeNameButtonHandler(mode,oldNameTF.getText(),newNameTF.getText())!=0){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error");
                    alert.setContentText("Incorrect new name.");
                    alert.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {
                            System.out.println("Pressed OK.");
                        }
                    });
                }else{
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

    public void parseMatrix(String path){
        //парсить текстовый файл, заносить содержимое в объекты matrix и allFileNames
        FileWorker fw = new FileWorker();
        ArrayList<String> subjects = fw.parseSubjects(path);
        ArrayList<String> objects = fw.parseObjects(path);

        if(Objects.equals(subjects,null) || Objects.equals(objects,null)) return;

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