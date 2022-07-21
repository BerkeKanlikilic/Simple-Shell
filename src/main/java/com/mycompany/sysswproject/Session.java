package com.mycompany.sysswproject;

/**
 *
 * @author Sattik
 */
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Session {
    String user;
    Boolean isSuper;
    String currentDirectory;
    String prompt;

    CommandHistory cmdHistory = new CommandHistory();
    
    StringBuffer sb = new StringBuffer();

    String commandLine;
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    ProcessBuilder pb = new ProcessBuilder();
    File homeDir = new File(System.getProperty("user.home"));

    boolean cmdExternal;
    boolean cmdSuper;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001b[32m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BLUE = "\u001B[34m";

    public Session(String user, Boolean isSuper) {
        this.user = user;
        this.isSuper = isSuper;
    }

    public void Start() throws IOException, InterruptedException {
        System.out.println("Successfully logged in. Welcome, " + user + "!");
        pb.directory(homeDir);
        currentDirectory = pb.directory().getAbsolutePath();

        while (true) {
            cmdExternal = false;
            cmdSuper = false;

            // read what the user entered
            System.out.print(getPrompt());
            commandLine = reader.readLine();
            String[] command = commandLine.split(" ");

            // if the user entered a return, just loop again
            if (commandLine.equals("")) {
                continue;
            }

            if (commandLine.startsWith("logoff")) {
                System.out.println("Successfully logged off.");
                break;
            }
            
            if(commandLine.startsWith("super")){
                if(isSuper){
                    Builtins builtins = new Builtins(cmdHistory, user);
                    builtins.useBuiltinCommand(command, pb);
                } else {
                    System.out.println("Access denied. You are not a super user.");
                }
                continue;
            }
            
            String[] superCommandsCheck = "addUser,delUser,chUserType".split(",");
            
            for (int i = 0; i < superCommandsCheck.length; i++) {
                if (commandLine.startsWith(superCommandsCheck[i])) {
                    cmdSuper = true;
                    System.out.println("Error: " + superCommandsCheck[i] + ": Try using 'super " + superCommandsCheck[i] + " ..'.");
                    break;
                }
            }
            
            if(cmdSuper){
                continue;
            }

            // Array of external commands to check
            String[] extCommandsCheck = "ls,cp,mv,mkdir,rmdir,ps,which,clear".split(",");

            // Loop through all external commands to check with the input
            for (int i = 0; i < extCommandsCheck.length; i++) {
                if (commandLine.startsWith(extCommandsCheck[i])) {
                    cmdExternal = true;
                    break;
                }
            }

            // if 
            if (cmdExternal) {
                // Run external command
                ExternalCommands extCommands = new ExternalCommands(cmdHistory);
                extCommands.useExternalCommand(command, pb);
            } else {
                // Run Built in command
                Builtins builtins = new Builtins(cmdHistory, user);
                builtins.useBuiltinCommand(command, pb);
            }
        }
    }

    private String getPrompt() {
        prompt = ANSI_GREEN + user + ANSI_WHITE + ":" + ANSI_BLUE + "~" + pb.directory() + ANSI_WHITE + "$ ";
        return prompt;
    }
}