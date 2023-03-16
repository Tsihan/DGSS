package IntegrationTest;

import FTPServer.FTPUtil;
import GraphStreamSketch.DataBackup;

import java.io.IOException;

public class finalTest {
    public static void main(String[] args) throws IOException {
        int dataNodeNum = 5;
        String[] dataNodeIps = {"127.0.0.1", "47.108.220.140", "47.108.80.65", "47.108.30.249", "47.108.175.71"};
        ;
        String[] dataNodeFileNames = {"src/main/resources/out.wiki_talk_ht1", "src/main/resources/out.wiki_talk_ht2",
                "src/main/resources/out.wiki_talk_ht3", "src/main/resources/out.wiki_talk_ht4",
                "src/main/resources/out.wiki_talk_ht5"};

        String masterFileName = "src/main/resources/out.wiki_talk_ht";
        DataBackup test = new DataBackup(dataNodeNum, dataNodeIps, dataNodeFileNames, masterFileName);
        //====产生备份后的数据====
        test.generateBackUpData();

        //====将每一份备份数据传送到远程服务器====

        //wsl
        int ftpPort1 = 21;
        String ftpUserName1 = "qihan";
        String ftpPassword1 = "zpy010408";
        String ftpHost1 = "127.0.0.1";
        FTPUtil testFTPUtil1 = new FTPUtil(ftpHost1, ftpPort1, ftpUserName1, ftpPassword1);

        testFTPUtil1.uploadLocalSingleFile("/home/qihan/DistributedGraphStreamSketch/DGSS/src/main/resources/out.wiki_talk_ht1",
                "src/main/resources/out.wiki_talk_ht1");


        //GDB4
        int ftpPort2 = 21;
        String ftpUserName2 = "ecs-user";
        String ftpPassword2 = "Zpy010408";
        String ftpHost2 = "47.108.220.140";
        FTPUtil testFTPUtil2 = new FTPUtil(ftpHost2, ftpPort2, ftpUserName2, ftpPassword2);
        testFTPUtil2.uploadLocalSingleFile("./DGSS/src/main/resources/out.wiki_talk_ht2",
                "src/main/resources/out.wiki_talk_ht2");

        //GDB3
        int ftpPort3 = 21;
        String ftpUserName3 = "ecs-user";
        String ftpPassword3 = "Zpy010408";
        String ftpHost3 = "47.108.80.65";
        FTPUtil testFTPUtil3 = new FTPUtil(ftpHost3, ftpPort3, ftpUserName3, ftpPassword3);
        testFTPUtil3.uploadLocalSingleFile("./DGSS/src/main/resources/out.wiki_talk_ht3",
                "src/main/resources/out.wiki_talk_ht3");

        //GDB2
        int ftpPort4 = 21;
        String ftpUserName4 = "ecs-user";
        String ftpPassword4 = "Zpy010408";
        String ftpHost4 = "47.108.30.249";
        FTPUtil testFTPUtil4 = new FTPUtil(ftpHost4, ftpPort4, ftpUserName4, ftpPassword4);
        testFTPUtil4.uploadLocalSingleFile("./DGSS/src/main/resources/out.wiki_talk_ht4",
                "src/main/resources/out.wiki_talk_ht4");

        //GDB1
        int ftpPort5 = 21;
        String ftpUserName5 = "ecs-user";
        String ftpPassword5 = "Zpy010408";
        String ftpHost5 = "47.108.175.71";
        FTPUtil testFTPUtil5 = new FTPUtil(ftpHost5, ftpPort5, ftpUserName5, ftpPassword5);
        testFTPUtil5.uploadLocalSingleFile("./DGSS/src/main/resources/out.wiki_talk_ht5",
                "src/main/resources/out.wiki_talk_ht5");

        //todo====接收来自至少超过半数的节点通知已收到备份数据并成功完成DGSS，将GSS序列化到服务器
        //todo  这里要在FPTServer上面添加新的方法


        //todo====获取全部已经完成的GSS序列化对象，并将其反序列化到内存

        //todo====去重后进行指标获取（不需要获得全部的GSS，超过半数即可）


    }
}
