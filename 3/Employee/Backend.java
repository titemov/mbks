import javafx.scene.control.*;
import java.util.*;

public class Backend extends Interface {
    private static final Matrix matrix = new Matrix();
    private static final FileNames allFileNames = new FileNames();
    public void importMatrix(String path){
        Backend a = new Backend();
        FileWorker fw = new FileWorker();
        int result = fw.importMatrix(path);

        try{
            if(result==0) {
                if(Objects.equals(fw.parseObjects(path),null) || Objects.equals(fw.parseSubjects(path),null)){
                    throw new Exception();
                }
                matrix.clearEmployees();
                allFileNames.clearFileNames();
                a.parseMatrix(path);
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

    public String getUserInfo(String name, String files){
        Backend a = new Backend();
        String output="";
        String employee="";
        String accessibleFiles="";
        String[] filesArray = a.removeDuplicates(files.split("(?!^)"));
        for(int i=0;i<matrix.matrixLength();i++){
            if(Objects.equals(matrix.getEmployees().get(i).getName(),name)){
                employee+=matrix.getEmployees().get(i).getName();
                for(int n=0;n<matrix.getEmployees().get(i).getFileNames().size();n++){
                    for(int m=0;m<filesArray.length;m++){
                        if(Objects.equals(matrix.getEmployees().get(i).getFileNames().get(n),filesArray[m])){
                            accessibleFiles+=matrix.getEmployees().get(i).getFileNames().get(n)+" ";
                        }
                    }
                }
            }
        }
        if(Objects.equals(employee,"")){
            output="User with name \""+name+"\" cannot be found.";
        }else{
            if(Objects.equals(accessibleFiles,"")){
                output="User \""+employee+"\" have no access to listed file(s)";
            }else{
                output="User \""+employee+"\" have access to file(s) \""+accessibleFiles+"\"";
            }
        }
        return output;
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