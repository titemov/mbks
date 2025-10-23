import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class FileWorker {

    public void writeInFile(Matrix matrix){
        int len = matrix.matrixLength();
        String[] names = new String[len];
        String[] files = new String[len];
        //if user have no files -> he has " " file (empty file)
        for(int i=0;i<len;i++){
            String tempString="";
            String[] employeeFiles = matrix.getEmployees().get(i).getFileNames().getAllFileNames();
            names[i]=(matrix.getEmployees().get(i)).getName();
            for (int n = 0; n < employeeFiles.length; n++) {
                tempString += employeeFiles[n];
            }

            if(Objects.equals(tempString,"")){
                files[i]=" ";
            }else {
                files[i] = tempString;
            }
        }

        try(FileWriter writer = new FileWriter("Matrix.txt", false))
        {
            for(int i=0;i<len;i++){
                String text = names[i]+"-"+files[i];
                writer.write(text);
                writer.append('\n');
                writer.flush();
            }
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<String> parseSubjects(){
        ArrayList<String> result = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader("Matrix.txt")))
        {
            //чтение построчно
            String s;
            while((s=br.readLine())!=null){
                result.add(s.split("-")[0]);
                System.out.println(s);
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
            return null;
        }
        return result;
    }

    public ArrayList<String> parseObjects(){
        ArrayList<String> result = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader("Matrix.txt")))
        {
            //чтение построчно
            String s;
            while((s=br.readLine())!=null){
                //переделать
                String temp="";
                try {
                    temp = s.split("-")[1];
                }catch (ArrayIndexOutOfBoundsException e){
                    System.out.println(e+": this user have no file accesses.");
                }
                if(Objects.equals(temp,"")){
                    result.add("");
                }else {
                    result.add(s.split("-")[1]);
                }
                System.out.println(s);
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
            return null;
        }
        return result;
    }
}
