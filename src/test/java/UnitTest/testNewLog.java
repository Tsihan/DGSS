package UnitTest;

import FTPServer.FTPUtil;

import java.io.IOException;

public class testNewLog {
    public static void main(String[] args) throws IOException {
        int ftpPort = 21;
        String ftpUserName = "qihan";
        String ftpPassword = "zpy010408";
        String ftpHost = "127.0.0.1";
        FTPUtil test = new FTPUtil(ftpHost,ftpPort,ftpUserName,ftpPassword);
      //  test.deleteRemoteSingleFile("/home/qihan/DistributedGraphStreamSketch/GSS.dat1");
        test.uploadLocalSingleFile("/home/qihan/DistributedGraphStreamSketch/GSS.dat1","src/main/resources/GSSremote.dat");
        test.disconnect();

    }
}
