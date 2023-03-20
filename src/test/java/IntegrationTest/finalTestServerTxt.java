package IntegrationTest;

import GraphStreamSketch.GSS;
import GraphStreamSketch.HashFunction;
import org.apache.lucene.util.RamUsageEstimator;
import org.apache.lucene.util.ToStringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import static ResultEvaluation.EvaluationResultGetter.getDeduplicatedEdges;
import static ResultEvaluation.EvaluationResultGetter.getDeduplicatedNodes;

public class finalTestServerTxt {
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

        //==============进行查询====================
        String fileNameTestSet = "/home/ecs-user/DGSS/src/main/resources/out.wiki_talk_htTestSet";
        FileInputStream fisTestSet = new FileInputStream(fileNameTestSet);
        InputStreamReader isrTestSet = new InputStreamReader(fisTestSet);
        BufferedReader brTestSet = new BufferedReader(isrTestSet);

        //根据不同的数据节点需要进行调整
        FileOutputStream fosTestSet = new FileOutputStream("/home/ecs-user/DGSS/src/main/resources/out.wiki_talk_htTestSetResult");

        int Num4DeduplicatedEdges = getDeduplicatedEdges(fileName).size();

        int Num4BufferedEdges = testGSS.buffer.size();

        System.out.println(testGSS.IPaddress);
        fosTestSet.write(("IPaddress" + "\r\n").getBytes(StandardCharsets.UTF_8));
        fosTestSet.write((testGSS.IPaddress + "\r\n").getBytes(StandardCharsets.UTF_8));

        System.out.println("======buffer usage=====");
        System.out.println(Num4DeduplicatedEdges);
        System.out.println(Num4BufferedEdges);
        System.out.println((float) Num4BufferedEdges / (float) Num4DeduplicatedEdges);
        fosTestSet.write(("BufferUsage" + "\r\n").getBytes(StandardCharsets.UTF_8));
        fosTestSet.write(((float) Num4BufferedEdges / (float) Num4DeduplicatedEdges + "\r\n").getBytes(StandardCharsets.UTF_8));


        System.out.println("======Memory Cost=====");
        String size4gss = RamUsageEstimator.humanSizeOf(testGSS);
        long size4gss1 = RamUsageEstimator.sizeOf(testGSS);


        System.out.println(size4gss);

        fosTestSet.write(("MemoryCost" + "\r\n").getBytes(StandardCharsets.UTF_8));
        fosTestSet.write((size4gss1 + "\r\n").getBytes(StandardCharsets.UTF_8));

        System.out.println("======AP=====");
        Set<String> DeduplicatedNodes = getDeduplicatedNodes(fileNameTestSet);

        fosTestSet.write(("NodePredicatedValues" + "\r\n").getBytes(StandardCharsets.UTF_8));
        int count = 0;
        for (String node : DeduplicatedNodes) {

            int predicateValue = testGSS.nodeDegreeQuery(node, 0);
            fosTestSet.write((predicateValue + "\t").getBytes(StandardCharsets.UTF_8));
            //为保持规整
            if (count == 10) {
                fosTestSet.write(("\r\n").getBytes(StandardCharsets.UTF_8));
            }
            count++;


        }


        System.out.println("======ARE=====");
        Set<String> DeduplicatedEdges = getDeduplicatedEdges(fileNameTestSet);
        fosTestSet.write(("EdgePredicatedValues" + "\r\n").getBytes(StandardCharsets.UTF_8));
        count = 0;
        for (String pair : DeduplicatedEdges) {
            String[] edges = pair.split("\\s+");
            float predicateValue = testGSS.edgeQuery(edges[0], edges[1]);
            fosTestSet.write((predicateValue + "\t").getBytes(StandardCharsets.UTF_8));
            //为保持规整
            if (count == 10) {
                fosTestSet.write(("\r\n").getBytes(StandardCharsets.UTF_8));
            }
            count++;

        }


    }


    //将testGSS的内容全部写到GSS.dat文件中去
    // "/home/qihan/DistributedGraphStreamSketch/DGSS/src/main/resources/GSS.dat"
//        testGSS.serialize("/home/ecs-user/DGSS/src/main/resources/GSS.dat");

}



