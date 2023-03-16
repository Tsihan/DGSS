import FTPServer.FTPUtil;
import org.apache.commons.net.ftp.FTPClient;

import java.io.*;

public class testFTPDownload {
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
//        InputStream input =  ftpClient.retrieveFileStream("/home/qihan/GraphStreamSketch4GraduationProject/wiki_talk_ht/meta.wiki_talk_ht");
//        OutputStream output = new FileOutputStream("src/main/resources/test.txt");
//
//        byte[] buffer = new byte[1024];
//        int bytesRead;
//        while ((bytesRead = input.read(buffer) )!= -1){
//            output.write(buffer,0,bytesRead);
//        }
//        input.close();
//        output.flush();
//        output.close();

        //===================新版测试
        FTPUtil testFTPUtil = new FTPUtil(ftpHost,ftpPort,ftpUserName,ftpPassword);
        testFTPUtil.downloadRemoteSingleFile("/home/qihan/DistributedGraphStreamSketch/GSS.dat","src/main/resources/GSSremote.dat");
        testFTPUtil.disconnect();

    }
}
