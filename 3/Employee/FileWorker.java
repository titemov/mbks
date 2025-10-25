import java.io.*;
import java.util.ArrayList;

public class FileWorker {

    public ArrayList<String> parseSubjects(String path){
        ArrayList<String> result = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(path)))
        {
            String s;
            while((s=br.readLine())!=null){
                result.add(s.split("-")[0]);
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
            return null;
        }
        return result;
    }

    public ArrayList<String> parseObjects(String path){
        //must include ONLY english letters (no numbers and etc.) - done
        ArrayList<String> result = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(path)))
        {
            String s;
            while((s=br.readLine())!=null){
                String temp="";
                try {
                    temp = s.split("-")[1];
                    String[] splitted = temp.split("(?!^)");
                    ArrayList<String> tempEngOnly = new ArrayList<>();
                    for(int i=0;i<splitted.length;i++){
                        if((splitted[i]+splitted[i]).matches("^[a-zA-Z][a-zA-Z\\s]+$")){
                            tempEngOnly.add(splitted[i]);
                        }
                    }
                    temp="";
                    for(int i=0; i<tempEngOnly.size();i++){
                        temp+=tempEngOnly.get(i);
                    }
                    result.add(temp);
                }catch (ArrayIndexOutOfBoundsException e){
                    System.out.println(e+": this user have no file accesses.");
                    result.add("");
                }
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
            return null;
        }
        return result;
    }

    public int importMatrix(String path){
        try(BufferedReader br = new BufferedReader(new FileReader(path)))
        {
            //чтение строки
            String s;
            if((s=br.readLine())!=null){
                System.out.println("read");
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
            return 1;
        }
        return 0;
    }
}
