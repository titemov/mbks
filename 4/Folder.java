public class Folder {
    private String name;
    private int secrecyLevel;
    private String path;

    public Folder(String name,int secrecyLevel,String path){
        this.name=name;
        this.secrecyLevel=secrecyLevel;
        this.path=path;
    }

    public String getName(){
        return this.name;
    }
    public int getSecrecyLevel(){
        return this.secrecyLevel;
    }
    public String getPath(){
        return this.path;
    }
}
