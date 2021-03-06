package com.halcyon.mule;

/**
 * Hello world!
 *
 */
import com.halcyon.mule.webServ.IpServiceClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class App {

    public static void main(String[] args) throws Exception {

        /*String OS_Name = System.getProperty("os.name");
        System.out.println();
        if(OS_Name.equalsIgnoreCase("Linux"))
        {
            System.out.println(OS_Name);
            System.out.println();
            getExceCommand("pwsh", "get-process");
        }*/
        new IpServiceClient().getSSHConnection();
    }

// for executing commands in local machine
     public static void getExceCommand(String OSCommand,String command) throws IOException
      {
        StringBuilder commands = new StringBuilder(OSCommand);
        commands.append(" -Command  "+command);
        Process process= Runtime.getRuntime().exec(commands.toString());
        System.out.println(process.getOutputStream());
        process.getOutputStream().close();
        String line;
        System.out.println("Standard Output:");
        BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
        while ((line = stdout.readLine()) != null) {
            System.out.println(line);
        }
        stdout.close();
        System.out.println();
        System.out.println("Standard Error:");
        BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        while ((line = stderr.readLine()) != null) {
            System.out.println(line);
        }
        stderr.close();
        System.out.println("Done");
    }
}
