package com.halcyon.mule.webServ;

import com.jcraft.jsch.*;
import org.apache.commons.io.IOUtils;

import java.io.*;


public class IpServiceClient {

    String host, user, password;

    String sourceFilePath = "/home/java/Downloads/";
    String sourceFileName = "get-en-dash-18122017.ps1";
    //String executeCommandPrefix = "powershell -f C:\\Users\\caspexh.EC2AMAZ-S42432S\\";
    String executeCommandPrefix = "powershell -f %temp%\\";
    //String executeCommandPrefix = "powershell -f ";
    // first I'm connecting to the client machine
    // creating a file in client machine
    // executing command with command1 as command
    // getting respose as a stream
   
    public void setHostUserPassword()
    {
//      host = "192.168.10.123";
//      user = "";
//      password = "";
        host = "52.53.232.50";
        user = "";
        password = "";
    }

    public void getSSHConnection() {
        setHostUserPassword();
        try {
            //1. Connecting to the client machine
            Session session = getSession();

            System.out.println("Connected");

            //executeScript(session, "powershell Get-ChildItem Env:temp");
            //String tempFolder = executeScript(session, "powershell $env:temp").toString();
//            System.out.println("***"+tempFolder+"***");
//            Streams streams = executeScriptWithStreams(session, "powershell $env:temp");
            Streams streams = executeScriptWithStreams(session, "powershell");
            System.out.println("111");
            OutputStream outputStream = streams.getOutputStream();
            System.out.println("112");
            writeToStream(outputStream, "powershell");
            System.out.println("113");
            String output = readStream(streams.getInputStream()).toString();
            System.out.println("114");
            System.out.println(output);
            System.out.println("115");
//            ChannelSftp sftpchannel = uploadFile(session, tempFolder);
            //executeScript(session, executeCommandPrefix+sourceFileName);
            //sftpchannel.exit();
            streams.getChannel().disconnect();
            System.out.println("116");
            session.disconnect();
            System.out.println("117");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeToStream(OutputStream outputStream, String content) {
        PrintWriter p = new PrintWriter(outputStream);
        p.println(content);
        p.flush();
    }

    private StringBuilder executeScript(Session session, String command) throws JSchException, IOException {
        Channel channel = session.openChannel("exec");
        //3. Setting command with command1 as command
        ((ChannelExec) channel).setCommand(command);
        ((ChannelExec) channel).setErrStream(System.err);

        //4. Getting response as a stream
        InputStream in = channel.getInputStream();
        channel.connect();

        StringBuilder sb = readStream(in);
        //System.out.println(sb.toString());
        channel.setInputStream(null);
        channel.disconnect();
        return sb;
    }

    private StringBuilder readStream(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line;
        System.out.println("Entered readStream 211");
        while ((line = br.readLine()) != null)
        {
            System.out.println("Entered readStream 212");
            System.out.println(line);
            sb.append(line);
        }
        return sb;
    }

    private String readStreamApache(InputStream in) throws IOException {
        String theString = IOUtils.toString(in);
        return theString;
    }
    //this will execute the command and give us the streams to run
    //the user should close the channel by calling Streams.getChannel().disconnect()
    private Streams executeScriptWithStreams(Session session, String command) throws JSchException, IOException {
        Channel channel = session.openChannel("exec");
        //3. Setting command with command1 as command
        //((ChannelExec) channel).setCommand(command);
        ((ChannelExec) channel).setErrStream(System.err);

        Streams streams = new Streams();
        //4. Getting response as a stream
        InputStream in = channel.getInputStream();
        OutputStream out = channel.getOutputStream();
        channel.connect();

        streams.setInputStream(in);
        streams.setOutputStream(out);
        streams.setChannel(channel);
        return streams;
    }

    private ChannelSftp uploadFile(Session session, String destFolder) throws JSchException, SftpException, FileNotFoundException {
        ChannelSftp sftpchannel = (ChannelSftp)session.openChannel("sftp"); // getting the SFTC channel

        sftpchannel.connect();
        String realPath = sftpchannel.realpath("/");
        System.out.println(realPath);
//        String pwd = sftpchannel.getHome();
//        System.out.println(pwd);
        //2. Creating a file in client machine
        File f = new File(sourceFilePath+sourceFileName);// creating file in client machine
        //"C:\\Users\\caspexh\\"
        sftpchannel.put(new FileInputStream(f), destFolder+"\\"+sourceFileName);
        return sftpchannel;
    }

    private Session getSession() throws JSchException {
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        JSch jsch = new JSch();
        Session session = jsch.getSession(user, host, 22);
        session.setPassword(password);
        session.setConfig(config);
        session.connect();

        return session;
    }
}
