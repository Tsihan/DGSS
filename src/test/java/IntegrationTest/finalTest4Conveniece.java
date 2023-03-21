package IntegrationTest;

import FTPServer.FTPUtil;
import GraphStreamSketch.ListGraph;
import ResultEvaluation.EvaluationResultGetter;
import SSHServer.SSHUtil;

import java.io.IOException;
import java.util.ArrayList;

public class finalTest4Conveniece {
    public static void main(String[] args) throws IOException {

        //wsl
        int ftpPort1 = 21;
        int sshPort1 = 2222;
        String ftpUserName1 = "qihan";
        String ftpPassword1 = "zpy010408";
        String ftpHost1 = "127.0.0.1";
        FTPUtil testFTPUtil1 = new FTPUtil(ftpHost1, ftpPort1, ftpUserName1, ftpPassword1, sshPort1,
                "/home/qihan/DistributedGraphStreamSketch/DGSS/src/main/resources/out.lkml-replySetResult",
                "src/main/resources/GSSremote1.TestSetResult",
                "/usr/bin/env /usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java -cp /tmp/cp_b4wz4y2kn5fq5krih5odh69ml.jar IntegrationTest.finalTestServerTxt");


        //GDB4
        int ftpPort2 = 21;
        int sshPort2 = 22;
        String ftpUserName2 = "ecs-user";
        String ftpPassword2 = "Zpy010408";
        String ftpHost2 = "47.108.220.140";
        FTPUtil testFTPUtil2 = new FTPUtil(ftpHost2, ftpPort2, ftpUserName2, ftpPassword2, sshPort2,
                "/home/ecs-user/DGSS/src/main/resources/out.lkml-replyTestSetResult",
                "src/main/resources/GSSremote2.TestSetResult",
                "/usr/bin/env /usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java -cp /tmp/cp_1s29d2ag01vnqg4wunvgt7atn.jar IntegrationTest.finalTestServerTxt");


        //GDB3
        int ftpPort3 = 21;
        int sshPort3 = 22;
        String ftpUserName3 = "ecs-user";
        String ftpPassword3 = "Zpy010408";
        String ftpHost3 = "47.108.80.65";
        FTPUtil testFTPUtil3 = new FTPUtil(ftpHost3, ftpPort3, ftpUserName3, ftpPassword3, sshPort3,
                "/home/ecs-user/DGSS/src/main/resources/out.lkml-replyTestSetResult",
                "src/main/resources/GSSremote3.TestSetResult",
                "/usr/bin/env /usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java -cp /tmp/cp_1s29d2ag01vnqg4wunvgt7atn.jar IntegrationTest.finalTestServerTxt");


        //GDB2
        int ftpPort4 = 21;
        int sshPort4 = 22;
        String ftpUserName4 = "ecs-user";
        String ftpPassword4 = "Zpy010408";
        String ftpHost4 = "47.108.30.249";
        FTPUtil testFTPUtil4 = new FTPUtil(ftpHost4, ftpPort4, ftpUserName4, ftpPassword4, sshPort4,
                "/home/ecs-user/DGSS/src/main/resources/out.lkml-replyTestSetResult",
                "src/main/resources/GSSremote4.TestSetResult",
                "/usr/bin/env /usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java -cp /tmp/cp_1s29d2ag01vnqg4wunvgt7atn.jar IntegrationTest.finalTestServerTxt");

        //  /home/ecs-user/DGSS/src/main/resources/out.wiki_talk_ht
        //GDB1
        int ftpPort5 = 21;
        int sshPort5 = 22;
        String ftpUserName5 = "ecs-user";
        String ftpPassword5 = "Zpy010408";
        String ftpHost5 = "47.108.175.71";
        FTPUtil testFTPUtil5 = new FTPUtil(ftpHost5, ftpPort5, ftpUserName5, ftpPassword5, sshPort5,
                "/home/ecs-user/DGSS/src/main/resources/out.lkml-replyTestSetResult",
                "src/main/resources/GSSremote5.TestSetResult",
                "/usr/bin/env /usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java -cp /tmp/cp_1s29d2ag01vnqg4wunvgt7atn.jar IntegrationTest.finalTestServerTxt");


        ArrayList<FTPUtil> uploadedFTPUtils = new ArrayList<>(3);
        uploadedFTPUtils.add(testFTPUtil1);
        //  uploadedFTPUtils.add(testFTPUtil2);
        uploadedFTPUtils.add(testFTPUtil3);
        //  uploadedFTPUtils.add(testFTPUtil4);
        uploadedFTPUtils.add(testFTPUtil5);


        //==== 由于这个是读文本文件的版本，不再需要 获取全部已经完成的GSS序列化对象，并将其反序列化到内存
        //====先测试能否将服务器上产生的预测值和数据下载到本地
//        for (int i = 0; i < 5; i++) {
//            //将远程序列化对象文件下载到本地
//            FTPUtil temp = uploadedFTPUtils.get(i);
//            temp.downloadRemoteSingleFile(temp.DeserializedRemoteFileName, temp.DeserializedLocalFileName);
//
//
//        }


        ListGraph testListGraph = new ListGraph();


        EvaluationResultGetter.getAllDistributedResultsTxt(uploadedFTPUtils, testListGraph, "src/main/resources/out.lkml-reply", 5);
    }

}
