package FTPServer;

import java.io.*;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class FTPUtil {
    public Logger logger = LogManager.getLogger(FTPUtil.class);

    public String FtpHost;
    public int FtpPort;
    public String FtpUserName;
    public String FtpPassword;
    public FTPClient FtpClient;

    public int SSHPort;
    // "/home/ecs-user/DGSS/src/main/resources/GSS.dat"
    // "/home/qihan/DistributedGraphStreamSketch/DGSS/src/main/resources/GSS.dat"

    public String DeserializedRemoteFileName;
    // "src/main/resources/GSSremote.dat"
    public String DeserializedLocalFileName;

    public String ShellCmd;
    //  /usr/bin/env /usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java -cp /tmp/cp_b4wz4y2kn5fq5krih5odh69ml.jar IntegrationTest.finalTestServer

    //通过测试
    public FTPUtil(String ftpHost, int ftpPort, String ftpUserName, String ftpPassword, int sshPort,
                   String deserializedRemoteFileName, String deserializedLocalFileName, String shellCmd) {
        FtpHost = ftpHost;
        FtpPort = ftpPort;
        FtpUserName = ftpUserName;
        FtpPassword = ftpPassword;
        SSHPort = sshPort;
        DeserializedRemoteFileName = deserializedRemoteFileName;
        DeserializedLocalFileName = deserializedLocalFileName;
        ShellCmd = shellCmd;
        try {
            FtpClient = new FTPClient();
            FtpClient.connect(ftpHost, ftpPort);// 连接FTP服务器
            FtpClient.login(ftpUserName, ftpPassword);// 登陆FTP服务器
            if (!FTPReply.isPositiveCompletion(FtpClient.getReplyCode())) {
                logger.info("未连接到FTP,用户名或密码错误!");
                FtpClient.disconnect();
            } else {
                logger.info("FTP连接成功!");
            }
        } catch (SocketException e) {
            e.printStackTrace();
            logger.info("FTP的IP地址可能错误，请正确配置。");
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("FTP的端口错误,请正确配置。");
        }

    }

    //通过测试
    public FTPUtil(String ftpHost, int ftpPort, String ftpUserName, String ftpPassword, int sshPort,
                   String deserializedRemoteFileName, String deserializedLocalFileName) {
        FtpHost = ftpHost;
        FtpPort = ftpPort;
        FtpUserName = ftpUserName;
        FtpPassword = ftpPassword;
        SSHPort = sshPort;
        DeserializedRemoteFileName = deserializedRemoteFileName;
        DeserializedLocalFileName = deserializedLocalFileName;

        try {
            FtpClient = new FTPClient();
            FtpClient.connect(ftpHost, ftpPort);// 连接FTP服务器
            FtpClient.login(ftpUserName, ftpPassword);// 登陆FTP服务器
            if (!FTPReply.isPositiveCompletion(FtpClient.getReplyCode())) {
                logger.info("未连接到FTP,用户名或密码错误!");
                FtpClient.disconnect();
            } else {
                logger.info("FTP连接成功!");
            }
        } catch (SocketException e) {
            e.printStackTrace();
            logger.info("FTP的IP地址可能错误，请正确配置。");
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("FTP的端口错误,请正确配置。");
        }

    }

    //完成测试 不用添加log
    public void disconnect() throws IOException {
        // 设置PassiveMode传输
        FtpClient.disconnect();


    }

    //完成测试
    public void deleteRemoteSingleFile(String remoteAbsoluteFileName) throws IOException {
        // 设置PassiveMode传输
        FtpClient.enterLocalPassiveMode();

        // 设置以二进制流的方式传输
        FtpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
        //删除远程文件
        FtpClient.deleteFile(remoteAbsoluteFileName);
    }

    //通过测试
    public void downloadRemoteSingleFile(String remoteAbsoluteFileName, String localFileName) throws IOException {
        // 设置PassiveMode传输
        FtpClient.enterLocalPassiveMode();

        // 设置以二进制流的方式传输
        FtpClient.setFileType(FTPClient.ASCII_FILE_TYPE);
        InputStream input = FtpClient.retrieveFileStream(remoteAbsoluteFileName);
        OutputStream output = new FileOutputStream(localFileName);

        byte[] buffer = new byte[1024];
        int bytesRead;
        try {
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            input.close();
            output.flush();
            output.close();

        } catch (NullPointerException e) {
            e.printStackTrace();
            logger.info("未正确读取远程文件内容，请检查远程路径是否有误！");
        }


    }

    //通过测试
    public boolean uploadLocalSingleFile(String remoteAbsoluteFileName, String localFileName) throws IOException {
        // 设置PassiveMode传输
        FtpClient.enterLocalPassiveMode();
        // FtpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
        // 设置以二进制流的方式传输
        FtpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
        //编码方式为utf-8
        FtpClient.setControlEncoding("UTF-8");

        InputStream input = new FileInputStream(localFileName);

        //  System.out.println(FtpClient.printWorkingDirectory());

        //FtpClient.logout();

        if (FtpClient.storeFile(remoteAbsoluteFileName, input)) {
            logger.info("本地文件上传成功！");

            return true;
        } else {
            logger.info("本地文件上传失败！");
            logger.info(FtpClient.getReplyString());
            input.close();
            System.out.println(this.FtpHost);
            System.out.println(FtpClient.getReplyString());
            return false;
        }


    }


}





