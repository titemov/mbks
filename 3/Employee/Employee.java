public class Employee {
    private String name;
    private FileNames fileNames = new FileNames();

    public Employee(String name, String[] files){
        this.name=name;
        if(files!=null) {
            this.fileNames.addFiles(files);
        }else{
            this.fileNames.addFiles(new String[]{""});
        }
    }

    public String getName(){
        return this.name;
    }

    public FileNames getFileNames(){
        return this.fileNames;
    }
}
