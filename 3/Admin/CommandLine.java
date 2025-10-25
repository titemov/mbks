public class CommandLine {
    private String userInput;
    private static Matrix matrix;
    private static FileNames allFileNames;

    public CommandLine(String userInput, Matrix matrix, FileNames allFileNames){
        this.userInput = userInput;
        CommandLine.matrix = matrix;
        CommandLine.allFileNames = allFileNames;
        //maybe make Backend.matrix?
    }

    public void chooseCommand(){
        String userinput=this.userInput;
    }

}
