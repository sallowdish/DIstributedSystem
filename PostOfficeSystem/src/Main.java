import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/// Main class to handle all business logic

public class Main {
    public static void main(String[] args) throws Exception {
        //local var
        String postOfficeFile = "offices.txt";
        String criminalFile = "wanted.txt";
        String commandFile = "commands.txt";

        if (args.length == 3){
            postOfficeFile = args[0];
            criminalFile = args[1];
            commandFile = args[2];
        }


        //Initialization
        PostOffice.initializeStaticLogs();

        //Read setup info from office.txt
        try {
            Scanner officeInfoReader = new Scanner(new File(postOfficeFile));
            Integer officeCount = officeInfoReader.nextInt();
            for (int i = 0; i < officeCount; i++) {
//                String office = officeInfoReader.nextLine();
//                String officeArgs[] = office.trim().split(" ");
                String postOfficeName = officeInfoReader.next();
                Integer transitTiem = officeInfoReader.nextInt();
                Integer postage = officeInfoReader.nextInt();
                Integer capacity = officeInfoReader.nextInt();
                Integer persuasion = officeInfoReader.nextInt();
                Integer maxLength = officeInfoReader.nextInt();
                PostOffice newInstance = new PostOffice(postOfficeName, transitTiem, postage, capacity, persuasion, maxLength);
            }
            officeInfoReader.close();
        } catch (Exception e) {
            System.err.println("PostOffice Instance set up failed: " + e.getMessage());
            System.exit(-1);
        }

        //Read criminals
        try {

            Scanner criminalInfoReader = new Scanner(new File(criminalFile));
            Integer crinimalCount = Integer.parseInt(criminalInfoReader.nextLine());
            for (int i = 0; i < crinimalCount; i++) {
                String criminal = criminalInfoReader.nextLine();
                PostOffice.criminalNameList.add(criminal);
            }
            criminalInfoReader.close();
        } catch (Exception e) {
            System.err.println("PostOffice criminal name list set up failed: " + e.getMessage());
            System.exit(-1);
        }

        //Read command.txt
        try {
            Scanner commandReader = new Scanner(new File(commandFile));
            Integer cmdCount = Integer.parseInt(commandReader.nextLine());

            for (int i = 0; i < cmdCount; i++) {
                String cmd = commandReader.nextLine();
                System.out.println("Executing: " + cmd);
                PostOffice.invokeCommand(cmd);
            }

            commandReader.close();
        } catch (Exception e) {
            System.err.println("Execute cmd error: " + e.getMessage());
        }

        PostOffice.cleanUp();
    }
}
