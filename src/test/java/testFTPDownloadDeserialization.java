import GraphStreamSketch.GSS;
import GraphStreamSketch.HashFunction;
import org.apache.commons.net.ftp.FTPClient;

import java.io.*;
import java.util.ArrayList;

public class testFTPDownloadDeserialization {
    public static void main(String[] args) throws IOException {
        FTPClient ftpClient = new FTPClient();
        int ftpPort = 21;
        String ftpUserName = "qihan";
        String ftpPassword = "zpy010408";
        String ftpHost = "127.0.0.1";
        ftpClient.connect(ftpHost, ftpPort);// 连接FTP服务器
        ftpClient.login(ftpUserName, ftpPassword);// 登陆FTP服务器
        // 设置PassiveMode传输
        ftpClient.enterLocalPassiveMode();

        // 设置以二进制流的方式传输
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
        InputStream input =  ftpClient.retrieveFileStream("/home/qihan/DistributedGraphStreamSketch/DGSS/src/main/resources/GSS.dat");
        ftpClient.disconnect();
        OutputStream output = new FileOutputStream("src/main/resources/GSSRemote"+"wsl"+".dat");

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer) )!= -1){
            output.write(buffer,0,bytesRead);
        }
        input.close();
        output.flush();
        output.close();



        GSS testGSS = null;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("src/main/resources/GSSRemote"+"wsl"+".dat"))) {
            testGSS = (GSS) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
      //  System.out.println(testGSS.IPaddress);
        System.out.print("begin: ");
        System.out.print("\n");


        System.out.print("Predicted: =========");
        System.out.print("\n");


        ArrayList<String> testIDS1 = new ArrayList<String>();
        testGSS.nodeSuccessorQuery("1", testIDS1);
        System.out.print("test nodeSuccessorQuery:");
        System.out.print("\n");
        for (String it : testIDS1) {
            System.out.print(it);
            System.out.print("\n");
        }
        System.out.print("test nodeSuccessorQuery end");
        System.out.print("\n");


        System.out.print(testGSS.edgeQuery("1", "2"));//3
        System.out.print("\n");
        System.out.print(testGSS.query("1", "2"));//true
        System.out.print("\n");

        System.out.print(testGSS.edgeQuery("3", "2"));//1
        System.out.print("\n");
        System.out.print(testGSS.query("3", "2"));//true
        System.out.print("\n");

        System.out.print(testGSS.edgeQuery("1", "7"));//0
        System.out.print("\n");
        System.out.print(testGSS.query("1", "7"));//true
        System.out.print("\n");

        testGSS.cleanupBuffer();

        System.out.print("end.");
        System.out.print("\n");
    }
}
