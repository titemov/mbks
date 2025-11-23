public class Secrecy {
    private String name;
    private int level;

    public Secrecy(String name,int level){
        this.name=name;
        this.level=level;
    }

    public String getName(){
        return this.name;
    }
    public int getLevel(){
        return this.level;
    }
    public void setName(String name){
        this.name=name;
    }
    public void setLevel(int level){
        this.level=level;
    }
}
