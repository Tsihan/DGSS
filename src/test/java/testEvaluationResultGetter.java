import GraphStreamSketch.GSS;
import GraphStreamSketch.HashFunction;
import GraphStreamSketch.ListGraph;
import ResultEvaluation.EvaluationResultGetter;


import java.io.*;

public class testEvaluationResultGetter {
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
        ListGraph testListGraph = new ListGraph();
        //通过测试
       // System.out.println(EvaluationResultGetter.getAverageRelativeError(testGSS,testListGraph,"src/main/resources/out.wiki_talk_ht"));
        //通过测试
       // EvaluationResultGetter.getDeduplicatedNodes("src/main/resources/out.wiki_talk_ht");
        //通过测试
       // System.out.println(EvaluationResultGetter.getAveragePrecision(testGSS,testListGraph,"src/main/resources/out.wiki_talk_ht"));





        //小数据集
     //  String fileName = "src/main/resources/out.wiki_talk_ht";
        //大一些的数据集
         String fileName = "src/main/resources/out.wiki_talk_lv_tsv";
        //测试通过 负向优化 参数需要调整
        //EvaluationResultGetter.getCompressionRatio(testGSS,testListGraph,fileName);
        EvaluationResultGetter.getBufferPercentage(testGSS,fileName);


    }
}
