import java.io.*;
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
}
