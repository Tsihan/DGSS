import GraphStreamSketch.GSS;
import GraphStreamSketch.HashFunction;

import java.io.*;

public class testGSSTime {
    public static void main(String[] args) throws IOException {
        /*初始化GSS测试参数*/
        // the side length of matrix
        int width = 30000;
        // the length of hash address list
        int range = 40;
        // the number of candidate bucket
        int p_num = 8;
        // the number of rooms 不能超过room的最大限制
        int size = 2;
        // finger print lenth
        int f_num = 8;
        // whether to use hash table
        boolean usehashtable = true;
        // the size of the table
        int TableSize = 2000;

        GSS testGSS = new GSS(width, range, p_num, size, f_num, usehashtable, TableSize, HashFunction.hashfunctions.BOB1);

        //  String fileName = "src/main/resources/out.wiki_talk_ht";
        //换更大的数据集
        String fileName = "src/main/resources/out.wiki_talk_lv";
        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);

        String line;
        int count = -1;

        // 开始时间
        long stime1 = System.currentTimeMillis();


        while ((line = br.readLine()) != null) {
            count++;
            //跳过首行
            if (count == 0) {
                continue;
            }
            String[] temp = line.split("\\s+");
            testGSS.insert(temp[0], temp[1], Integer.parseInt(temp[2]));

        }

        // 结束时间
        long etime1 = System.currentTimeMillis();
        // 计算执行时间
        long gap1 = etime1 - stime1;
        System.out.println("将原始文件通过GSS压缩执行时长为" + gap1 + "毫秒.");


        br.close();
        //输出总共的有效行数
        System.out.println("the number of lines: " + count);

        System.out.println("begin: ");


        System.out.println("Predicted: =========");


//        ArrayList<String> testIDS = new ArrayList<String>();
//        testGSS.nodeSuccessorQuery("1", testIDS);
//        System.out.print("test nodeSuccessorQuery:");
//        System.out.print("\n");
//        for (String it : testIDS)
//        {
//            System.out.print(it);
//            System.out.print("\n");
//        }
//        System.out.print("test nodeSuccessorQuery end");
//        System.out.print("\n");


        // 开始时间
        long stime2 = System.currentTimeMillis();
        System.out.println(testGSS.edgeQuery("31", "2"));//6
        // 结束时间
        long etime2 = System.currentTimeMillis();
        // 计算执行时间
        long gap2 = etime2 - stime2;
        System.out.println("查询某一特定边的权重时间为" + gap2 + "毫秒.");


        // 开始时间
        long stime3 = System.currentTimeMillis();
        System.out.println(testGSS.query("31", "2"));//true 1
        // 结束时间
        long etime3 = System.currentTimeMillis();
        // 计算执行时间
        long gap3 = etime3 - stime3;
        System.out.println("查询两点间是否可达时间为" + gap3 + "毫秒.");


        System.out.println(testGSS.edgeQuery("18", "7"));//1

        System.out.println(testGSS.query("18", "7"));//true 1


        System.out.println(testGSS.edgeQuery("42", "726"));//6

        System.out.println(testGSS.query("42", "726"));//true 1


        testGSS.cleanupBuffer();

        System.out.println("end.");


    }
}
