package UnitTest;

import GraphStreamSketch.GSS;
import GraphStreamSketch.HashFunction;

import java.io.*;
import java.util.ArrayList;

public class testGSSFileArray {
    public static void main(String[] args) throws IOException {
        /*初始化GSS测试参数*/
        // the side length of matrix
        int width = 2000;
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
        int TableSize = 2000;
        ArrayList<GSS> testGSSArray = new ArrayList<>();

        for (HashFunction.hashfunctions hashfunc : HashFunction.hashfunctions.values()) {
            testGSSArray.add(new GSS(width, range, p_num, size, f_num, usehashtable, TableSize, hashfunc));
        }
      //  System.out.println(testGSSArray.size());


        String fileName = "src/main/resources/out.wiki_talk_ht";
        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);

        String line;
        int count = -1;



        while ((line = br.readLine()) != null) {
            count++;
            //跳过首行
            if (count == 0) {
                continue;
            }
            String[] temp = line.split("\\s+");

            for(GSS gss: testGSSArray){
                gss.insert(temp[0], temp[1], Integer.parseInt(temp[2]));
            }


        }
        br.close();
        //输出总共的有效行数
        System.out.println("the number of lines: " + count);

        System.out.print("begin: ");
        System.out.print("\n");


        System.out.print("Predicted: =========");
        System.out.print("\n");



        for(GSS gss:testGSSArray){
            System.out.println(gss.hft);
            System.out.print(gss.edgeQuery("1", "2"));//1
            System.out.print("\n");
            System.out.print(gss.query("1", "2"));//true 1
            System.out.print("\n");

            System.out.print(gss.edgeQuery("3", "2"));//1
            System.out.print("\n");
            System.out.print(gss.query("3", "2"));//true 1
            System.out.print("\n");

            System.out.print(gss.edgeQuery("1", "7"));//0
            System.out.print("\n");
            System.out.print(gss.query("1", "7"));//true 1
            System.out.print("\n");

            gss.cleanupBuffer();
        }


        System.out.print("end.");
        System.out.print("\n");


    }
}
