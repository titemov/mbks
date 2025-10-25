import java.util.ArrayList;

public class Matrix {
    private ArrayList<Employee> employees = new ArrayList<>();

    public ArrayList<Employee> getEmployees(){
        return employees;
    }

    public void addEmployee(Employee employee){
        this.employees.add(employee);
    }

    public int matrixLength(){
        return employees.size();
    }

    public void clearEmployees(){
        this.employees=new ArrayList<>();
    }

}
