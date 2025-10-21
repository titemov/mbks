import java.util.ArrayList;
import java.util.Collections;

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
    public String[] getAllFileNames(){
        String[] allFileNames = new String[this.fileNames.size()];
        for(int i=0;i<this.fileNames.size();i++){
            allFileNames[i]=this.fileNames.get(i);
        }
        return allFileNames;
    }
}
