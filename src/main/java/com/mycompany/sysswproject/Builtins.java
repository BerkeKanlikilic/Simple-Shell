package com.mycompany.sysswproject;

import java.io.*;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.apache.commons.csv.*;

/**
 *
 * @author Sattik
 */
public class Builtins {

    File homeDir = new File(System.getProperty("user.home"));
    CommandHistory cmdHis;
    String user;

    private CSVPrinter csvPrinter;

    public Builtins(CommandHistory cmdHis, String user) {
        this.cmdHis = cmdHis;
        this.user = user;
    }

    void addUser(String[] command) throws IOException {
        switch (command.length) {
            case 2 -> {
                cmdHis.addToHistory(command);

                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                Console console = System.console();
                Boolean isSuper;

                System.out.print("Enter username: ");
                String tempUname = reader.readLine();

                Reader in = new FileReader("users.csv");
                Iterable<CSVRecord> users = CSVFormat.DEFAULT.withHeader("username", "password", "type").parse(in);
                for (CSVRecord user : users) {
                    if (user.get("username").matches(tempUname)) {
                        System.out.println("That username already exists!");
                        return;
                    }
                }

                if (console != null) {
                    char[] tempPassHidden = console.readPassword("Enter password: ");
                    Arrays.fill(tempPassHidden, ' ');

                    System.out.print("Is user is a super type(Y/N): ");
                    String type = reader.readLine().toUpperCase();
                    if (type.equals("Y")) {
                        type = "true";
                    } else if (type.equals("N")) {
                        type = "false";
                    } else {
                        System.out.println("Answer not recognised. Please try again.");
                        return;
                    }

                    BufferedWriter writer = Files.newBufferedWriter(
                            Paths.get("users.csv"),
                            StandardOpenOption.APPEND,
                            StandardOpenOption.CREATE);

                    csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);

                    csvPrinter.printRecord(tempUname, tempPassHidden, type);
                    csvPrinter.flush();
                    csvPrinter.close();

                    System.out.println("User " + tempUname + " is successfully added.");
                } else {
                    System.out.print("Enter password: ");
                    String tempPass = reader.readLine();

                    System.out.print("Is user is a super type(Y/N): ");
                    String type = reader.readLine().toUpperCase();
                    if (type.equals("Y")) {
                        type = "true";
                    } else if (type.equals("N")) {
                        type = "false";
                    } else {
                        System.out.println("Answer not recognised. Please try again.");
                        return;
                    }

                    BufferedWriter writer = Files.newBufferedWriter(
                            Paths.get("users.csv"),
                            StandardOpenOption.APPEND,
                            StandardOpenOption.CREATE);

                    csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);

                    csvPrinter.printRecord(tempUname, tempPass, type);
                    csvPrinter.flush();
                    csvPrinter.close();

                    System.out.println("User " + tempUname + " is successfully added.");
                }
            }
            default -> {
                System.out.println(command[0] + ": missing or too many operand");
                System.out.println("Try '" + command[0] + " --help' for more information.");
            }
        }

    }

    void delUser(String[] command) throws IOException {

        switch (command.length) {
            case 2 -> {
                cmdHis.addToHistory(command);

                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                Scanner x;

                System.out.print("Enter username: ");
                String removeName = reader.readLine();

                String tempFile = "temp.csv";
                File oldFile = new File("users.csv");
                File newFile = new File(tempFile);
                String uname = "";
                String pass = "";
                String type = "";

                FileWriter fw = new FileWriter(tempFile, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter(bw);
                x = new Scanner(new File("users.csv"));
                x.useDelimiter("[,\n]");

                while (x.hasNext()) {
                    uname = x.next();
                    pass = x.next();
                    type = x.next();
                    if (!uname.equals(removeName)) {
                        pw.println(uname + "," + pass + "," + type);
                    } else {
                        System.out.println("Successfully removed user " + removeName + ".");
                    }
                }

                x.close();
                pw.flush();
                pw.close();
                oldFile.delete();
                File dump = new File("users.csv");
                newFile.renameTo(dump);
            }
            default -> {
                System.out.println(command[0] + ": missing or too many operand");
                System.out.println("Try '" + command[0] + " --help' for more information.");
            }
        }

    }

    void chPass(String[] command) throws IOException {
        switch (command.length) {
            case 1 -> {
                cmdHis.addToHistory(command);

                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                Scanner x;
                String searchName = "";

                System.out.print("Enter new password: ");
                String newPass = reader.readLine();

                String tempFile = "temp.csv";
                File oldFile = new File("users.csv");
                File newFile = new File(tempFile);
                String uname = "";
                String pass = "";
                String type = "";

                FileWriter fw = new FileWriter(tempFile, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter(bw);
                x = new Scanner(new File("users.csv"));
                x.useDelimiter("[,\n]");

                while (x.hasNext()) {
                    uname = x.next();

                    if (!uname.equals(user)) {
                        pass = x.next();
                    } else {
                        pass = newPass;
                        x.next();
                        System.out.println("Successfully changed the password of " + user + ".");
                    }

                    type = x.next();
                    pw.println(uname + "," + pass + "," + type);
                }

                x.close();
                pw.flush();
                pw.close();
                oldFile.delete();
                File dump = new File("users.csv");
                newFile.renameTo(dump);
            }
            case 2 -> {
                cmdHis.addToHistory(command);

                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                Scanner x;
                String searchName = "";
                
                Boolean foundName = false;

                System.out.print("Enter username: ");
                searchName = reader.readLine();

                Reader in = new FileReader("users.csv");
                Iterable<CSVRecord> users = CSVFormat.DEFAULT.withHeader("username", "password", "type").parse(in);
                for (CSVRecord user : users) {
                    if (user.get("username").equals(searchName)) {
                        foundName = true;
                    }
                }

                if(!foundName){
                    System.out.println("Error: Username not found!");
                    return;
                }
                
                System.out.print("Enter new password: ");
                String newPass = reader.readLine();

                String tempFile = "temp.csv";
                File oldFile = new File("users.csv");
                File newFile = new File(tempFile);
                String uname = "";
                String pass = "";
                String oldPass = "";
                String type = "";

                FileWriter fw = new FileWriter(tempFile, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter(bw);
                x = new Scanner(new File("users.csv"));
                x.useDelimiter("[,\n]");

                while (x.hasNext()) {
                    uname = x.next();

                    if (!uname.equals(searchName)) {
                        pass = x.next();
                    } else {
                        pass = newPass;
                        oldPass = x.next();
                        if(newPass.equals(oldPass)){
                            System.out.println("Password cannot be the same with the old one.");
                        } else {
                            System.out.println("Successfully changed the password of " + searchName + ".");
                        }
                    }

                    type = x.next();
                    pw.println(uname + "," + pass + "," + type);

                }

                x.close();
                pw.flush();
                pw.close();
                oldFile.delete();
                File dump = new File("users.csv");
                newFile.renameTo(dump);
            }
            default -> {
                System.out.println(command[0] + ": missing or too many operand");
                System.out.println("Try '" + command[0] + " --help' for more information.");
            }
        }
    }

    void chUserType(String[] command) throws IOException {

        switch (command.length) {
            case 2 -> {
                cmdHis.addToHistory(command);

                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                Scanner x;
                String searchName = "";

                System.out.print("Enter username: ");
                searchName = reader.readLine();

                System.out.print("New user type is (standard/super): ");
                String newType = reader.readLine();

                if (newType.equals("super")) {
                    newType = "true";
                } else if (newType.equals("standard")) {
                    newType = "false";
                } else {
                    System.out.println("Invalid type. User types are 'standar' and 'super'.");
                    return;
                }

                String tempFile = "temp.csv";
                File oldFile = new File("users.csv");
                File newFile = new File(tempFile);
                String uname = "";
                String pass = "";
                String type = "";

                FileWriter fw = new FileWriter(tempFile, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter(bw);
                x = new Scanner(new File("users.csv"));
                x.useDelimiter("[,\n]");

                while (x.hasNext()) {
                    uname = x.next();
                    pass = x.next();

                    if (!uname.equals(searchName)) {
                        type = x.next();
                    } else {
                        type = newType;
                        x.next();
                        System.out.println("Successfully changed the type of " + searchName + ".");
                    }

                    pw.println(uname + "," + pass + "," + type);
                }

                x.close();
                pw.flush();
                pw.close();
                oldFile.delete();
                File dump = new File("users.csv");
                newFile.renameTo(dump);
            }
            default -> {
                System.out.println(command[0] + ": missing or too many operand");
                System.out.println("Try '" + command[0] + " --help' for more information.");
            }
        }
    }

    void history(String[] command) {
        switch (command.length) {
            case 1 -> {
                cmdHis.readHistory();
                cmdHis.addToHistory(command);
            }
            case 2 -> {
                if (command[1].equals("--help")) {
                    cmdHis.addToHistory(command);
                    System.out.println("""
                                           Usage:
                                                history:
                                                    Shows all the previously used successful commands.""");
                } else {
                    System.out.println(command[0] + ": missing or too many operand");
                    System.out.println("Try '" + command[0] + " --help' for more information.");
                }
            }
            default -> {
                System.out.println(command[0] + ": missing or too many operand");
                System.out.println("Try '" + command[0] + " --help' for more information.");
            }
        }
    }

    void cd(String[] command, ProcessBuilder pb) {
        switch (command.length) {
            case 1 -> {
                // cd
                pb.directory(homeDir);
                cmdHis.addToHistory(command);
            }
            case 2 -> {
                // cd ..
                if (command[1].equals("..")) {
                    cmdHis.addToHistory(command);
                    if (pb.directory().getParentFile() != null) {
                        File parentDir = new File(pb.directory().getParent());
                        pb.directory(parentDir);
                    }
                } else if (command[1].equals("--help")) {
                    cmdHis.addToHistory(command);
                    System.out.println("""
                                       Usage:
                                            cd:
                                                Change directory to user folder.

                                            cd <directory name>:
                                                Change the current directory to specified directory.

                                            cd ..:
                                                Change directory to the parent of current directory.""");
                } // cd [dir] 
                else {
                    String dir = command[1];
                    File newDir = new File(pb.directory() + File.separator + dir);
                    if (newDir.exists() && newDir.isDirectory()) {
                        cmdHis.addToHistory(command);
                        pb.directory(newDir);
                    } else {
                        System.out.println("cd: No such file or directory named " + command[1]);
                    }
                }
            }
            default -> {
                System.out.println(command[0] + ": missing or too many operand");
                System.out.println("Try '" + command[0] + " --help' for more information.");
            }
        }
    }

    void showDir(String[] command, ProcessBuilder pb) {
        switch (command.length) {
            case 1 -> {
                cmdHis.addToHistory(command);
                System.out.println(pb.directory());
            }
            case 2 -> {
                if (command[1].equals("--help")) {
                    cmdHis.addToHistory(command);
                    System.out.println("""
                                       Usage:
                                            showDir:
                                                Show the current directory.""");
                    return;
                }

                System.out.println(command[0] + ": missing or too many operand");
                System.out.println("Try '" + command[0] + " --help' for more information.");
            }
            default -> {
                System.out.println(command[0] + ": missing or too many operand");
                System.out.println("Try '" + command[0] + " --help' for more information.");
            }
        }
    }

    void whoAmI(String[] command) {
        switch (command.length) {
            case 1 -> {
                cmdHis.addToHistory(command);
                System.out.println(user);
            }
            case 2 -> {
                if (command[1].equals("--help")) {
                    cmdHis.addToHistory(command);
                    System.out.println("""
                                       Usage:
                                            whoAmI:
                                                Shows the current user.""");
                    return;
                }

                System.out.println(command[0] + ": missing or too many operand");
                System.out.println("Try '" + command[0] + " --help' for more information.");
            }
            default -> {
                System.out.println(command[0] + ": missing or too many operand");
                System.out.println("Try '" + command[0] + " --help' for more information.");
            }
        }
    }

    public void useBuiltinCommand(String[] command, ProcessBuilder pb) throws IOException {

        if (command[0].equals("super")) {
            if (command.length == 2) {
                if (command[1].equals("addUser")) {
                    this.addUser(command);
                    return;
                }

                if (command[1].equals("delUser")) {
                    this.delUser(command);
                    return;
                }

                if (command[1].equals("chPass")) {
                    this.chPass(command);
                    return;
                }

                if (command[1].equals("chUserType")) {
                    this.chUserType(command);
                    return;
                }

                if (command[1].equals("--help")) {
                    cmdHis.addToHistory(command);
                    System.out.println("""
                                       Usage:
                                            super:
                                                Only valid for 'super' users. Allows user to use super commands.""");
                    return;
                }

                System.out.println("Error! " + command[1] + " is not a valid super command!");
                return;
            } else {
                System.out.println(command[0] + ": missing or too many operand");
                System.out.println("Try '" + command[0] + " --help' for more information.");
                return;
            }
        }

        if (command[0].equals("chPass")) {
            this.chPass(command);
            return;
        }

        if (command[0].equals("history")) {
            this.history(command);
            return;
        }

        // Command: cd
        if (command[0].equals("cd")) {
            this.cd(command, pb);
            return;
        }

        if (command[0].equals("showDir")) {
            this.showDir(command, pb);
            return;
        }

        if (command[0].equals("whoAmI")) {
            this.whoAmI(command);
            return;
        }

        System.out.println("Error! " + command[0] + " is not a valid command!");
    }
}
