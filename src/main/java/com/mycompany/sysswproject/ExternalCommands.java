package com.mycompany.sysswproject;

import java.io.*;

/**
 *
 * @author Sattik
 */
public class ExternalCommands {

    CommandHistory cmdHis;

    File homeDir = new File(System.getProperty("user.dir"));

    public ExternalCommands(CommandHistory cmdHis) {
        this.cmdHis = cmdHis;
    }

    public void useExternalCommand(String[] command, ProcessBuilder pb) throws InterruptedException {
        try {
            switch (command.length) {
                case 1 ->
                    pb.command(command[0]);
                case 2 ->
                    pb.command(command[0], command[1]);
                case 3 ->
                    pb.command(command[0], command[1], command[2]);
                case 4 ->
                    pb.command(command[0], command[1], command[2], command[3]);
                default ->
                    System.out.println("Error: Command use is incorrect.");
            }
            Process p = pb.start();
            var ret = p.waitFor();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String s;

            while ((s = br.readLine()) != null) {
                System.out.println(s);
            }

            if (ret != 0) {
                if (command[0].matches("rmdir") && command.length == 2) {
                    System.out.println(command[0] + ": file not found");
                    br.close();
                    return;
                }
                System.out.println(command[0] + ": missing or too many operand");
                System.out.println("Try '" + command[0] + " --help' for more information.");
                br.close();
                return;
            }

            cmdHis.addToHistory(command);
            br.close();
        } catch (IOException e) {
            System.out.println("Error! " + command[0] + " is not a valid command!");
        }
    }
}
