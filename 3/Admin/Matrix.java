import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class Matrix {
    private ArrayList<Employee> employees = new ArrayList<>();

    public ArrayList<Employee> getEmployees(){
        return employees;
    }

    public void addEmployee(Employee employee){
        this.employees.add(employee);
    }

    public void removeEmployee(String name){
        for(int i=0;i<this.employees.size();i++){
            if(Objects.equals(this.employees.get(i).getName(),name)){
                this.employees.remove(i);
                break;
            }
        }
    }

    public int addFilesToEmployee(String name, ArrayList<String> files){
        //сравнить имя -> если совпало, то с помощью Collections.addAll(files) добавить все файлы юзеру
        //разобраться с повторами changeAccess(da fa qw +)
        for(int i=0;i<this.employees.size();i++){
            if(Objects.equals(this.employees.get(i).getName(),name)){
                this.employees.get(i).getFileNames().addFiles(files.toArray(new String[0]));
                return 0;
            }
        }
        return -1;
    }

    public int removeFilesFromEmployee(String name, ArrayList<String> files){
        for(int i=0;i<this.employees.size();i++){
            if(Objects.equals(this.employees.get(i).getName(),name)){
                for(int n=0;n<this.employees.get(i).getFileNames().size();n++){
                    for(int m=0;m<files.size();m++){
                        if(Objects.equals(this.employees.get(i).getFileNames().get(n),files.get(m))){
                            this.employees.get(i).getFileNames().removeByIndex(n);
                        }
                    }
                }
                return 0;
            }
        }
        return -1;
    }

    public int matrixLength(){
        return employees.size();
    }

    public String[] getAllNames(){
        String[] result = new String[employees.size()];
        for(int i=0;i<employees.size();i++){
            result[i]=(employees.get(i)).getName();
        }
        return result;
    }

}
