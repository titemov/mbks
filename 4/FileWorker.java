import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileWorker {
    public void writeSecrecy(ArrayList<Secrecy> secrecies){
        try(FileWriter writer = new FileWriter("secrecies.txt", false))
        {
            for(int i=0;i<secrecies.size();i++){
                String text = secrecies.get(i).getName()+"-"+secrecies.get(i).getLevel();
                writer.write(text);
                writer.append('\n');
                writer.flush();
            }
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<Secrecy> parseSecrecies(String path){
        ArrayList<Secrecy> result = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(path)))
        {
            //чтение построчно
            String s;
            while((s=br.readLine())!=null){
                String name = s.split("-")[0];
                int level = Integer.parseInt(s.split("-")[1]);
                result.add(new Secrecy(name,level));
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
            return null;
        }
        return result;

    }

    public void writeFolder(ArrayList<Folder> folders){
        try(FileWriter writer = new FileWriter("folders.txt", false))
        {
            for(int i=0;i<folders.size();i++){
                String text = folders.get(i).getName()+"-"+folders.get(i).getSecrecyLevel()+"-"+folders.get(i).getPath();
                writer.write(text);
                writer.append('\n');
                writer.flush();
            }
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<Folder> parseFolders(String path){
        ArrayList<Folder> result = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(path)))
        {
            //чтение построчно
            String s;
            while((s=br.readLine())!=null){
                String name = s.split("-")[0];
                int secrecyLevel = Integer.parseInt(s.split("-")[1]);
                String folderPath = s.split("-")[2];
                result.add(new Folder(name,secrecyLevel,folderPath));
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
            return null;
        }
        return result;

    }
}
