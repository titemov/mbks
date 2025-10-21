import java.util.ArrayList;

public class Matrix {
    private ArrayList<Employee> employees = new ArrayList<>();

    public void addEmployee(Employee employee){
        this.employees.add(employee);
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
