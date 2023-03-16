import FTPServer.FTPUtil;
import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;

public class testFTPDelete {
    public static void main(String[] args) throws IOException {
        //===============旧版测试
       // FTPClient ftpClient = new FTPClient();
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
//        //删除远程文件
//        ftpClient.deleteFile("/home/qihan/GraphStreamSketch4GraduationProject/wiki_talk_ht/test.txt");

        //===================新版测试
        FTPUtil testFTPUtil = new FTPUtil(ftpHost,ftpPort,ftpUserName,ftpPassword);
        testFTPUtil.deleteRemoteSingleFile("/home/qihan/DistributedGraphStreamSketch/GSS.dat");
        testFTPUtil.disconnect();
    }
}
