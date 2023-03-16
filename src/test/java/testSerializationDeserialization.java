import GraphStreamSketch.GSS;
import GraphStreamSketch.HashFunction;

import java.io.*;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class testSerializationDeserialization {
    public static void main(String[] args) throws UnknownHostException {
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

        testGSS.insert("1", "2", 1);
        testGSS.insert("3", "2", 1);
        testGSS.insert("1", "2", 1);
        testGSS.insert("4", "2", 1);
        testGSS.insert("6", "5", 1);
        testGSS.insert("1", "2", 1);
        testGSS.insert("2", "7", 1);
        System.out.print("begin: ");
        System.out.print("\n");


        System.out.print("Predicted: =========");
        System.out.print("\n");


        ArrayList<String> testIDS = new ArrayList<String>();
        testGSS.nodeSuccessorQuery("1", testIDS);
        System.out.print("test nodeSuccessorQuery:");
        System.out.print("\n");
        for (String it : testIDS) {
            System.out.print(it);
            System.out.print("\n");
        }
        System.out.print("test nodeSuccessorQuery end");
        System.out.print("\n");


        System.out.print(testGSS.edgeQuery("1", "2"));//3
        System.out.print("\n");
        System.out.print(testGSS.query("1", "2"));//true
        System.out.print("\n");

        System.out.print(testGSS.edgeQuery("3", "2"));//1
        System.out.print("\n");
        System.out.print(testGSS.query("3", "2"));//true
        System.out.print("\n");

        System.out.print(testGSS.edgeQuery("1", "7"));//0
        System.out.print("\n");
        System.out.print(testGSS.query("1", "7"));//true
        System.out.print("\n");

        testGSS.cleanupBuffer();

        System.out.print("end.");
        System.out.print("\n");

       //将testGSS的内容全部写到GSS.dat文件中去
        testGSS.serialize("src/main/resources/GSS.dat");
        System.out.println(testGSS.IPaddress);
//        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("src/main/resources/GSS.dat"))) {
//            out.writeObject(testGSS);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        GSS testGSS1 = testGSS.deserialize("src/main/resources/GSS.dat");
//        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("src/main/resources/GSS.dat"))) {
//            testGSS1 = (GSS) in.readObject();
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }


        System.out.print("begin: ");
        System.out.print("\n");


        System.out.print("Predicted: =========");
        System.out.print("\n");


        ArrayList<String> testIDS1 = new ArrayList<String>();
        testGSS1.nodeSuccessorQuery("1", testIDS1);
        System.out.print("test nodeSuccessorQuery:");
        System.out.print("\n");
        for (String it : testIDS1) {
            System.out.print(it);
            System.out.print("\n");
        }
        System.out.print("test nodeSuccessorQuery end");
        System.out.print("\n");


        System.out.print(testGSS1.edgeQuery("1", "2"));//3
        System.out.print("\n");
        System.out.print(testGSS1.query("1", "2"));//true
        System.out.print("\n");

        System.out.print(testGSS1.edgeQuery("3", "2"));//1
        System.out.print("\n");
        System.out.print(testGSS1.query("3", "2"));//true
        System.out.print("\n");

        System.out.print(testGSS1.edgeQuery("1", "7"));//0
        System.out.print("\n");
        System.out.print(testGSS1.query("1", "7"));//true
        System.out.print("\n");

        testGSS1.cleanupBuffer();

        System.out.print("end.");
        System.out.print("\n");

    }
}
