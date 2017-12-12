package com.halcyon.mule.webServ;

import com.jcraft.jsch.*;

import java.io.*;


public class IpServiceClient {

    
    // first I'm connecting to the client machine
    // creating a file in client machine
    // executing command with command1 as command
    // getting respose as a stream
   
    
    public static void getSSHConnection() {
        String host = "ipaddress";
        String user = "username";
        String password = "password";
        String command1 = "pwsh -f get-en-dash.ps1";
        try {
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, 22);
            session.setPassword(password);
            session.setConfig(config);
            session.connect();
            System.out.println("Connected");

            ChannelSftp sftpchannel = (ChannelSftp)session.openChannel("sftp"); // getting the SFTC channel
            sftpchannel.connect();
            File f = new File("/home/prasanna/wastage/get-en-dash.ps1");// creating file in client machine
            sftpchannel.put(new FileInputStream(f), f.getName());

            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command1);

            ((ChannelExec) channel).setErrStream(System.err);

            InputStream in = channel.getInputStream();// get command output as a stream
            channel.connect();
            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0)
                        break;
                    System.out.print(new String(tmp, 0, i));
                }
                if (channel.isClosed()) {
                    System.out.println("exit-status: " + channel.getExitStatus());
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception ee) {
                }
            }
            channel.disconnect();
            session.disconnect();
            System.out.println("DONE");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
