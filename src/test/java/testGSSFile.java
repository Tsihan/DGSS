import GraphStreamSketch.GSS;
import GraphStreamSketch.HashFunction;

import java.io.*;
import java.util.ArrayList;

public class testGSSFile {
    public static void main(String[] args) throws IOException {
        /*初始化GSS测试参数*/
        // the side length of matrix
        int width = 2000;
        // the length of hash addtress list
        int range = 8;
        // the number of candidate bucekt
        int p_num = 4;
        // the number of rooms 不能超过room的最大限制
        int size = 2;
        // finger print lenth
        int f_num = 12;
        // whether to use hash table
        boolean usehashtable = true;
        // the size of the table
        int TableSize = 2000;

        GSS testGSS = new GSS(width, range, p_num, size, f_num, usehashtable, TableSize, HashFunction.hashfunctions.BOB1);

      //  String fileName = "src/main/resources/out.wiki_talk_ht";
        //大一些的数据集
        String fileName = "src/main/resources/out.wiki_talk_lv_tsv";
        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);

        String line;
        int count = -1;

        int test = 5;

        while ((line = br.readLine()) != null) {
            count++;
            //跳过首行
            if (count == 0) {
                continue;
            }
            String[] temp = line.split("\\s+");
            if(count == test){
                System.out.println(temp[0]+"==="+temp[1]+"==="+Integer.parseInt(temp[2]));
            }

            testGSS.insert(temp[0], temp[1], Integer.parseInt(temp[2]));

        }
        br.close();
        //输出总共的有效行数
        System.out.println("the number of lines: " + count);

        System.out.print("begin: ");
        System.out.print("\n");


        System.out.print("Predicted: =========");
        System.out.print("\n");


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



        System.out.print(testGSS.edgeQuery("1", "2"));//1
        System.out.print("\n");
        System.out.print(testGSS.query("1", "2"));//true 1
        System.out.print("\n");

        System.out.print(testGSS.edgeQuery("3", "2"));//1
        System.out.print("\n");
        System.out.print(testGSS.query("3", "2"));//true 1
        System.out.print("\n");

        System.out.print(testGSS.edgeQuery("1", "7"));//0
        System.out.print("\n");
        System.out.print(testGSS.query("1", "7"));//true 1
        System.out.print("\n");

        testGSS.cleanupBuffer();

        System.out.print("end.");
        System.out.print("\n");


    }
}
