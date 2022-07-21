/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sysswproject;

/**
 *
 * @author Sattik
 */

import com.sysswproject.core.Shell;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class HTTPserver {
    private final int PORT = 2222;

    public static void main(String[] args) {
        EchoHTTPServer gtp = new EchoHTTPServer();
        gtp.start();
    }

    public void run() {
        try {
            Shell shell = new Shell();
            ServerSocket server = new ServerSocket(PORT);
            System.out.println("Echo Server listening port: " + PORT);
            boolean shudown = true;

            while (shudown) {

                Socket socket = server.accept();

                InputStream is = socket.getInputStream();
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(is));
                String line;
                line = in.readLine();
                String auxLine = line;
                line = "";
                // looks for post data
                int postDataI = -1;
                while ((line = in.readLine()) != null && (line.length() != 0)) {
                    System.out.println(line);
                    if (line.indexOf("Content-Length:") > -1) {
                        postDataI = new Integer(line
                                .substring(
                                        line.indexOf("Content-Length:") + 16,
                                        line.length())).intValue();
                    }
                }
                String postData = "";
                for (int i = 0; i < postDataI; i++) {
                    int intParser = in.read();
                    postData += (char) intParser;
                }
                // replace + by " "
                int index = postData.indexOf('+');
                while (index > -1) {

                    postData = postData.substring(0, index) + ' ' + postData.substring(index + 1);
                    index = postData.indexOf('+');
                }

                String readTmpLine = postData.replace("user=", "");
                String[] readTmpArgs = readTmpLine.split("\s+");

                List<String> writeLine = shell.useCommand(readTmpArgs);


                out.println("HTTP/1.0 200 OK");
                out.println("Content-Type: text/html; charset=utf-8");
                out.println("Server: MINISERVER");
                // this blank line signals the end of the headers
                out.println("");
                // Send the HTML page
                out.println("<H1>Systems Software - Echo server</H1>");
                out.println("");
                out.println("<H3 style="margin-left:5px">Shell:</H3>");
                for(String arg : writeLine){
                    out.println("<p style="margin-left:5px">" + arg + "</p>");
                }
                out.println("<form name="input" action="imback" method="post">");
                out.println("Input: <input type="text" name="user"><input type="submit" value="Submit"></form>");

                //if your get parameter contains shutdown it will shutdown
                if (auxLine.indexOf("?shutdown") > -1) {
                    shudown = false;
                }
                out.flush();
                out.close();
                socket.close();
            }
            server.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
