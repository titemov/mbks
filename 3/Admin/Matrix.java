import java.util.ArrayList;
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

    public void addFilesToEmployee(String name, String[] files){
        //сравнить имя -> если совпало, то с помощью Collections.addAll(files) добавить все файлы юзеру
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
