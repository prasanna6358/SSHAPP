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

            InputStream in = channel.getInputStream();
            channel.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            // StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null)
            {
                //sb.append(line);
                //  sb.append("\n");
                System.out.println(line);
            }
            //System.out.println(sb.toString());
            channel.setInputStream(null);
            sftpchannel.exit();
            channel.disconnect();
            session.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
