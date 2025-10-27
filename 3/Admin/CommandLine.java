import java.util.Objects;

public class CommandLine {
    private String userInput;
    private static FileNames allFileNames;

    public CommandLine(String userInput, FileNames allFileNames){
        this.userInput = userInput;
        CommandLine.allFileNames = allFileNames;
        //maybe make Backend.matrix? - nah
    }

    private String grantCommandHandler(String args){
        //grant			[subjects]	[objects]	-> grants given set of subjects access to given set of objects
        String output="";
        try {
            Backend a = new Backend();
            String subjects="";
            String objects=args.split(" ")[args.split(" ").length-1];
            String[] temp=args.split(" ");
            for(int i=0;i<temp.length-1;i++){
                subjects+=temp[i]+" ";
            }
            //System.out.println("Subjects: "+subjects);
            //System.out.println("Objects: "+objects);
            //mode=0 - "Grant"; mode=1 - "Take";
            if(a.changeButtonHandler(0,subjects,objects,true)==0) {
                output = "Access to file(s) " + objects + " GRANTED to employee(s) " + subjects;
            }else throw new Exception();
        } catch (Exception e) {
            output = "Error! Incorrect input";
        }
        return output;
    }
    private String createCommandHandler(String args){
        //create subject [objects]	-> creates new subject with access to given set of objects.
        // If subject exists - just grant access. If object(s) doesnt exist - create new.
        String output="";
        try {
            Backend a = new Backend();
            String subjects="";
            String objects=args.split(" ")[args.split(" ").length-1];
            String[] temp=args.split(" ");
            for(int i=0;i<temp.length-1;i++){
                subjects+=temp[i]+" ";
            }

            //mode=0 -subjects
            if(a.addButtonHandler(0,subjects)!=0){
                throw new Exception();
            }
            //add objects; mode=1 - objects
            if(a.addButtonHandler(1,objects)!=0){
                throw new Exception();
            }
            //mode=0 - "Grant"; mode=1 - "Take";
            if(a.changeButtonHandler(0,subjects,objects,true)==0) {
                output = "Access to file(s) " + objects + " GRANTED to employee(s) " + subjects;
            }else throw new Exception();
        } catch (Exception e) {
            output = "Error! Incorrect input";
        }
        return output;
    }
    private String removeCommandHandler(String args){
        //remove		[subjects]	[objects]	-> removes given set of subkects access to given set of objects
        String output="";
        try {
            Backend a = new Backend();
            String subjects="";
            String objects=args.split(" ")[args.split(" ").length-1];
            String[] temp=args.split(" ");
            for(int i=0;i<temp.length-1;i++){
                subjects+=temp[i]+" ";
            }

            //mode=0 - "Grant"; mode=1 - "Take";
            if(a.changeButtonHandler(1,subjects,objects,true)==0) {
                output = "Access to file(s) " + objects + " REMOVED from employee(s) " + subjects;
            }else throw new Exception();
        } catch (Exception e) {
            output = "Error! Incorrect input";
        }
        return output;
    }
    private String grantAllCommandHandler(String args){
        //grant_all		[subjects]				-> grants given set of subjects access to all objects
        String output="";
        try {
            Backend a = new Backend();
            String subjects="";
            String objects="";
            String[] temp=args.split(" ");
            for(int i=0;i<temp.length;i++){
                subjects+=temp[i]+" ";
            }
            for(int i=0;i< allFileNames.size();i++){
                objects+=allFileNames.get(i);
            }

            //mode=0 - "Grant"; mode=1 - "Take";
            if(a.changeButtonHandler(0,subjects,objects,true)==0) {
                output = "Access to file(s) " + objects + " GRANTED to employee(s) " + subjects;
            }else throw new Exception();
        } catch (Exception e) {
            output = "Error! Incorrect input";
        }
        return output;
    }
    private String removeAllCommandHandler(String args){
        //remove_all	[subjects]				-> removes given set of subjects access to all objects
        String output="";
        try {
            Backend a = new Backend();
            String subjects="";
            String objects="";
            String[] temp=args.split(" ");
            for(int i=0;i<temp.length;i++){
                subjects+=temp[i]+" ";
            }
            for(int i=0;i< allFileNames.size();i++){
                objects+=allFileNames.get(i);
            }

            //mode=0 - "Grant"; mode=1 - "Take";
            if(a.changeButtonHandler(1,subjects,objects,true)==0) {
                output = "Access to file(s) " + objects + " REMOVED from employee(s) " + subjects;
            }else throw new Exception();
        } catch (Exception e) {
            output = "Error! Incorrect input";
        }
        return output;
    }

    public String chooseCommand(){
        /*
        - grant			[subjects]	[objects]	-> grants given set of subjects access to given set of objects
        - create		subject		[objects]	-> creates new subject with access to given set of objects. If subject exists - just grant access. If object(s) doesnt exist - create new.
        - remove		[subjects]	[objects]	-> removes given set of subkects access to given set of objects
        - grant_all		[subjects]				-> grants given set of subjects access to all objects
        - remove_all	[subjects]				-> removes given set of subjects access to all objects
        */
        String userinput=this.userInput;
        String command=userinput.split(" ")[0];
        String args = userinput.split(command+" ")[1];
        System.out.println("Args:"+args);
        System.out.println("Command:"+command);
        String output="";
        if(Objects.equals(command,"grant")) {
            output=grantCommandHandler(args);
        }else{
            if(Objects.equals(command,"create")) {
                output=createCommandHandler(args);
            }else{
                if(Objects.equals(command,"remove")) {
                    output=removeCommandHandler(args);
                }else{
                    if(Objects.equals(command,"grant_all")) {
                        output=grantAllCommandHandler(args);
                    }else{
                        if(Objects.equals(command,"remove_all")) {
                            output=removeAllCommandHandler(args);
                        }else{
                            output = "Error! Incorrect input";
                        }
                    }
                }
            }
        }
        return output;
    }
}
