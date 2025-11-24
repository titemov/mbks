import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;

public class Backend extends Interface{

    private static ArrayList<Secrecy> secrecies = new ArrayList<>();
    private static ArrayList<Folder> folders = new ArrayList<>();

    private void createSecrecyHandler(String name, int level){
        FileWorker fw = new FileWorker();

        if(level<0) level=0;

        boolean isExist=false;

        for(int i = 0; i< secrecies.size(); i++){
            if(Objects.equals(secrecies.get(i).getName(),name) || Objects.equals(secrecies.get(i).getLevel(),level)){
                isExist=true;
                System.out.println("One of the subjects already exists!");
            }
        }

        if(!isExist){
            secrecies.add(new Secrecy(name,level));
            fw.writeSecrecy(secrecies);
        }

        for(int i = 0; i< secrecies.size(); i++){
            System.out.println(secrecies.get(i).getName()+" "+ secrecies.get(i).getLevel());
        }
        System.out.println("=========================");
    }

    private void changeSecrecyHandler(int mode, String oldName, String newName){
        //mode==0 names; mode==1 levels;
        FileWorker fw = new FileWorker();
        try{
            oldName = oldName.split(" ")[0];
            newName = newName.split(" ")[0];
        }catch (Exception e){
            System.out.println("Nothing to split.");
        }

        System.out.println("oldname: "+oldName+"; newname: "+newName);

        if(mode==0){
            for(int i=0;i<secrecies.size();i++){
                if(Objects.equals(secrecies.get(i).getName(),oldName)){
                    secrecies.get(i).setName(newName);
                    fw.writeSecrecy(secrecies);
                }
            }
        }else{
            for(int i=0;i<secrecies.size();i++){
                if(Objects.equals(secrecies.get(i).getLevel(),Integer.parseInt(oldName))){
                    secrecies.get(i).setLevel(Integer.parseInt(newName));
                    fw.writeSecrecy(secrecies);
                }
            }
            for(int i=0;i<folders.size();i++){
                if(folders.get(i).getSecrecyLevel()==Integer.parseInt(oldName)){
                    folders.get(i).setSecrecyLevel(Integer.parseInt(newName));
                }
            }
            fw.writeFolder(folders);
        }
        //if level changed, change it in folders.txt
    }

    private void removeSecrecyHandler(int mode, String value){
        //mode==0 names; mode==1 levels;
        FileWorker fw = new FileWorker();
        try{
            value = value.split(" ")[0];
        }catch (Exception e){
            System.out.println("Nothing to split.");
        }

        System.out.println("value: "+value+"; mode: "+mode);

        if(mode==0){
            for(int i=0;i<secrecies.size();i++){
                if(Objects.equals(secrecies.get(i).getName(),value)){
                    int secrecyLevel = secrecies.get(i).getLevel();
                    secrecies.remove(i);
                    for(int n=folders.size()-1;n>=0;n--){
                        if(Objects.equals(folders.get(n).getSecrecyLevel(),secrecyLevel)){
                            removeFolderHandler(folders.get(n).getName(),folders.get(n).getPath());
                            folders.remove(n);
                        }
                    }
                    fw.writeFolder(folders);
                    fw.writeSecrecy(secrecies);
                }
            }
        }else{
            for(int i=0;i<secrecies.size();i++){
                if(Objects.equals(secrecies.get(i).getLevel(),Integer.parseInt(value))){
                    secrecies.remove(i);
                    for(int n=folders.size()-1;n>=0;n--){
                        if(folders.get(n).getSecrecyLevel()==Integer.parseInt(value)){
                            removeFolderHandler(folders.get(n).getName(),folders.get(n).getPath());
                            folders.remove(n);
                        }
                    }
                    fw.writeFolder(folders);
                    fw.writeSecrecy(secrecies);
                }
            }
        }
        //if level removed, remove all folders with this level
    }

    private void createFolderHandler(String name,int secrecyLevel,String path){
        FileWorker fw = new FileWorker();
        if(!path.endsWith("\\")) path+="\\";

        //check if folder exists
        try {
            String[] folderNames = path.split("\\\\");
            //System.out.println(Arrays.toString(folderNames));
            for (int i = 1; i < folderNames.length; i++) {
                if(!Objects.equals(folderNames[i],null) || !Objects.equals(folderNames[i],"")) {
                    //System.out.println(folderNames[i]);
                    boolean exists = false;

                    for (int n = 0; n < folders.size(); n++) {
                        if (Objects.equals(folderNames[i], folders.get(n).getName())) {
                            exists = true;
                            break;
                        }
                    }
                    if (!exists) {
                        createFolderHandler(folderNames[i], secrecyLevel, path.split(folderNames[i])[0]);
                    }
                }
            }
        }catch (Exception e){
            System.out.println(e);
        }

        String folderPath = path+name;
        File folder = new File(folderPath);

        if (folder.mkdirs()) {
            System.out.println("Folder created successfully at: " + folderPath);
            folders.add(new Folder(name,secrecyLevel,path));
            fw.writeFolder(folders);
        } else {
            System.out.println("Failed to create folder or folder already exists at: " + folderPath);
        }
    }

    private void changeFolderHandler(String oldName,String newName,String path){
        FileWorker fw = new FileWorker();

        if(!path.endsWith("\\")) path+="\\";

        File oldDir = new File(path+oldName);
        File newDir = new File(path+newName);

        try{
            oldDir.renameTo(newDir);
            System.out.println("Folder successfully renamed from \""+oldDir.getName()+"\" to " + newDir.getName());
            for(int i=0;i<folders.size();i++) {
                if((folders.get(i).getPath()).contains(oldName)){
                    String[] temp = folders.get(i).getPath().split(oldName);
                    folders.get(i).setPath(temp[0]+newName+temp[1]);
                }
            }
            for(int i=0;i<folders.size();i++){
                if(Objects.equals(folders.get(i).getName(),oldName) && Objects.equals(folders.get(i).getPath(),path)){
                    folders.get(i).setName(newName);
                }
            }
            fw.writeFolder(folders);
        }catch (Exception e) {
            System.out.println("Failed to rename");
        }
    }

    private void removeFolderHandler(String name,String path){
        FileWorker fw = new FileWorker();
        String folderPath = path+name;
        File folder = new File(folderPath);

        try{
            deleteDirectory(folder);
            System.out.println("Folder (and subfolders) removed successfully at: " + folderPath);
            for(int i=0;i<folders.size();i++) {
                //delete root folder
                if (Objects.equals(name, folders.get(i).getName()) && Objects.equals(path, folders.get(i).getPath())) {
                    folders.remove(i);
                }
            }
            for(int i=folders.size()-1;i>=0;i--){
                //if abs path of folder in other abs path - remove
                if(folders.get(i).getPath().contains(folderPath)){
                    folders.remove(i);
                }
            }
            fw.writeFolder(folders);
        }catch (Exception e) {
            System.out.println(e);
            System.out.println("Failed to remove folder or folder already removed at: " + folderPath);
        }
    }

    private void changeFolderLevelHandler(String path, int newLevel){
        FileWorker fw = new FileWorker();
        for(int i=0;i<folders.size();i++){
            if((folders.get(i).getPath()+folders.get(i).getName()).contains(path)){
//                String[] temp = path.split("\\\\");
//                String tempPath="";
//                for(int n = 0;n<temp.length-1;n++){
//                    tempPath+=temp[n]+"\\"+temp[n+1];
//                    for(int m=0;m<folders.size();m++) {
//                        if (Objects.equals(tempPath, folders.get(m).getPath()+folders.get(m).getName())){
//                            System.out.println();
//                            if(folders.get(m).getSecrecyLevel()>newLevel){
//                                return;
//                            }
//                        }
//                    }
//                }
                folders.get(i).setSecrecyLevel(newLevel);
            }
        }
        fw.writeFolder(folders);
    }

    private void copyContentsHandler(int level, String source, String target){
        boolean canCopyTo=false;
        boolean canReadFrom=false;
        int targetLevel=0;
        //check
//        for(int i=0;i<folders.size();i++){
//            System.out.println("read: "+folders.get(i).getPath()+folders.get(i).getName()+"; source: "+source);
//            if(Objects.equals(folders.get(i).getPath()+folders.get(i).getName(),source)){
//                if(folders.get(i).getSecrecyLevel()<=level){
//                    canReadFrom=true;
//                    break;
//                }
//            }
//        }
        for(int i=0;i<folders.size();i++){
            System.out.println("write: "+folders.get(i).getPath()+folders.get(i).getName()+"; target: "+target);
            if(Objects.equals(folders.get(i).getPath()+folders.get(i).getName(),target)){
                targetLevel=folders.get(i).getSecrecyLevel();
                if(targetLevel>=level){
                    canCopyTo=true;
                    break;
                }
            }
        }

        System.out.println("bool:"+canReadFrom+" "+canCopyTo);

        ArrayList<String> filePaths = new ArrayList<>();
        if(canCopyTo) {
            File directory = new File(source);
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        filePaths.add(file.getPath());//getting file paths
                        System.out.println(file.getPath());
                    }
                }
            }
            for (int i = 0; i < filePaths.size(); i++) {
                try {
                    Runtime rs = Runtime.getRuntime();
                    rs.exec("xcopy " + filePaths.get(i) + " " + target + " /I");
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
        File directory = new File(source);
        if (directory.exists() && directory.isDirectory()) {
            File[] subdirectories = directory.listFiles(File::isDirectory);

            if (subdirectories != null) {
                for (File subdir : subdirectories) {
                    System.out.println(subdir.getPath());
                    System.out.println(target + "\\" + subdir.getName());
                    int currentLevel=level+1;
                    for(int i=0;i<folders.size();i++){
                        System.out.println("AAAA " + subdir.getPath()+" " + folders.get(i).getPath()+folders.get(i).getName());
                        if(Objects.equals(subdir.getPath(),folders.get(i).getPath()+folders.get(i).getName())){
                            currentLevel=folders.get(i).getSecrecyLevel();//?
                            break;
                        }
                    }
                    System.out.println(currentLevel+" "+targetLevel);
                    if(currentLevel<=targetLevel) {
                        createFolderHandler(subdir.getName(), currentLevel, target);
                        copyContentsHandler(currentLevel, subdir.getPath(), target + "\\" + subdir.getName());
                    }
                }
            }
        } else {
            System.out.println("Folder does not exist");
        }
    }

    public Stage createSecrecy(){
        Stage newStage = new Stage();
        newStage.setWidth(300);
        newStage.setHeight(100);

        Group group = new Group();
        newStage.setTitle("CreateSecrecy");

        Label label = new Label("Create secrecy:");
        label.setLayoutX(10);
        label.setLayoutY(5);
        group.getChildren().add(label);

        TextField nameTF = new TextField();
        nameTF.setLayoutX(10);
        nameTF.setLayoutY(30);
        nameTF.setPromptText("Name...");
        nameTF.setPrefSize(90,20);
        nameTF.setFocusTraversable(false);
        group.getChildren().add(nameTF);

        TextField levelTF = new TextField();
        levelTF.setLayoutX(110);
        levelTF.setLayoutY(30);
        levelTF.setPromptText("Level...");
        levelTF.setPrefSize(90,20);
        levelTF.setFocusTraversable(false);
        group.getChildren().add(levelTF);

        Button enterButton = new Button("Enter");
        enterButton.setLayoutX(210);
        enterButton.setLayoutY(30);
        enterButton.setPrefSize(60,20);
        enterButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    createSecrecyHandler(nameTF.getText(), Integer.parseInt(levelTF.getText()));
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
        group.getChildren().add(enterButton);

        Scene newScene = new Scene(group, Color.rgb(245,245,245));
        newStage.setScene(newScene);
        newStage.setResizable(false);
        newStage.show();

        return newStage;
    }

    public Stage changeSecrecy(){
        Stage newStage = new Stage();
        newStage.setWidth(400);
        newStage.setHeight(100);

        Group group = new Group();
        newStage.setTitle("ChangeSecrecy");

        Label label = new Label("Change secrecy values:");
        label.setLayoutX(10);
        label.setLayoutY(5);
        group.getChildren().add(label);

        ObservableList<String> observableList = FXCollections.observableArrayList("Name","Level");
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
        oldNameTF.setPromptText("Old value...");
        oldNameTF.setPrefSize(90,20);
        oldNameTF.setFocusTraversable(false);
        group.getChildren().add(oldNameTF);

        TextField newNameTF = new TextField();
        newNameTF.setLayoutX(210);
        newNameTF.setLayoutY(30);
        newNameTF.setPromptText("New value...");
        newNameTF.setPrefSize(90,20);
        newNameTF.setFocusTraversable(false);
        group.getChildren().add(newNameTF);

        Button enterButton = new Button("Change");
        enterButton.setLayoutX(310);
        enterButton.setLayoutY(30);
        enterButton.setPrefSize(60,20);
        enterButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    int mode;
                    String userChoice = choiceCB.getValue();
                    if(Objects.equals(userChoice,"Name")){
                        mode=0;
                    }else{
                        mode=1;
                    }
                    changeSecrecyHandler(mode,oldNameTF.getText(),newNameTF.getText());
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
        group.getChildren().add(enterButton);

        Scene newScene = new Scene(group, Color.rgb(245,245,245));
        newStage.setScene(newScene);
        newStage.setResizable(false);
        newStage.show();

        return newStage;
    }

    public Stage removeSecrecy(){
        Stage newStage = new Stage();
        newStage.setWidth(300);
        newStage.setHeight(100);

        Group group = new Group();
        newStage.setTitle("RemoveSecrecy");

        Label label = new Label("Remove secrecy by value:");
        label.setLayoutX(10);
        label.setLayoutY(5);
        group.getChildren().add(label);

        ObservableList<String> observableList = FXCollections.observableArrayList("Name","Level");
        ComboBox<String> choiceCB = new ComboBox<>(observableList);
        choiceCB.setLayoutX(10);
        choiceCB.setLayoutY(30);
        choiceCB.setPrefWidth(90);
        choiceCB.setVisibleRowCount(5);
        choiceCB.setValue(observableList.get(0));
        group.getChildren().add(choiceCB);

        TextField valueTF = new TextField();
        valueTF.setLayoutX(110);
        valueTF.setLayoutY(30);
        valueTF.setPromptText("Value...");
        valueTF.setPrefSize(90,20);
        valueTF.setFocusTraversable(false);
        group.getChildren().add(valueTF);

        Button enterButton = new Button("Remove");
        enterButton.setLayoutX(210);
        enterButton.setLayoutY(30);
        enterButton.setPrefSize(60,20);
        enterButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    int mode;
                    String userChoice = choiceCB.getValue();
                    if(Objects.equals(userChoice,"Name")){
                        mode=0;
                    }else{
                        mode=1;
                    }
                    removeSecrecyHandler(mode,valueTF.getText());
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
        group.getChildren().add(enterButton);

        Scene newScene = new Scene(group, Color.rgb(245,245,245));
        newStage.setScene(newScene);
        newStage.setResizable(false);
        newStage.show();

        return newStage;
    }

    public Stage createFolder(){
        Stage newStage = new Stage();
        newStage.setWidth(300);
        newStage.setHeight(100);

        Group group = new Group();
        newStage.setTitle("CreateFolder");

        Label label = new Label("Create folder:   (please use \" \\\" in path)");
        label.setLayoutX(10);
        label.setLayoutY(5);
        group.getChildren().add(label);

        TextField nameTF = new TextField();
        nameTF.setLayoutX(10);
        nameTF.setLayoutY(30);
        nameTF.setPromptText("Name...");
        nameTF.setPrefSize(90,20);
        nameTF.setFocusTraversable(false);
        group.getChildren().add(nameTF);

        TextField pathTF = new TextField();
        pathTF.setLayoutX(110);
        pathTF.setLayoutY(30);
        pathTF.setPromptText("Path...");
        pathTF.setPrefSize(90,20);
        pathTF.setFocusTraversable(false);
        group.getChildren().add(pathTF);

        Button enterButton = new Button("Create");
        enterButton.setLayoutX(210);
        enterButton.setLayoutY(30);
        enterButton.setPrefSize(60,20);
        enterButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    String path;
                    if(Objects.equals(pathTF.getText(),null) || Objects.equals(pathTF.getText(),"")){
                        path=".\\";
                    }else{
                        path=pathTF.getText();
                    }

                    int minLevel= (int) (Math.pow(2,31)-1);

                    for(int i=0;i<secrecies.size();i++){
                        System.out.println(secrecies.get(i).getLevel());
                        if(secrecies.get(i).getLevel()<=minLevel){
                            minLevel=secrecies.get(i).getLevel();
                        }
                    }

                    if(minLevel==(int) (Math.pow(2,31)-1)) throw new Exception("No secrecy levels");

                    createFolderHandler(nameTF.getText(),minLevel,path);
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
        group.getChildren().add(enterButton);

        Scene newScene = new Scene(group, Color.rgb(245,245,245));
        newStage.setScene(newScene);
        newStage.setResizable(false);
        newStage.show();

        return newStage;
    }

    public Stage changeFolder(){
        Stage newStage = new Stage();
        newStage.setWidth(400);
        newStage.setHeight(100);

        Group group = new Group();
        newStage.setTitle("ChangeFolder");

        Label label = new Label("Change folder:   (please use \" \\\" in path)");
        label.setLayoutX(10);
        label.setLayoutY(5);
        group.getChildren().add(label);

        TextField pathTF = new TextField();
        pathTF.setLayoutX(10);
        pathTF.setLayoutY(30);
        pathTF.setPromptText("Path...");
        pathTF.setPrefSize(90,20);
        pathTF.setFocusTraversable(false);
        group.getChildren().add(pathTF);

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

        Button enterButton = new Button("Change");
        enterButton.setLayoutX(310);
        enterButton.setLayoutY(30);
        enterButton.setPrefSize(60,20);
        enterButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    String path;
                    if(Objects.equals(pathTF.getText(),null) || Objects.equals(pathTF.getText(),"")){
                        path=".\\";
                    }else{
                        path=pathTF.getText();
                    }
                    changeFolderHandler(oldNameTF.getText(),newNameTF.getText(),path);
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
        group.getChildren().add(enterButton);

        Scene newScene = new Scene(group, Color.rgb(245,245,245));
        newStage.setScene(newScene);
        newStage.setResizable(false);
        newStage.show();

        return newStage;
    }

    public Stage removeFolder(){
        Stage newStage = new Stage();
        newStage.setWidth(300);
        newStage.setHeight(100);

        Group group = new Group();
        newStage.setTitle("RemoveFolder");

        Label label = new Label("Remove folder:   (please use \" \\\" in path)");
        label.setLayoutX(10);
        label.setLayoutY(5);
        group.getChildren().add(label);

        TextField pathTF = new TextField();
        pathTF.setLayoutX(10);
        pathTF.setLayoutY(30);
        pathTF.setPromptText("Path...");
        pathTF.setPrefSize(90,20);
        pathTF.setFocusTraversable(false);
        group.getChildren().add(pathTF);

        TextField nameTF = new TextField();
        nameTF.setLayoutX(110);
        nameTF.setLayoutY(30);
        nameTF.setPromptText("Name...");
        nameTF.setPrefSize(90,20);
        nameTF.setFocusTraversable(false);
        group.getChildren().add(nameTF);

        Button enterButton = new Button("Remove");
        enterButton.setLayoutX(210);
        enterButton.setLayoutY(30);
        enterButton.setPrefSize(60,20);
        enterButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    String path;
                    if(Objects.equals(pathTF.getText(),null) || Objects.equals(pathTF.getText(),"")){
                        path=".\\";
                    }else{
                        path=pathTF.getText();
                    }
                    removeFolderHandler(nameTF.getText(),path);
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
        group.getChildren().add(enterButton);

        Scene newScene = new Scene(group, Color.rgb(245,245,245));
        newStage.setScene(newScene);
        newStage.setResizable(false);
        newStage.show();

        return newStage;
    }

    public Stage changeFolderLevel(){
        Stage newStage = new Stage();
        newStage.setWidth(300);
        newStage.setHeight(100);

        Group group = new Group();
        newStage.setTitle("ChangeFolderLevel");

        Label label = new Label("Change folder level:   (please use \" \\\" in path)");
        label.setLayoutX(10);
        label.setLayoutY(5);
        group.getChildren().add(label);

        TextField pathTF = new TextField();
        pathTF.setLayoutX(10);
        pathTF.setLayoutY(30);
        pathTF.setPromptText("Path...");
        pathTF.setPrefSize(90,20);
        pathTF.setFocusTraversable(false);
        group.getChildren().add(pathTF);

        ArrayList<Integer> levels = new ArrayList<>();
        for(int i=0;i<secrecies.size();i++){
            levels.add(secrecies.get(i).getLevel());
        }

        ObservableList<Integer> observableList = FXCollections.observableArrayList(levels);
        ComboBox<Integer> choiceCB = new ComboBox<>(observableList);
        choiceCB.setLayoutX(110);
        choiceCB.setLayoutY(30);
        choiceCB.setPrefWidth(90);
        choiceCB.setVisibleRowCount(5);
        choiceCB.setValue(observableList.get(0));
        group.getChildren().add(choiceCB);

        Button enterButton = new Button("Change");
        enterButton.setLayoutX(210);
        enterButton.setLayoutY(30);
        enterButton.setPrefSize(60,20);
        enterButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    String path;
                    if(Objects.equals(pathTF.getText(),null) || Objects.equals(pathTF.getText(),"")){
                        throw new Exception();
                    }else{
                        path=pathTF.getText();
                    }
                    changeFolderLevelHandler(path,choiceCB.getValue());
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
        group.getChildren().add(enterButton);

        Scene newScene = new Scene(group, Color.rgb(245,245,245));
        newStage.setScene(newScene);
        newStage.setResizable(false);
        newStage.show();

        return newStage;
    }

    public Stage showInfo(){
        Stage newStage = new Stage();
        newStage.setWidth(750);
        newStage.setHeight(550);

        Group group = new Group();
        newStage.setTitle("Info");

        GridPane gridPane = new GridPane();
        //gridPane.getColumnConstraints().add(new ColumnConstraints(80));
        gridPane.setGridLinesVisible(true);

        Label emptyLabel = new Label(" ");
        Label[] rowNames = new Label[secrecies.size()];
        Label[] columnNames = new Label[folders.size()];

        gridPane.getChildren().add(emptyLabel);
        GridPane.setMargin(emptyLabel,new Insets(15));
        GridPane.setConstraints(emptyLabel,0,0);

        for(int i=0;i<rowNames.length;i++){
            rowNames[i] = new Label(" "+secrecies.get(i).getName()+" (Level "+secrecies.get(i).getLevel()+")");
            gridPane.getChildren().add(rowNames[i]);
            GridPane.setConstraints(rowNames[i],0,i+1);
            GridPane.setMargin(rowNames[i],new Insets(15));
        }

        for(int i=0;i<columnNames.length;i++){
            columnNames[i] = new Label(folders.get(i).getPath()+folders.get(i).getName());
            gridPane.getChildren().add(columnNames[i]);
            GridPane.setConstraints(columnNames[i],i+1,0);
            GridPane.setMargin(columnNames[i],new Insets(15));
        }

        Label[][] cross = new Label[secrecies.size()][folders.size()];
        for(int i=0;i<secrecies.size();i++){
            for(int n=0;n<folders.size();n++){
                if(folders.get(n).getSecrecyLevel()==secrecies.get(i).getLevel()){
                    cross[i][n] = new Label("+");
                    gridPane.getChildren().add(cross[i][n]);
                    GridPane.setConstraints(cross[i][n], n + 1, i + 1);
                    GridPane.setMargin(cross[i][n], new Insets(5));
                }
            }
        }

        ScrollPane scrollPane = new ScrollPane(gridPane);
        scrollPane.setLayoutX(5);
        scrollPane.setLayoutY(5);
        scrollPane.setPrefSize(723,500);
        scrollPane.setStyle("-fx-background: rgb(255,255,255)");
        group.getChildren().add(scrollPane);

        Scene newScene = new Scene(group, Color.rgb(245,245,245));
        newStage.setScene(newScene);
        newStage.setResizable(false);
        newStage.show();

        return newStage;
    }

    public Stage copyContents(){
        Stage newStage = new Stage();
        newStage.setWidth(400);
        newStage.setHeight(100);

        Group group = new Group();
        newStage.setTitle("ChangeFolderLevel");

        Label label = new Label("Copy folder content:   (please use \" \\\" in path)");
        label.setLayoutX(10);
        label.setLayoutY(5);
        group.getChildren().add(label);

        ArrayList<Integer> levels = new ArrayList<>();
        for(int i=0;i<secrecies.size();i++){
            levels.add(secrecies.get(i).getLevel());
        }

        ObservableList<String> suitableFolders = FXCollections.observableArrayList();

        ObservableList<Integer> observableLevelList = FXCollections.observableArrayList(levels);
        ComboBox<Integer> levelChoiceCB = new ComboBox<>(observableLevelList);
        levelChoiceCB.setLayoutX(10);
        levelChoiceCB.setLayoutY(30);
        levelChoiceCB.setPrefWidth(90);
        levelChoiceCB.setVisibleRowCount(5);
        levelChoiceCB.setValue(observableLevelList.get(0));

        ComboBox<String> folderChoiceCB = new ComboBox<>(suitableFolders);
        folderChoiceCB.setLayoutX(110);
        folderChoiceCB.setLayoutY(30);
        folderChoiceCB.setPrefWidth(90);
        folderChoiceCB.setVisibleRowCount(5);
        group.getChildren().add(folderChoiceCB);

        // Метод для обновления списка папок
        Runnable updateFolders = () -> {
            suitableFolders.clear();
            Integer selectedLevel = levelChoiceCB.getValue();
            if (selectedLevel != null) {
                for(int i=0;i<folders.size();i++){
                    if(folders.get(i).getSecrecyLevel() == selectedLevel){
                        suitableFolders.add(folders.get(i).getPath() + folders.get(i).getName());
                    }
                }
            }
            // Устанавливаем первое значение если список не пуст
            if (!suitableFolders.isEmpty()) {
                folderChoiceCB.setValue(suitableFolders.get(0));
            }
        };

        // Инициализируем список папок при старте
        updateFolders.run();

        // Обработчик изменения выбора уровня
        levelChoiceCB.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                updateFolders.run();
            }
        });
        group.getChildren().add(levelChoiceCB);

        TextField pathTF = new TextField();
        pathTF.setLayoutX(210);
        pathTF.setLayoutY(30);
        pathTF.setPromptText("Path...");
        pathTF.setPrefSize(90,20);
        pathTF.setFocusTraversable(false);
        group.getChildren().add(pathTF);

        Button enterButton = new Button("Copy");
        enterButton.setLayoutX(310);
        enterButton.setLayoutY(30);
        enterButton.setPrefSize(60,20);
        enterButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    String path;
                    if(Objects.equals(pathTF.getText(),null) || Objects.equals(pathTF.getText(),"")){
                        throw new Exception();
                    }else{
                        path=pathTF.getText();
                    }
                    copyContentsHandler(levelChoiceCB.getValue(),folderChoiceCB.getValue(),path);
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
        group.getChildren().add(enterButton);

        Scene newScene = new Scene(group, Color.rgb(245,245,245));
        newStage.setScene(newScene);
        newStage.setResizable(false);
        newStage.show();

        return newStage;
    }

    public void initialParse(){
        FileWorker fw = new FileWorker();
        ArrayList<Secrecy> temp1 = fw.parseSecrecies("secrecies.txt");
        ArrayList<Folder> temp2 = fw.parseFolders("folders.txt");

        if(Objects.equals(temp1,null)) return;

        for(int i=0;i<temp1.size();i++){
            secrecies.add(new Secrecy(temp1.get(i).getName(),temp1.get(i).getLevel()));
        }

        if(Objects.equals(temp2,null)) return;

        for(int i=0;i< temp2.size();i++){
            folders.add(new Folder(temp2.get(i).getName(),temp2.get(i).getSecrecyLevel(),temp2.get(i).getPath()));
        }
    }

    public static int rng(int low, int high) {
        Random r = new Random();
        return r.nextInt(high - low) + low;
    }

    public static void deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteDirectory(file);
                }
            }
        }
        dir.delete();
    }

}