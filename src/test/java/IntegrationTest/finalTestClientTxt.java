package IntegrationTest;

import FTPServer.FTPUtil;
import GraphStreamSketch.DataBackup;
import GraphStreamSketch.GSS;
import GraphStreamSketch.ListGraph;
import ResultEvaluation.EvaluationResultGetter;
import SSHServer.SSHUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class finalTestClientTxt {

    public static void main(String[] args) throws IOException {
        int dataNodeNum = 5;
        int copyNum = dataNodeNum / 2 + (dataNodeNum % 2 != 0 ? 1 : 0);
        String[] dataNodeIps = {"127.0.0.1", "47.108.220.140", "47.108.80.65", "47.108.30.249", "47.108.175.71"};

        String[] dataNodeFileNames = {"src/main/resources/out.wiki_talk_ht1", "src/main/resources/out.wiki_talk_ht2",
                "src/main/resources/out.wiki_talk_ht3", "src/main/resources/out.wiki_talk_ht4",
                "src/main/resources/out.wiki_talk_ht5"};

        String masterFileName = "src/main/resources/out.wiki_talk_ht";

        //修正之后将测试集同样上传到服务器
        String testSetFileName ="src/main/resources/out.wiki_talk_htTestSet";
        DataBackup test = new DataBackup(dataNodeNum, dataNodeIps, dataNodeFileNames, masterFileName);
        //====产生备份后的数据====
        test.generateBackUpData();

        //====将每一份备份数据传送到远程服务器====


        //wsl
        int ftpPort1 = 21;
        int sshPort1 = 2222;
        String ftpUserName1 = "qihan";
        String ftpPassword1 = "zpy010408";
        String ftpHost1 = "127.0.0.1";
        FTPUtil testFTPUtil1 = new FTPUtil(ftpHost1, ftpPort1, ftpUserName1, ftpPassword1, sshPort1,
                "/home/qihan/DistributedGraphStreamSketch/DGSS/src/main/resources/GSS.TestSetResult",
                "src/main/resources/GSSremote1.TestSetResult",
                "/usr/bin/env /usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java -cp /tmp/cp_b4wz4y2kn5fq5krih5odh69ml.jar IntegrationTest.finalTestServer");


        //GDB4
        int ftpPort2 = 21;
        int sshPort2 = 22;
        String ftpUserName2 = "ecs-user";
        String ftpPassword2 = "Zpy010408";
        String ftpHost2 = "47.108.220.140";
        FTPUtil testFTPUtil2 = new FTPUtil(ftpHost2, ftpPort2, ftpUserName2, ftpPassword2, sshPort2,
                "/home/ecs-user/DGSS/src/main/resources/GSS.TestSetResult",
                "src/main/resources/GSSremote2.TestSetResult",
                "/usr/bin/env /usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java -cp /tmp/cp_1s29d2ag01vnqg4wunvgt7atn.jar IntegrationTest.finalTestServer");


        //GDB3
        int ftpPort3 = 21;
        int sshPort3 = 22;
        String ftpUserName3 = "ecs-user";
        String ftpPassword3 = "Zpy010408";
        String ftpHost3 = "47.108.80.65";
        FTPUtil testFTPUtil3 = new FTPUtil(ftpHost3, ftpPort3, ftpUserName3, ftpPassword3, sshPort3,
                "/home/ecs-user/DGSS/src/main/resources/GSS.TestSetResult",
                "src/main/resources/GSSremote3.TestSetResult",
                "/usr/bin/env /usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java -cp /tmp/cp_1s29d2ag01vnqg4wunvgt7atn.jar IntegrationTest.finalTestServer");


        //GDB2
        int ftpPort4 = 21;
        int sshPort4 = 22;
        String ftpUserName4 = "ecs-user";
        String ftpPassword4 = "Zpy010408";
        String ftpHost4 = "47.108.30.249";
        FTPUtil testFTPUtil4 = new FTPUtil(ftpHost4, ftpPort4, ftpUserName4, ftpPassword4, sshPort4,
                "/home/ecs-user/DGSS/src/main/resources/GSS.TestSetResult",
                "src/main/resources/GSSremote4.TestSetResult",
                "/usr/bin/env /usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java -cp /tmp/cp_1s29d2ag01vnqg4wunvgt7atn.jar IntegrationTest.finalTestServer");

        //  /home/ecs-user/DGSS/src/main/resources/out.wiki_talk_ht
        //GDB1
        int ftpPort5 = 21;
        int sshPort5 = 22;
        String ftpUserName5 = "ecs-user";
        String ftpPassword5 = "Zpy010408";
        String ftpHost5 = "47.108.175.71";
        FTPUtil testFTPUtil5 = new FTPUtil(ftpHost5, ftpPort5, ftpUserName5, ftpPassword5, sshPort5,
                "/home/ecs-user/DGSS/src/main/resources/GSS.TestSetResult",
                "src/main/resources/GSSremote5.TestSetResult",
                "/usr/bin/env /usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java -cp /tmp/cp_1s29d2ag01vnqg4wunvgt7atn.jar IntegrationTest.finalTestServer");


        //接收来自至少超过半数的节点通知已收到备份数据并成功完成DGSS，将GSS序列化到服务器

        boolean[] uploaded = new boolean[dataNodeNum];
        // String[] uploadedIPAddresses = new String[dataNodeNum];
        ArrayList<FTPUtil> uploadedFTPUtils = new ArrayList<>();


        boolean temp1 = testFTPUtil1.uploadLocalSingleFile("/home/qihan/DistributedGraphStreamSketch/DGSS/src/main/resources/out.wiki_talk_ht1",
                "src/main/resources/out.wiki_talk_ht1");
        boolean temp2 = testFTPUtil1.uploadLocalSingleFile("/home/qihan/DistributedGraphStreamSketch/DGSS/src/main/resources/out.wiki_talk_htTestSet",
                "src/main/resources/out.wiki_talk_htTestSet");
        uploaded[0] = (temp1 && temp2);
        if (uploaded[0]) {

            uploadedFTPUtils.add(testFTPUtil1);
        }

         temp1 = testFTPUtil2.uploadLocalSingleFile("/home/ecs-user/DGSS/src/main/resources/out.wiki_talk_ht2",
                "src/main/resources/out.wiki_talk_ht2");
        temp2 = testFTPUtil2.uploadLocalSingleFile("/home/ecs-user/DGSS/src/main/resources/out.wiki_talk_htTestSet",
                "src/main/resources/out.wiki_talk_htTestSet");
        uploaded[1] = (temp1 && temp2);

//        uploaded[1] = testFTPUtil2.uploadLocalSingleFile("/home/ecs-user/DGSS/src/main/resources/out.wiki_talk_ht2",
//                "src/main/resources/out.wiki_talk_ht2");
        if (uploaded[1]) {

            uploadedFTPUtils.add(testFTPUtil2);
        }


        temp1 = testFTPUtil3.uploadLocalSingleFile("/home/ecs-user/DGSS/src/main/resources/out.wiki_talk_ht3",
                "src/main/resources/out.wiki_talk_ht3");
        temp2 = testFTPUtil3.uploadLocalSingleFile("/home/ecs-user/DGSS/src/main/resources/out.wiki_talk_htTestSet",
                "src/main/resources/out.wiki_talk_htTestSet");
        uploaded[2] = (temp1 && temp2);
        if (uploaded[2]) {

            uploadedFTPUtils.add(testFTPUtil3);
        }


        temp1 = testFTPUtil4.uploadLocalSingleFile("/home/ecs-user/DGSS/src/main/resources/out.wiki_talk_ht4",
                "src/main/resources/out.wiki_talk_ht4");
        temp2 = testFTPUtil4.uploadLocalSingleFile("/home/ecs-user/DGSS/src/main/resources/out.wiki_talk_htTestSet",
                "src/main/resources/out.wiki_talk_htTestSet");
        uploaded[3] = (temp1 && temp2);
        if (uploaded[3]) {

            uploadedFTPUtils.add(testFTPUtil4);
        }

        temp1 = testFTPUtil5.uploadLocalSingleFile("/home/ecs-user/DGSS/src/main/resources/out.wiki_talk_ht5",
                "src/main/resources/out.wiki_talk_ht5");
        temp2 = testFTPUtil5.uploadLocalSingleFile("/home/ecs-user/DGSS/src/main/resources/out.wiki_talk_htTestSet",
                "src/main/resources/out.wiki_talk_htTestSet");
        uploaded[4] = (temp1 && temp2);
        if (uploaded[4]) {
            uploadedFTPUtils.add(testFTPUtil5);
        }
        //如果满足要求
        if (uploadedFTPUtils.size() >= copyNum) {
            for (int i = 0; i < copyNum; i++) {
//                String cmd = "ls -1";
//                 String cmd = "pwd";
                SSHUtil shell = new SSHUtil(uploadedFTPUtils.get(i).FtpHost, uploadedFTPUtils.get(i).FtpUserName, uploadedFTPUtils.get(i).FtpPassword, uploadedFTPUtils.get(i).SSHPort);
//                String execLog = shell.execCommand(cmd);
//                System.out.println(execLog);
            }
        } else {
            System.out.println("已上传节点数不满足要求！");
            System.exit(-1);
        }
        //==========以上的内容全部通过测试==========

        // 新建shell对象，远程执行每一个服务器上的finalTestServer
        for (int i = 0; i < copyNum; i++) {
            FTPUtil temp = uploadedFTPUtils.get(i);
            String cmd = temp.ShellCmd;

            SSHUtil shell = new SSHUtil(temp.FtpHost, temp.FtpUserName, temp.FtpPassword, temp.SSHPort);
            String execLog = shell.execCommand(cmd);
            System.out.println(execLog);
        }


        //==== 由于这个是读文本文件的版本，不再需要 获取全部已经完成的GSS序列化对象，并将其反序列化到内存
//        ArrayList<GSS> GSSs = new ArrayList<>();
//        for (int i = 0; i < copyNum; i++) {
//            //将远程序列化对象文件下载到本地
//            FTPUtil temp = uploadedFTPUtils.get(i);
//            temp.downloadRemoteSingleFile(temp.DeserializedRemoteFileName, temp.DeserializedLocalFileName);
//
//
//            GSS testGSS = null;
//            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(temp.DeserializedLocalFileName))) {
//                testGSS = (GSS) in.readObject();
//            } catch (IOException | ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//
//            GSSs.add(testGSS);
//        }

        //====先测试能否将服务器上产生的预测值和数据下载到本地
        ListGraph testListGraph = new ListGraph();
   //     EvaluationResultGetter.getAllDistributedResults(GSSs,testListGraph,masterFileName,dataNodeNum);




    }
}
