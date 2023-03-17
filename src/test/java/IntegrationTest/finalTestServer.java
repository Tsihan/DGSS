package IntegrationTest;


import GraphStreamSketch.GSS;
import GraphStreamSketch.HashFunction;

import java.io.*;
import java.net.UnknownHostException;

//服务器读取相应的备份图数据并且序列化GSS对象到服务器本地
public class finalTestServer {
    public static void main(String[] args) throws IOException {
        /*初始化GSS测试参数*/
        // the side length of matrix
        int width = 200;
        // the length of hash addtress list
        int range = 20;
        // the number of candidate bucekt
        int p_num = 4;
        // the number of rooms 不能超过room的最大限制
        int size = 2;
        // finger print lenth
        int f_num = 8;
        // whether to use hash table
        boolean usehashtable = true;
        // the size of the table
        int TableSize = 200;

        GSS testGSS = new GSS(width, range, p_num, size, f_num, usehashtable, TableSize, HashFunction.hashfunctions.BOB1);
        //不同的服务器节点这里需要对文件名进行修改
        // "/home/qihan/DistributedGraphStreamSketch/DGSS/src/main/resources/out.wiki_talk_ht1"
        String fileName = "/home/ecs-user/DGSS/src/main/resources/out.wiki_talk_ht5";

        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);

        String line;
        //拆分过后，在服务器上面运行可以不需要跳过第一行
        //  int count = -1;

        while ((line = br.readLine()) != null) {
//            count++;
//            //跳过首行
//            if (count == 0) {
//                continue;
//            }
            String[] temp = line.split("\\s+");


            testGSS.insert(temp[0], temp[1], Integer.parseInt(temp[2]));

        }
        br.close();


        //将testGSS的内容全部写到GSS.dat文件中去
        // "/home/qihan/DistributedGraphStreamSketch/DGSS/src/main/resources/GSS.dat"
        testGSS.serialize("/home/ecs-user/DGSS/src/main/resources/GSS.dat");
        System.out.println(testGSS.IPaddress);
    }
}
