package UnitTest;

import FTPServer.FTPUtil;
import org.apache.commons.net.ftp.FTPClient;

import java.io.*;

public class testFTPUpload {
    public static void main(String[] args) throws IOException {
        //===================旧版测试
//        FTPClient ftpClient = new FTPClient();
        //默认端口21
        int ftpPort = 21;
        String ftpUserName = "qihan";
        String ftpPassword = "zpy010408";
        String ftpHost = "127.0.0.1";
//        ftpClient.connect(ftpHost, ftpPort);// 连接FTP服务器
//        ftpClient.login(ftpUserName, ftpPassword);// 登陆FTP服务器
//        // 设置PassiveMode传输
//        ftpClient.enterLocalPassiveMode();
//
//        // 设置以二进制流的方式传输
//        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
//        //编码方式为utf-8
//        ftpClient.setControlEncoding("UTF-8");
//
//        InputStream input =  new FileInputStream("src/main/resources/test.txt") ;
//
//        ftpClient.storeFile("/home/qihan/GraphStreamSketch4GraduationProject/wiki_talk_ht/test.txt",input);
//        input.close();

        //===================新版测试
        FTPUtil testFTPUtil = new FTPUtil(ftpHost,ftpPort,ftpUserName,ftpPassword);
        testFTPUtil.uploadLocalSingleFile("/home/qihan/DistributedGraphStreamSketch/GSS.dat","src/main/resources/GSS.dat");
    }
}
