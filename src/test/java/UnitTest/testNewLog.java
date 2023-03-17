package UnitTest;

import FTPServer.FTPUtil;

import java.io.IOException;

public class testNewLog {
    public static void main(String[] args) throws IOException {
        int ftpPort = 21;
        String ftpUserName = "qihan";
        String ftpPassword = "zpy010408";
        String ftpHost = "127.0.0.1";
        FTPUtil test = new FTPUtil(ftpHost, ftpPort, ftpUserName, ftpPassword, 2222, "", "");
        //  test.deleteRemoteSingleFile("/home/qihan/DistributedGraphStreamSketch/GSS.dat1");
        test.uploadLocalSingleFile("/home/qihan/DistributedGraphStreamSketch/out.wiki_talk_ht", "src/main/resources/out.wiki_talk_ht");
        test.disconnect();

    }
}
