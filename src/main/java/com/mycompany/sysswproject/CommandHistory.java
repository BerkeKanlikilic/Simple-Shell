/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sysswproject;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sattik
 */
public class CommandHistory {
    private final List<String> history = new ArrayList<>();
    
    public void addToHistory(String[] stringArray) {
        history.add(ConnectStringArray(stringArray, " "));
    }

    public void readHistory() {
        for (int i = 0; i < history.size(); i++) {
            System.out.println(history.get(i));
        }
    }

    private String ConnectStringArray(String[] command, String connectionChar) {
        var commandSingleString = String.join(connectionChar, command);
        return commandSingleString;
    }
}
