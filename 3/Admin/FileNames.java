import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class FileNames {
    private ArrayList<String> fileNames = new ArrayList<>();
    public void addFiles(String[] array){
        Collections.addAll(this.fileNames, array);
    }
    public int size(){
        return this.fileNames.size();
    }
    public String get(int index){
        return this.fileNames.get(index);
    }
    public String remove(String name){
        //String removed = get(index);
        //.remove(index) method returns object that it deleted
        for(int i=0;i<this.fileNames.size();i++){
            if(Objects.equals(this.fileNames.get(i),name)){
                return this.fileNames.remove(i);
            }
        }
        return null;
    }

    public String[] getAllFileNames(){
        String[] allFileNames = new String[this.fileNames.size()];
        for(int i=0;i<this.fileNames.size();i++){
            allFileNames[i]=this.fileNames.get(i);
        }
        return allFileNames;
    }
}
