/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sysswproject;

import java.io.*;
import java.util.Arrays;
import org.apache.commons.csv.*;

/**
 *
 * @author ntu-user
 */
public class MainClass {

    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_GREEN = "\u001b[32m";
    public static final String ANSI_BLUE = "\u001B[34m";

    static Session session;

    public static void main(String[] args) throws IOException, InterruptedException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Console console = System.console();
        String input;

        while (true) {
            System.out.print(ANSI_GREEN + "prompt" + ANSI_BLUE + "~" + ANSI_WHITE + "$ ");
            input = reader.readLine();
            input = input.trim();
            String[] command = input.split(" ");

            if (input.equals("")) {
                continue;
            }

            if (command[0].equals("login")) {
                switch (command.length) {
                    case 1 -> {
                        System.out.print("Enter username: ");
                        String tempUname = reader.readLine();
                        Boolean isSuper;
                        if (console != null) {
                            char[] tempPassHiddenChars = console.readPassword("Enter password: ");
                            
                            String tempPassHidden = new String(tempPassHiddenChars);

                            Boolean[] loginCheck = login(tempUname, tempPassHidden);

                            if (loginCheck[0]) {
                                session = new Session(tempUname, loginCheck[1]);
                                session.Start();
                            } else {
                                System.out.println("Error: Wrong username or password!");
                            }
                        } else {
                            System.out.print("Enter password: ");
                            String tempPass = reader.readLine();

                            Boolean[] loginCheck = login(tempUname, tempPass);

                            if (loginCheck[0]) {
                                session = new Session(tempUname, loginCheck[1]);
                                session.Start();
                            } else {
                                System.out.println("Error: Wrong username or password!");
                            }
                        }
                    }
                    case 2 -> {
                        if (command[1].equals("--help")) {
                            System.out.println("""
                                               Usage:
                                                    login:
                                                        Enter the required information.""");
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
                continue;
            }

            System.out.println("Error! " + command[0] + " is not a valid command!");
        }
    }

    public static Boolean[] login(String uname, String pass) throws FileNotFoundException, IOException {
        Boolean[] out = new Boolean[]{false, false};

        Reader in = new FileReader("users.csv");
        Iterable<CSVRecord> users = CSVFormat.DEFAULT.withHeader("username", "password", "type").parse(in);
        for (CSVRecord user : users) {
            if (user.get("username").equals(uname) && user.get("password").equals(pass)) {

                out = new Boolean[]{true, Boolean.parseBoolean(user.get("type"))};

                return out;
            }
        }

        return out;
    }
}
