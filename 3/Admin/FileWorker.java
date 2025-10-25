import java.io.*;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Objects;

public class FileWorker {

    public void writeInFile(Matrix matrix){
        int len = matrix.matrixLength();
        String[] names = new String[len];
        String[] files = new String[len];
        //if user have no files -> he has "" file (empty file)
        for(int i=0;i<len;i++){
            String tempString="";
            String[] employeeFiles = matrix.getEmployees().get(i).getFileNames().getAllFileNames();
            names[i]=(matrix.getEmployees().get(i)).getName();
            for (int n = 0; n < employeeFiles.length; n++) {
                tempString += employeeFiles[n];
            }

//            if(Objects.equals(tempString,"")){
//                files[i]=" ";
//            }else {
//                files[i] = tempString;
//            }
            files[i]=tempString;
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

    public ArrayList<String> parseSubjects(String path){
        ArrayList<String> result = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(path)))
        {
            //чтение построчно
            String s;
            while((s=br.readLine())!=null){
                result.add(s.split("-")[0]);
                System.out.println("Subject: "+s);
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
            //чтение построчно
            String s;
            while((s=br.readLine())!=null){
                String temp="";
                try {
                    temp = s.split("-")[1];
                    System.out.println("Objects: "+temp);
                    //if(Objects.equals(temp," ")) throw new ArrayIndexOutOfBoundsException("Space is not valid file name");
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
                System.out.println("Read line: "+s);
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
            return null;
        }
        return result;
    }

    public String exportMatrix(Matrix matrix){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
        String timestamp = now.format(formatter);
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

        String name = "Matrix_"+timestamp+".txt";

        try(FileWriter writer = new FileWriter(name, false))
        {
            for(int i=0;i<len;i++){
                String text = names[i]+"-"+files[i];
                writer.write(text);
                writer.append('\n');
                writer.flush();
            }
            return name;
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
        return null;
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
