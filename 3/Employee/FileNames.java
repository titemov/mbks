import java.util.ArrayList;
import java.util.Collections;
public class FileNames {
    private ArrayList<String> fileNames = new ArrayList<>();

    public void addFiles(String[] array){
        if(array.length==0) return;
        Collections.addAll(this.fileNames, array);
        this.fileNames=Backend.removeArrayListDuplicates(this.fileNames);
    }
    public int size(){
        return this.fileNames.size();
    }
    public String get(int index){
        return this.fileNames.get(index);
    }
    public void clearFileNames(){
        this.fileNames=new ArrayList<>();
    }
}
