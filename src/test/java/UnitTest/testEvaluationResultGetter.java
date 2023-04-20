package UnitTest;

import GraphStreamSketch.GSS;
import GraphStreamSketch.HashFunction;
import GraphStreamSketch.ListGraph;
import ResultEvaluation.EvaluationResultGetter;


import java.io.*;

public class testEvaluationResultGetter {
    public static void main(String[] args) throws IOException {

        /*初始化GSS测试参数*/
        // the side length of matrix
        int width = 400;
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
        //更大的数据集
      // String fileName = "src/main/resources/out.wiki_talk_lv1";
        //最大的数据集
       String fileName = "src/main/resources/out.lkml-reply1";
        GSS testGSS = new GSS(width, range, p_num, size, f_num, usehashtable, TableSize, HashFunction.hashfunctions.BOB1);
        ListGraph testListGraph = new ListGraph();
        //通过测试
        //  System.out.println(EvaluationResultGetter.getAverageRelativeError(UnitTest.testGSS,UnitTest.testListGraph,fileName));

        //通过测试
        //  System.out.println(EvaluationResultGetter.getAveragePrecision(UnitTest.testGSS,UnitTest.testListGraph,fileName));


        //小数据集
        //  String fileName = "src/main/resources/out.wiki_talk_ht";
        //大一些的数据集

        //测试通过 负向优化 参数需要调整
        //  EvaluationResultGetter.getCompressionRatio(UnitTest.testGSS, UnitTest.testListGraph, fileName);
        //测试通过
        //   EvaluationResultGetter.getBufferPercentage(UnitTest.testGSS,fileName);

        // System.out.println( EvaluationResultGetter.getAveragePrecision(UnitTest.testGSS,UnitTest.testListGraph,fileName));
        // 开始时间
        long stime1 = System.currentTimeMillis();

        EvaluationResultGetter.getAllResults(testGSS, testListGraph, fileName);
        // 结束时间
        long etime1 = System.currentTimeMillis();
        // 计算执行时间
        long gap1 = etime1 - stime1;
        System.out.println("将原始文件通过GSS获取指标时长为" + gap1 + "毫秒.");

    }
}
